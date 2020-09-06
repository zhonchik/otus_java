package ru.otus.feeds;

import java.time.Duration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "feeds", ignoreUnknownFields = false)
@Data
public class FeedReaderProperties {
    private Duration updateInterval = Duration.ofSeconds(10);
    private Duration queueOfferInterval = Duration.ofSeconds(1);
    private Duration shutdownTimeout = Duration.ofSeconds(5);
}
