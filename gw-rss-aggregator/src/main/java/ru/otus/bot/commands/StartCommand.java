package ru.otus.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import ru.otus.controllers.AggregatorController;

@Slf4j
public class StartCommand extends BotCommand {
    private final AggregatorController controller;

    public StartCommand(AggregatorController controller) {
        super("start", "Start bot");
        this.controller = controller;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        log.info("Start command from {}", chat);

        StringBuilder messageBuilder = new StringBuilder();

        var chatId = chat.getId();
        if (controller.hasChat(chatId)) {
            messageBuilder.append("I know you");
        }
        else {
            controller.addChat(chatId);

            String userName = String.format("%s %s", user.getFirstName(), user.getLastName());
            messageBuilder.append("Hi, ");
            messageBuilder.append(userName);
        }

        controller.sendTextMessage(chatId, messageBuilder.toString());
    }
}
