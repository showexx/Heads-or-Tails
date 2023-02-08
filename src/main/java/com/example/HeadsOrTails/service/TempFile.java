package com.example.HeadsOrTails.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TempFile {
    final String PATH = "c://var//temp.txt";

    public void saveResult(String result) {
        try {
            saveText(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveDate() throws IOException {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        FileWriter writer = new FileWriter(PATH);
        BufferedWriter bufferWriter = new BufferedWriter(writer);
        bufferWriter.write(formatter.format(date));
        bufferWriter.close();
    }

    public void saveText(String text) throws IOException {
        FileWriter writerWithAppend = new FileWriter(PATH, true);
        BufferedWriter bufferWriter = new BufferedWriter(writerWithAppend);
        bufferWriter.write("/" + text);
        bufferWriter.close();
    }
}
