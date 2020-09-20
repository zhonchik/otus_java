package ru.otus.controllers;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.otus.bot.commands.SubCommandResult;
import ru.otus.bot.commands.CommandsHandler;
import ru.otus.feeds.FeedReader;
import ru.otus.feeds.FeedReaderProperties;
import ru.otus.feeds.MultiFeedReader;
import ru.otus.model.Message;
import ru.otus.storage.Storage;
import ru.otus.storage.StorageImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Slf4j
public class AggregatorControllerImpl implements AggregatorController {
    private final static String STATUS_OK = "✅";
    private final static String STATUS_ERR = "⚠️";

    private final Storage storage;
    private CommandsHandler commandsHandler;
    private FeedReader feedReader;

    public AggregatorControllerImpl(String botToken) {
        storage = new StorageImpl();

        try {
            ApiContextInitializer.init();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            try {
                commandsHandler = new CommandsHandler(this, botToken);
                telegramBotsApi.registerBot(commandsHandler);
            } catch (TelegramApiException e) {
                log.error("Failed to register bot", e);
            }
        } catch (Exception e) {
            log.error("Failed to create API", e);
        }
    }

    @Override
    public void addChat(long chatId) {
        storage.addChat(chatId);
    }

    @Override
    public void removeChat(long chatId) {
        storage.removeChat(chatId);
    }

    @Override
    public boolean hasChat(long chatId) {
        return storage.hasChat(chatId);
    }

    @Override
    public SubCommandResult trySubscribe(long chatId, String argument) {
        URL url;
        try {
            url = new URL(argument);
        } catch (MalformedURLException e) {
            return new SubCommandResult(STATUS_ERR, "wrong URI");
        }
        if (!feedReader.checkUrl(url)) {
            return new SubCommandResult(STATUS_ERR, "feed type not supported");
        }
        if (storage.subscribe(chatId, url)) {
            feedReader.addFeed(storage.getFeed(url));
        }
        return new SubCommandResult(STATUS_OK, "");
    }

    @Override
    public SubCommandResult tryUnsubscribe(long chatId, String argument) {
        URL url;
        try {
            url = new URL(argument);
        } catch (MalformedURLException e) {
            return new SubCommandResult(STATUS_ERR, "wrong URI");
        }
        if (!storage.unsubscribe(chatId, url)) {
            return new SubCommandResult(STATUS_ERR, "was not subscribed");
        }
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
    public FeedReader newFeedReader(FeedReaderProperties properties) {
        feedReader = new MultiFeedReader(properties, storage.getFeeds());
        return feedReader;
    }

    @Override
    public void markAsProcessed(List<Message> messages) {
        storage.markAsProcessed(messages);
    }

    @Override
    public boolean isProcessed(Message message) {
        return storage.isProcessed(message);
    }
}
