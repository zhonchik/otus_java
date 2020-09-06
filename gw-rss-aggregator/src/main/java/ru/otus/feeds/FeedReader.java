package ru.otus.feeds;

import java.io.IOException;
import java.util.List;

import com.rometools.rome.io.FeedException;

import ru.otus.model.Message;

public interface FeedReader extends AutoCloseable {
    List<Message> getUpdates() throws IOException, FeedException;
}
