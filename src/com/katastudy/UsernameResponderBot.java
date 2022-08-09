package com.katastudy;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.Optional;

public class UsernameResponderBot extends Bot {
    @Override
    protected void sendMessage(Update update) {
        try {
            Message message = Optional.ofNullable(update.message()).orElse(update.editedMessage());
            String username = message.chat().username();

            SendMessage request = new SendMessage(message.chat().id(), username);

            this.getBot().execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
