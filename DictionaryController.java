package com.example.dictionary_btl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;


public class DictionaryController {

    private List<String> vocabularyList;
    private List<String> filteredVocabularyList;
    private List<String> explanationList;

    private DictionaryDataLoader dataLoader;

    public DictionaryController(DictionaryDataLoader dataLoader) {
        this.dataLoader = dataLoader;
        loadDictionaryData();
    }

    private void loadDictionaryData() {
        List<String> dictionaryData = dataLoader.loadDictionaryData();
        vocabularyList = new ArrayList<>();
        explanationList = new ArrayList<>();

        for (String line : dictionaryData) {
            String[] parts = line.split(":");
            vocabularyList.add(parts[0]);
            explanationList.add(parts[1]);
        }

        filteredVocabularyList = new ArrayList<>(vocabularyList);
    }

    public ObservableList<String> getVocabularyList() {
        return FXCollections.observableArrayList(vocabularyList);
    }

    public ObservableList<String> getFilteredVocabularyList() {
        return FXCollections.observableArrayList(filteredVocabularyList);
    }

    public void filterVocabularyList(String filter) {
        filteredVocabularyList.clear(); // Xóa danh sách từ vựng đã lọc trước đó

        for (String word : vocabularyList) {
            if (word.toLowerCase().contains(filter.toLowerCase())) {
                filteredVocabularyList.add(word);
            }
        }
    }

    public String lookupWord(String word) {
        int index = vocabularyList.indexOf(word);
        if (index != -1) {
            return explanationList.get(index);
        }
        return null;
    }

    public void addWord(String word, String explanation) {
        vocabularyList.add(word);
        explanationList.add(explanation);
        dataLoader.saveDictionaryData(getDictionaryData());
    }

    public void editWord(String word, String newExplanation) {
        int index = vocabularyList.indexOf(word);
        if (index != -1) {
            explanationList.set(index, newExplanation);
            dataLoader.saveDictionaryData(getDictionaryData());
        }
    }

    public void deleteWord(String word) {
        int index = vocabularyList.indexOf(word);
        if (index != -1) {
            vocabularyList.remove(index);
            explanationList.remove(index);
            dataLoader.saveDictionaryData(getDictionaryData());
        }
    }

    private List<String> getDictionaryData() {
        List<String> dictionaryData = new ArrayList<>();
        for (int i = 0; i < vocabularyList.size(); i++) {
            String line = vocabularyList.get(i) + ":" + explanationList.get(i);
            dictionaryData.add(line);
        }
        return dictionaryData;
    }
    // Kết thúc chương trình
}
