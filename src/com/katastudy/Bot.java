package com.katastudy;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

public abstract class Bot {
    private static final String BOT_TOKEN = "";
    private final TelegramBot bot = new TelegramBot(BOT_TOKEN);

    public void start() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::sendMessage);

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public TelegramBot getBot() {
        return this.bot;
    }

    public static Message getMessage(Update update) {
        return update.message() == null ? update.editedMessage() : update.message();
    }

    abstract void sendMessage(Update update);
}
