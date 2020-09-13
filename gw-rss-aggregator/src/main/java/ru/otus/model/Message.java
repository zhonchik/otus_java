package ru.otus.model;

import java.net.URL;

import lombok.Data;

@Data
public class Message {
    private final long chatId;
    private final URL link;
}
