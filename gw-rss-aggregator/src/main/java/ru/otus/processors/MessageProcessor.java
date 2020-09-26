package ru.otus.processors;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.controllers.AggregatorController;
import ru.otus.model.Message;

@Component
@Slf4j
public class MessageProcessor {
    private final AggregatorController controller;

    public MessageProcessor(AggregatorController controller) {
        this.controller = controller;
    }

    public void process(List<Message> messages) {
        var processedMessages = new ArrayList<Message>();
        for (var message : messages) {
            if (controller.isProcessed(message)) {
                continue;
            }
            log.info("New message to send {}", message);
            if (controller.sendTextMessage(message.getChatId(), message.getLink().toString())) {
                processedMessages.add(message);
            }
        }
        controller.markAsProcessed(processedMessages);
    }
}
