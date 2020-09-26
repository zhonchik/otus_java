package ru.otus.storage;

import java.util.List;

import ru.otus.model.Feed;
import ru.otus.model.Message;

public interface Storage {
    void removeChat(long chatId);
    void subscribe(long chatId, String url);
    void unsubscribe(long chatId, String url);
    Feed getFeed(String url);
    List<Feed> getFeeds();
    void markAsProcessed(List<Message> messages);
    boolean isProcessed(Message message);
}
