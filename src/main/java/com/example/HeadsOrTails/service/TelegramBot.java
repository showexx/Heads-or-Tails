package com.example.HeadsOrTails.service;

import com.example.HeadsOrTails.config.BotConfig;

import com.example.HeadsOrTails.models.Users;
import com.example.HeadsOrTails.repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;

    final BotConfig botConfig;
    final String WELCOME_MESSAGE = "Вас приветствует помощник для игры в монетку! Для продолжения введите сумму";
    final Path PATH = Paths.get("c://var//temp.txt");

    TempFile tempFile = new TempFile();

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
                    sendMessage(chatId, WELCOME_MESSAGE);
                    break;
                case "Победа!":
                    tempFile.saveResult("Победа");
                    save(chatId);
                    deleteFile();
                    sendMessage(chatId, "Сохранено!");
                    break;
                case "Поражение!":
                    tempFile.saveResult("Поражение");
                    save(chatId);
                    deleteFile();
                    sendMessage(chatId, "Сохранено!");
                    break;
                default:
                    if (StringUtils.isNumeric(messageText)) {
                        try {
                            tempFile.saveDate();
                            tempFile.saveText(messageText);
                            sendKeyboard(chatId, "Для продолжения выберите результат.", keyboard.getCreateKeyboard());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
            }
        }
    }

    private void save(long chatId) {
        try {
            String result = Files.readString(PATH, StandardCharsets.UTF_8);
            String[] subStr;
            String delimiter = "/";
            subStr = result.split(delimiter);

            Users users = new Users();
            users.setChatId(chatId);
            users.setDate(subStr[0]);
            users.setSum(subStr[1]);
            users.setResult(subStr[2]);
            userRepository.save(users);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteFile() {
        try {
            Files.delete(PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
