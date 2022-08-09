package com.katastudy;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class NameResponderBot extends Bot {
    @Override
    public void sendMessage(Update update) {
        try {
            Message message = update.message() != null ? update.message() : update.editedMessage();
            String name = message.from().firstName();

            SendMessage request = new SendMessage(message.chat().id(), String.format("Hello, sir %s\n!", name));
            this.getBot().execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
