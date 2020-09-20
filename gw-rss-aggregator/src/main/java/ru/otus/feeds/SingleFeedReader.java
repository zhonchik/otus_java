package ru.otus.feeds;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import lombok.extern.slf4j.Slf4j;
import ru.otus.model.Feed;
import ru.otus.model.Message;

@Slf4j
public class SingleFeedReader {
    private final Feed feed;
    private final SyndFeedInput input;

    public SingleFeedReader(Feed feed) {
        this.feed = feed;
        input = new SyndFeedInput();
    }

    public boolean checkUrl() {
        try {
            getEntries();
        } catch (Exception e) {
            log.error("Feed check failed", e);
            return false;
        }
        return true;
    }

    public List<Message> getUpdates() throws IOException, FeedException {
        List<Message> updates = new ArrayList<>();
        for (var item : getEntries()) {
            for (var chat : feed.getChats()) {
                updates.add(Message.NewMessage(chat, item.getLink()));
            }
        }
        return updates;
    }

    private List<SyndEntry> getEntries() throws IOException, FeedException {
        URL url = new URL(feed.getUrl());
        var reader = new XmlReader(url);
        var feedData = input.build(reader);
        return feedData.getEntries();
    }
}
