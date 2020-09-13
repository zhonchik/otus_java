package ru.otus.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import ru.otus.controllers.AggregatorController;
import ru.otus.controllers.AggregatorControllerImpl;
import ru.otus.feeds.FeedReaderProperties;
import ru.otus.processors.MessageProcessor;

@Component
@EnableConfigurationProperties({AggregatorServiceProperties.class, FeedReaderProperties.class})
@Slf4j
public class AggregatorService implements InitializingBean, DisposableBean {
    private ExecutorService executorService;
    private final AggregatorServiceProperties serviceProperties;
    private final FeedReaderProperties feedReaderProperties;
    private final MessageProcessor messageProcessor;
    private final AggregatorController controller;


    public AggregatorService(AggregatorServiceProperties serviceProperties, FeedReaderProperties feedReaderProperties) {
        this.serviceProperties = serviceProperties;
        this.feedReaderProperties = feedReaderProperties;

        controller = new AggregatorControllerImpl(serviceProperties.getBotToken());
        messageProcessor = new MessageProcessor(controller);
    }

    public void afterPropertiesSet() {
        executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try (var reader = controller.newFeedReader(feedReaderProperties)) {
                    while (!Thread.currentThread().isInterrupted()) {
                        var updates = reader.getUpdates();
                        if (updates.isEmpty()) {
                            continue;
                        }
                        log.info("{}", updates);
                        messageProcessor.process(updates);
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
