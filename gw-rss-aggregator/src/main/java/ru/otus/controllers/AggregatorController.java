package ru.otus.controllers;

import java.util.List;

import ru.otus.bot.commands.SubCommandResult;
import ru.otus.feeds.FeedReader;
import ru.otus.feeds.FeedReaderProperties;
import ru.otus.model.Message;

public interface AggregatorController {
    void addChat(long chatId);
    void removeChat(long chatId);
    boolean hasChat(long chatId);
    SubCommandResult trySubscribe(long chatId, String argument);
    SubCommandResult tryUnsubscribe(long chatId, String argument);

    boolean sendTextMessage(long chatId, String messageText);

    FeedReader newFeedReader(FeedReaderProperties properties);
    void markAsProcessed(List<Message> messages);
    boolean isProcessed(Message message);
}
