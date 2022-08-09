package com.katastudy;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.Optional;

public class OChanger extends Bot {
    @Override
    protected void sendMessage(Update update) {
        try {
            Message message = Optional.ofNullable(update.message()).orElse(update.editedMessage());

            String messageText = message.text();
            long chatId = message.chat().id();

            String responseText = messageText.replaceAll("о", "обро");

            if (responseText.equals(messageText)) {
                return;
            }

            SendMessage request = new SendMessage(chatId,  responseText);
            System.out.println("Sending message.." + responseText);
            this.getBot().execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
