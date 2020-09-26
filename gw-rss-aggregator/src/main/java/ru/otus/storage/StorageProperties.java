package ru.otus.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "storage", ignoreUnknownFields = false)
@Data
public class StorageProperties {
    private String filePath;
    private String user;
    private String password;
}
