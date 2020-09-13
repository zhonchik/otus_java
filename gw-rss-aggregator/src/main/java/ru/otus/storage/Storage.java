package ru.otus.storage;

import java.net.URL;
import java.util.List;

import ru.otus.model.Feed;
import ru.otus.model.Message;

public interface Storage {
    void addChat(long chatId);
    void removeChat(long chatId);
    boolean hasChat(long chatId);
    boolean subscribe(long chatId, URL url);
    boolean unsubscribe(long chatId, URL url);
    Feed getFeed(URL url);
    List<Feed> getFeeds();
    void markAsProcessed(List<Message> messages);
    boolean isProcessed(Message message);
}
