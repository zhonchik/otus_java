package ru.otus.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import ru.otus.controllers.AggregatorController;

@Slf4j
public class HelpCommand extends BotCommand {
    private final AggregatorController controller;
    private final ICommandRegistry commandRegistry;

    public HelpCommand(AggregatorController controller, ICommandRegistry commandRegistry) {
        super("help", "Available commands");
        this.controller = controller;
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        log.info("Help command from {}", chat);
        StringBuilder helpMessageBuilder = new StringBuilder();
        for (IBotCommand botCommand : commandRegistry.getRegisteredCommands()) {
            helpMessageBuilder.append(botCommand.toString()).append("\n");
        }
        controller.sendTextMessage(chat.getId(), helpMessageBuilder.toString());
    }
}
