package com.example.HeadsOrTails.service;

import com.example.HeadsOrTails.config.BotConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig botConfig;
    final String WELCOME_MESSAGE = "Вас приветствует помощник для игры в монетку!";
    final String path = "c://var//temp.txt";

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Keyboard keyboard = new Keyboard();
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    sendKeyboard(chatId, WELCOME_MESSAGE, keyboard.getCreateKeyboard());
                    break;
                case "Победа!":
                    saveResult("Победа");
                    break;
                case "Поражение!":
                    saveResult("Поражение");
                    break;
                default:
                    if (StringUtils.isNumeric(messageText)) {
                        try {
                            saveDate();
                            saveText(messageText);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
            }
        }

    }

    private void saveResult(String result) {
        try {
            saveText(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveDate() throws IOException {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        FileWriter writer = new FileWriter(path);
        BufferedWriter bufferWriter = new BufferedWriter(writer);
        bufferWriter.write(formatter.format(date));
        bufferWriter.close();
    }

    private void saveText(String text) throws IOException {
        FileWriter writerWithAppend = new FileWriter(path, true);
        BufferedWriter bufferWriter = new BufferedWriter(writerWithAppend);
        bufferWriter.write("/" + text);
        bufferWriter.close();
    }

    private void sendKeyboard(long chatId, String textToSend, ReplyKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.setReplyMarkup(keyboard);

        executeMessage(message);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Error");
        }
    }
}
