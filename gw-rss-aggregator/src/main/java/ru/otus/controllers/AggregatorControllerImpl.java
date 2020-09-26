package ru.otus.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.otus.bot.commands.SubCommandResult;
import ru.otus.bot.commands.CommandsHandler;
import ru.otus.feeds.FeedReaderProperties;
import ru.otus.feeds.MultiFeedReader;
import ru.otus.model.Feed;
import ru.otus.model.Message;
import ru.otus.services.AggregatorServiceProperties;
import ru.otus.storage.Storage;

@Component
@EnableConfigurationProperties(AggregatorServiceProperties.class)
@Slf4j
public class AggregatorControllerImpl implements AggregatorController {
    private final static String STATUS_OK = "\u2714";
    private final static String STATUS_ERR = "\u2757";

    private final Set<Long> chats = new HashSet<>();
    private final Map<URL, Feed> feeds = new HashMap<>();

    private final Storage storage;
    private CommandsHandler commandsHandler;
    private MultiFeedReader feedReader;

    public AggregatorControllerImpl(Storage storage, AggregatorServiceProperties serviceProperties) throws IOException {
        this.storage = storage;

        for (var feed : storage.getFeeds()) {
            feeds.put(new URL(feed.getUrl()), feed);
            chats.addAll(feed.getChats());
        }

        try {
            ApiContextInitializer.init();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            try {
                commandsHandler = new CommandsHandler(
                        this,
                        serviceProperties.getBotUserName(),
                        serviceProperties.getBotToken()
                );
                telegramBotsApi.registerBot(commandsHandler);
            } catch (TelegramApiException e) {
                log.error("Failed to register bot", e);
            }
        } catch (Exception e) {
            log.error("Failed to create API", e);
        }
    }

    @Override
    synchronized public void addChat(long chatId) {
        chats.add(chatId);
    }

    @Override
    synchronized public void removeChat(long chatId) {
        storage.removeChat(chatId);
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
    synchronized public SubCommandResult trySubscribe(long chatId, String argument) {
        URL url;
        try {
            url = new URL(argument);
        } catch (MalformedURLException e) {
            return new SubCommandResult(STATUS_ERR, "wrong URI");
        }
        String urlString = url.toString();
        if (!feedReader.checkUrl(urlString)) {
            return new SubCommandResult(STATUS_ERR, "feed type not supported");
        }
        boolean needNewReader = false;
        storage.subscribe(chatId, urlString);
        var feed = feeds.get(url);
        if (feed == null) {
            feed = storage.getFeed(urlString);
            feeds.put(url, feed);
            needNewReader = true;
        }
        if (feed.getChats().isEmpty()) {
            feed.getChats().add(chatId);
            needNewReader = true;
        }
        if (needNewReader) {
            feedReader.addFeed(feed);
        }
        return new SubCommandResult(STATUS_OK, "");
    }

    @Override
    synchronized public SubCommandResult tryUnsubscribe(long chatId, String argument) {
        URL url;
        try {
            url = new URL(argument);
        } catch (MalformedURLException e) {
            return new SubCommandResult(STATUS_ERR, "wrong URI");
        }
        var feed = feeds.get(url);
        if (feed == null) {
            return new SubCommandResult(STATUS_ERR, "was not subscribed");
        }
        var chats = feed.getChats();
        if (!chats.contains(chatId)) {
            return new SubCommandResult(STATUS_ERR, "was not subscribed");
        }
        chats.remove(chatId);
        storage.unsubscribe(chatId, url.toString());
        return new SubCommandResult(STATUS_OK, "");
    }

    @Override
    public boolean sendTextMessage(long chatId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);
        message.setParseMode("html");
        try {
            commandsHandler.execute(message);
            return true;
        } catch (TelegramApiException e) {
            log.error("Failed to send message", e);
        }
        return false;
    }

    @Override
    public MultiFeedReader newFeedReader(FeedReaderProperties properties) {
        feedReader = new MultiFeedReader(properties, new ArrayList<>(feeds.values()));
        return feedReader;
    }

    @Override
    synchronized public void markAsProcessed(List<Message> messages) {
        storage.markAsProcessed(messages);
    }

    @Override
    public boolean isProcessed(Message message) {
        return storage.isProcessed(message);
    }
}
