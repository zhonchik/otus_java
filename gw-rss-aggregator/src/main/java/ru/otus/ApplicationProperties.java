package ru.otus;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
}
