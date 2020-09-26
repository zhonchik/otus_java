package ru.otus.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import ru.otus.controllers.AggregatorController;

@Slf4j
public class ListCommand extends BotCommand {
    private final AggregatorController controller;

    public ListCommand(AggregatorController controller) {
        super("list", "List my feeds");
        this.controller = controller;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        log.info("List command from {}", chat);

        long chatId = chat.getId();
        if (!controller.hasChat(chatId)) {
            controller.sendTextMessage(chatId, "Execute /start to add bot");
            return;
        }

        String message = String.join("\n", controller.getChatFeeds(chatId));
        controller.sendTextMessage(chatId, message);
    }
}
