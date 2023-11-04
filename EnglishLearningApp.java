package com.example.dictionary_btl;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EnglishLearningApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("English Learning App");

        DictionaryDataLoader dataLoader = new DictionaryDataLoader();
        DictionaryController dictionaryController = new DictionaryController(dataLoader);

        ListView<String> vocabularyListView = new ListView<>(dictionaryController.getVocabularyList());
        vocabularyListView.setPrefWidth(200);
        TextArea explanationTextArea = new TextArea();
        explanationTextArea.setEditable(false);
        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        Button lookupButton = new Button("Lookup");
        Button quizButton = new Button("Vocabulary Quiz");

        vocabularyListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Lấy giải thích từ dictonaryController dựa trn từ mới được chọn
                String explanation = dictionaryController.lookupWord(newValue);
                // Hiển thị giải thích trong explanationTextArea
                explanationTextArea.setText(explanation);
            } else {
                //Xóa
                explanationTextArea.clear();
            }
        });

        addButton.setOnAction(event -> {
            String newWord = showInputDialog("Add Word", "Enter a new word:");
            if (newWord != null && !newWord.isEmpty()) {
                String explanation = showInputDialog("Add Word", "Enter the explanation:");
                if (explanation != null && !explanation.isEmpty()) {
                    dictionaryController.addWord(newWord, explanation);
                }
            }
        });

        editButton.setOnAction(event -> {
            String selectedWord = vocabularyListView.getSelectionModel().getSelectedItem();
            if (selectedWord != null) {
                String newExplanation = showInputDialog("Edit Word", "Enter the new explanation:");
                if (newExplanation != null && !newExplanation.isEmpty()) {
                    dictionaryController.editWord(selectedWord, newExplanation);
                }
            }
        });

        deleteButton.setOnAction(event -> {
            String selectedWord = vocabularyListView.getSelectionModel().getSelectedItem();
            if (selectedWord != null) {
                dictionaryController.deleteWord(selectedWord);
            }
        });

        lookupButton.setOnAction(event -> {
            String selectedWord = vocabularyListView.getSelectionModel().getSelectedItem();
            if (selectedWord != null) {
                String explanation = dictionaryController.lookupWord(selectedWord);
                explanationTextArea.setText(explanation);
            }
        });

        quizButton.setOnAction(event -> {
            VocabularyQuiz vocabularyQuiz = new VocabularyQuiz(dictionaryController);
            vocabularyQuiz.start();
        });

        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Search");
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            dictionaryController.filterVocabularyList(newValue);
            if (dictionaryController.getFilteredVocabularyList().isEmpty()) {
                showAlert("Không tìm thấy từ trong từ điển");
            }
        });

        ListView<String> filteredVocabularyListView = new ListView<>(dictionaryController.getFilteredVocabularyList());
        filteredVocabularyListView.setOnMouseClicked(event -> {
            String selectedWord = filteredVocabularyListView.getSelectionModel().getSelectedItem();
            if (selectedWord != null) {
                String explanation = dictionaryController.lookupWord(selectedWord);
                explanationTextArea.setText(explanation);
            } else {
                explanationTextArea.clear();
            }
        });

        HBox searchBox = new HBox(10);
        searchBox.getChildren().addAll(searchTextField);

        VBox vocabularyPane = new VBox(10);
        vocabularyPane.setPadding(new javafx.geometry.Insets(10));
        vocabularyPane.getChildren().addAll(searchBox, filteredVocabularyListView);

        VBox explanationPane = new VBox(10);
        explanationPane.setPadding(new javafx.geometry.Insets(10));
        explanationPane.getChildren().addAll(new Label("Explanation:"), explanationTextArea);

        HBox controlPane = new HBox(10);
        controlPane.setPadding(new javafx.geometry.Insets(10));
        controlPane.getChildren().addAll(addButton, editButton, deleteButton, lookupButton);

        BorderPane root = new BorderPane();
        root.setLeft(vocabularyPane);
        root.setCenter(explanationPane);
        root.setBottom(controlPane);
        root.setTop(quizButton);

        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String showInputDialog(String title, String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        return dialog.showAndWait().orElse(null);
    }
}