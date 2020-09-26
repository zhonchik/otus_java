package ru.otus.bot.commands;

import java.util.function.BiFunction;

import org.telegram.telegrambots.meta.api.objects.Chat;

import ru.otus.controllers.AggregatorController;

public class CommandHelper {

    public static void executeMultiArg(
            AggregatorController controller,
            Chat chat,
            String[] arguments,
            BiFunction<Long, String, SubCommandResult> func
    ) {
        long chatId = chat.getId();
        if (!controller.hasChat(chatId)) {
            controller.sendTextMessage(chatId, "Execute /start to add bot");
            return;
        }

        if (arguments.length == 0) {
            controller.sendTextMessage(chatId, "One or more feed urls expected");
            return;
        }

        StringBuilder messageBuilder = new StringBuilder();
        for (var argument : arguments) {
            var result = func.apply(chatId, argument);
            messageBuilder.append("\n");
            messageBuilder.append(String.format("%s %s %s", result.getEmoji(), argument, result.getMessage()));
        }

        controller.sendTextMessage(chatId, messageBuilder.toString());
    }
}
