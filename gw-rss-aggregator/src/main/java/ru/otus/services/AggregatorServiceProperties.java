package ru.otus.services;

import java.time.Duration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "aggregator", ignoreUnknownFields = false)
@Data
public class AggregatorServiceProperties {
    private Duration updateInterval = Duration.ofSeconds(1);
    private Duration shutdownTimeout = Duration.ofSeconds(1);
}
