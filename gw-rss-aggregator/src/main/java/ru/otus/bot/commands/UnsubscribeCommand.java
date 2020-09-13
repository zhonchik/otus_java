package ru.otus.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import ru.otus.controllers.AggregatorController;

@Slf4j
public class UnsubscribeCommand extends BotCommand {
    private final AggregatorController controller;

    public UnsubscribeCommand(AggregatorController controller) {
        super("unsubscribe", "Unsubscribe from feed. Few feeds line separated supported");
        this.controller = controller;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        log.info("Unsubscribe command from {} with {}", chat, arguments);
        CommandHelper.executeMultiArg(controller, chat, arguments, controller::tryUnsubscribe);
    }
}
