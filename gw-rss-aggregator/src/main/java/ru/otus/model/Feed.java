package ru.otus.model;

import java.net.URL;
import java.util.List;

import lombok.Data;

@Data
public class Feed {
    private final URL url;
    private final List<Chat> chats;
}
