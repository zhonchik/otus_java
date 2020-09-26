package ru.otus.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import static org.dizitart.no2.objects.filters.ObjectFilters.and;
import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

import ru.otus.model.Feed;
import ru.otus.model.Message;

@Component
@EnableConfigurationProperties(StorageProperties.class)
public class StorageImpl implements Storage {
    private final ObjectRepository<Feed> feedRepository;
    private final ObjectRepository<Message> processedMessageRepository;

    public StorageImpl(StorageProperties properties) {
        Nitrite db = Nitrite.builder()
                .compressed()
                .filePath(properties.getFilePath())
                .openOrCreate(properties.getUser(), properties.getPassword());

        feedRepository = db.getRepository(Feed.class);
        processedMessageRepository = db.getRepository(Message.class);
    }

    @Override
    public void removeChat(long chatId) {
        for (var feed : feedRepository.find()) {
            feed.getChats().remove(chatId);
            feedRepository.update(feed);
        }
    }

    @Override
    public void subscribe(long chatId, String url) {
        var feed = feedRepository.find(eq("url", url)).firstOrDefault();
        if (feed == null) {
            feed = Feed.NewFeed(url, new HashSet<>());
            feed.getChats().add(chatId);
            feedRepository.insert(feed);
        } else {
            feed.getChats().add(chatId);
            feedRepository.update(feed);
        }
    }

    @Override
    public void unsubscribe(long chatId, String url) {
        var feed = feedRepository.find(eq("url", url)).firstOrDefault();
        if (feed == null) {
            return;
        }
        feed.getChats().remove(chatId);
        feedRepository.update(feed);
    }

    @Override
    public Feed getFeed(String url) {
        return feedRepository.find(eq("url", url)).firstOrDefault();
    }

    @Override
    public List<Feed> getFeeds() {
        var feeds = new ArrayList<Feed>();
        for (var feed : feedRepository.find()) {
            feeds.add(feed);
        }
        return feeds;
    }

    @Override
    public void markAsProcessed(List<Message> messages) {
        for (var message : messages) {
            if (!isProcessed(message)) {
                processedMessageRepository.insert(message);
            }
        }
    }

    @Override
    public boolean isProcessed(Message message) {
        Message processedMessage = processedMessageRepository.find(
                and(
                        eq("chatId", message.getChatId()),
                        eq("link", message.getLink())
                )
        ).firstOrDefault();
        return processedMessage != null;
    }
}
