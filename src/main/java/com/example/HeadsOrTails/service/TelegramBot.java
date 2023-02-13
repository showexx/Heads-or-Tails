package com.example.HeadsOrTails.service;

import com.example.HeadsOrTails.config.BotConfig;

import com.example.HeadsOrTails.models.Users;
import com.example.HeadsOrTails.repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
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
        } else if (update.hasCallbackQuery()) {
            checkCallback(update);
        }
    }

    private void checkCallback(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        if (lengthString() >= 21) {
            if (callbackData.equals("Победа!")) {
                tempFile.saveResult("Победа");
                save(chatId);
                editMessage((int) messageId, chatId, "Победа!");
            } else if (callbackData.equals("Поражение!")) {
                tempFile.saveResult("Поражение");
                save(chatId);
                editMessage((int) messageId, chatId, "Поражение!");
            }
        }else{
            sendMessage(chatId, "Укажите сначала сумму!");
        }
    }

    private int lengthString() {
        String content = null;
        try {
            content = Files.lines(PATH).reduce("", String::concat);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content.length();
    }

    private void editMessage(int messageId, long chatId, String text) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId(messageId);
        executeEditMessage(message);
    }

    private void executeEditMessage(EditMessageText message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Error");
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
            Files.delete(PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendKeyboard(long chatId, String textToSend, InlineKeyboardMarkup keyboard) {
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
