package ru.otus.feeds;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import ru.otus.model.Feed;
import ru.otus.model.Message;

public class SingleFeedReader implements FeedReader {
    private final Feed feed;
    private final SyndFeedInput input;

    public SingleFeedReader(Feed feed) {
        this.feed = feed;
        input = new SyndFeedInput();
    }

    @Override
    public List<Message> getUpdates() throws IOException, FeedException {
        List<Message> updates = new ArrayList<>();
        for (var item : input.build(new XmlReader(feed.getUrl())).getEntries()) {
            for (var chat : feed.getChats()) {
                updates.add(new Message(chat, new URL(item.getLink())));
            }
        }
        return updates;
    }

    @Override
    public void close() {
    }
}
