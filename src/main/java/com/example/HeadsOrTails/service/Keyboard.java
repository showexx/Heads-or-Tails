package com.example.HeadsOrTails.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {

    private InlineKeyboardMarkup createKeyboard() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        rowInLine.add(createWinButton());
        rowInLine.add(createLoseButton());
        rowsInLine.add(rowInLine);
        keyboard.setKeyboard(rowsInLine);
        return keyboard;
    }

    private InlineKeyboardButton createWinButton() {
        InlineKeyboardButton winButton = new InlineKeyboardButton();
        winButton.setText("Победа!");
        winButton.setCallbackData("Победа!");
        return winButton;
    }

    private InlineKeyboardButton createLoseButton() {
        InlineKeyboardButton loseButton = new InlineKeyboardButton();
        loseButton.setText("Поражение!");
        loseButton.setCallbackData("Поражение!");
        return loseButton;
    }

    public InlineKeyboardMarkup getCreateKeyboard() {
        return createKeyboard();
    }
}
