package ru.otus.services;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import ru.otus.feeds.FeedReaderProperties;
import ru.otus.feeds.MultiFeedReader;
import ru.otus.model.Chat;
import ru.otus.model.Feed;

@Component
@EnableConfigurationProperties({AggregatorServiceProperties.class, FeedReaderProperties.class})
@Slf4j
public class AggregatorService implements InitializingBean, DisposableBean {
    private ExecutorService executorService;
    private final AggregatorServiceProperties serviceProperties;
    private final FeedReaderProperties feedReaderProperties;

    public AggregatorService(AggregatorServiceProperties serviceProperties, FeedReaderProperties feedReaderProperties) {
        this.serviceProperties = serviceProperties;
        this.feedReaderProperties = feedReaderProperties;
    }

    public void afterPropertiesSet() throws MalformedURLException {
        // Just for feed reader check
        // To be removed
        List<Feed> feeds = new ArrayList<>();
        List<Chat> chats = new ArrayList<>();
        chats.add(new Chat("chat0", 123));
        feeds.add(new Feed(new URL("https://habr.com/ru/rss/all/all/"), chats));
        // End of feed reader check

        executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try (var reader = new MultiFeedReader(feedReaderProperties, feeds)) {
                    while (!Thread.currentThread().isInterrupted()) {
                        var updates = reader.getUpdates();
                        log.info("{}", updates);
                        Thread.sleep(serviceProperties.getUpdateInterval().toMillis());
                    }
                }
                catch (Exception e) {
                    log.warn("Execution exception", e);
                }
            }
        });
    }

    public void destroy() throws InterruptedException {
        log.info("Closing service");
        executorService.awaitTermination(serviceProperties.getShutdownTimeout().toMillis(), TimeUnit.MILLISECONDS);
        log.info("Service closed");
    }
}
