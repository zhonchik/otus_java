package ru.otus.storage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;
import ru.otus.model.Feed;
import ru.otus.model.Message;

@Component
public class StorageImpl implements Storage {
    private final Set<Long> chats = new HashSet<>();
    private final Map<URL, Feed> feeds = new HashMap<>();
    private final Set<Message> processedMessages = new HashSet<>();

    @Override
    synchronized public void addChat(long chatId) {
        chats.add(chatId);
    }

    @Override
    synchronized public void removeChat(long chatId) {
        for (var feed : feeds.values()) {
            feed.getChats().remove(chatId);
        }
        chats.remove(chatId);
    }

    @Override
    public boolean hasChat(long chatId) {
        return chats.contains(chatId);
    }

    @Override
    synchronized public boolean subscribe(long chatId, URL url) {
        boolean result = false;
        var feed = feeds.get(url);
        if (feed == null) {
            feed = new Feed(url, new HashSet<>());
            feeds.put(url, feed);
            result = true;
        }
        feed.getChats().add(chatId);
        return result;
    }

    @Override
    synchronized public boolean unsubscribe(long chatId, URL url) {
        var feed = feeds.get(url);
        if (feed == null) {
            return false;
        }
        return feed.getChats().remove(chatId);
    }

    @Override
    public Feed getFeed(URL url) {
        return feeds.get(url);
    }

    @Override
    public List<Feed> getFeeds() {
        return new ArrayList<>(feeds.values());
    }

    @Override
    synchronized public void markAsProcessed(List<Message> messages) {
        processedMessages.addAll(messages);
    }

    @Override
    public boolean isProcessed(Message message) {
        return processedMessages.contains(message);
    }
}
