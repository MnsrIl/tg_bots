package com.katastudy;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RubExchangeRate extends Bot {
    Pattern datePattern = Pattern.compile("^(?:0[1-9]|1\\d|2\\d|3[0-1])/(?:0[1-9]|1[0-2])/(?:1989|199\\d|20[01]\\d|202[0-2])$", 0x08);
    Pattern currencyPattern = Pattern.compile("^[A-Z]{3}$", 0x08);

    @Override
    public void sendMessage(Update update) {
        System.out.printf("Message №%d ", update.updateId());

        try {
            Message message = getMessage(update);
            System.out.printf("From @%s\n", message.from().username());

            String messageText = message.text();
            long chatId = message.chat().id();

            SendMessage request;

            boolean isStartCommand = messageText.equals("/start");
            boolean isInvalidRequest = messageText.split(" ").length != 2;

            if (isStartCommand) {
                String welcomeMessage = """
                        Welcome to *exchanger*!
                        To start, input a __valid__ request with format:
                        `dd/MM/yyyy currency`
                        Where:
                        `dd/MM/yyyy` - date of rates
                        `currency` - the currency you want to see

                        Example: `09/08/2022 GBP` - gets pound rate for today""";

                request = new SendMessage(chatId, welcomeMessage)
                        .parseMode(ParseMode.Markdown);
            } else if (isInvalidRequest) {
                request = new SendMessage(chatId, "Invalid request");
            } else {
                String[] text = message.text().split(" ");

                String date = text[0],
                        currency = text[1],
                        responseText;

                boolean isValidCurrency = currencyPattern.matcher(currency).matches();
                boolean isValidDate = datePattern.matcher(date).matches();

                if (!isValidCurrency) {
                    responseText = "Invalid currency format";
                } else if (!isValidDate) {
                    responseText = "Invalid date format";
                } else {
                    String exchangeCurrencyToRub = getExchangeRate(date, currency);
                    responseText = String.format("Date - %s\n1 %s = %s RUB", date, currency, exchangeCurrencyToRub);
                }

                request = new SendMessage(chatId, responseText);
            }

            getBot().execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getExchangeRate(String date, String currency) {
        String API_URL = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + date;
        String exchangedValue = null;

        try (InputStream inputConnectionStream = new URL(API_URL).openStream()) {
            String response = new String(inputConnectionStream.readAllBytes());

            exchangedValue = xmlParser(response, currency);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exchangedValue;
    }

    private static String xmlParser(String xmlNode, String currency) {
        String result = null;
        Pattern pattern = Pattern.compile(String.format("<CharCode>%s</CharCode>(.*?)<Value>(.*?)</Value>", currency));
        Matcher matcher = pattern.matcher(xmlNode);

        if (matcher.find() && matcher.groupCount() == 2) {
            result = matcher.group(2);
        }

        return result;
    }
}
