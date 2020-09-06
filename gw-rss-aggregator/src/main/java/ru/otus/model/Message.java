package ru.otus.model;

import java.net.URL;

import lombok.Data;

@Data
public class Message {
    private final Chat chat;
    private final URL link;
}
