package ru.otus.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import ru.otus.controllers.AggregatorController;

@Slf4j
public class CommandsHandler extends TelegramLongPollingCommandBot {
    private final AggregatorController controller;
    private final String botToken;

    public CommandsHandler(AggregatorController controller, String botToken) {
        super();

        this.controller = controller;
        this.botToken = botToken;

        register(new StartCommand(controller));
        register(new StopCommand(controller));
        register(new SubscribeCommand(controller));
        register(new UnsubscribeCommand(controller));
        HelpCommand helpCommand = new HelpCommand(controller,this);
        register(helpCommand);

        registerDefaultAction((absSender, message) -> {
            String messageText = String.format("Unknown command '%s'", message.getText());
            controller.sendTextMessage(message.getChatId(), messageText);
        });
    }

    @Override
    public String getBotUsername() {
        return "";
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            controller.sendTextMessage(message.getChatId(), "Bla bla bla");
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
