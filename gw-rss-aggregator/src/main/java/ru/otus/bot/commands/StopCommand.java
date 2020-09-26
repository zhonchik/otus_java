package ru.otus.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import ru.otus.controllers.AggregatorController;

@Slf4j
public class StopCommand extends BotCommand {
    private final AggregatorController controller;

    public StopCommand(AggregatorController controller) {
        super("stop", "Stop bot");
        this.controller = controller;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        log.info("Stop command from {}", chat);

        StringBuilder messageBuilder = new StringBuilder();

        long chatId = chat.getId();
        if (controller.hasChat(chatId)) {
            messageBuilder.append("Nothing to stop");
        }
        else {
            controller.removeChat(chatId);
            messageBuilder.append(String.format("Bye, %s %s", user.getFirstName(), user.getLastName()));
        }

        controller.sendTextMessage(chatId, messageBuilder.toString());
    }
}
