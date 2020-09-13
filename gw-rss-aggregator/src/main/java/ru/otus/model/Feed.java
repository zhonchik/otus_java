package ru.otus.model;

import java.net.URL;
import java.util.Set;

import lombok.Data;

@Data
public class Feed {
    private final URL url;
    private final Set<Long> chats;
}
