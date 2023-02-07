package com.example.HeadsOrTails.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {
    private ReplyKeyboardMarkup createKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Победа!");
        row.add("Поражение!");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup getCreateKeyboard(){
        return createKeyboard();
    }
}
