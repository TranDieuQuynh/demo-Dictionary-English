package com.example.dictionary_btl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DictionaryDataLoader {

    private static final String FILE_PATH = "dict.txt";

    public List<String> loadDictionaryData() {
        List<String> dictionaryData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionaryData.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dictionaryData;
    }

    public void saveDictionaryData(List<String> dictionaryData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : dictionaryData) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}