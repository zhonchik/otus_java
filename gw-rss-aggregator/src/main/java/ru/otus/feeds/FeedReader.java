package ru.otus.feeds;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.rometools.rome.io.FeedException;

import ru.otus.model.Feed;
import ru.otus.model.Message;

public interface FeedReader extends AutoCloseable {
    boolean checkUrl(URL url);
    void addFeed(Feed feed);
    List<Message> getUpdates() throws IOException, FeedException;
}
