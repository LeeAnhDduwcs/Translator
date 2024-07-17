package main.translator;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControlTranslate implements Initializable {
    public static String[] searchWord = new String[3];
    @FXML
    private TextField input;

    @FXML
    private WebView output;

    @FXML
    private ListView<String> savingList;

    @FXML
    private TextField inputVN;

    @FXML
    private TextArea meaning;


    @FXML
    void searchMeaning(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!inputVN.getText().isEmpty()) {
                listVN.getItems().clear();
                listVN.getItems().addAll(Database.searchVN(inputVN.getText()));
                if (!listVN.getItems().isEmpty()) {
                    searchWord = Database.html(listVN.getItems().get(0), false);
                    if (searchWord != null) {
                        input.setText(searchWord[0]);
                        output.getEngine().loadContent(searchWord[1]);
                        meaning.setText(searchWord[2]);
                    }
                }
            }
        }
    }

    @FXML
    void savingAdd(Event event) {
        if (searchWord != null && !HelloApplication.wordSet.contains(searchWord[0])) {
            savingList.getItems().add(searchWord[0]);
            Collections.sort(savingList.getItems());
            HelloApplication.wordSet.add(searchWord[0]);
        }
    }

    @FXML
    void search(KeyEvent event) {
        if (!input.getText().isEmpty()) {
            listEnglish.getItems().clear();
            listEnglish.getItems().addAll(Database.searchEnglish(input.getText()));
            if (!listEnglish.getItems().isEmpty()) {
                searchWord = Database.html(listEnglish.getItems().get(0), true);
                if (searchWord != null) {
                    output.getEngine().loadContent(searchWord[1]);
                    meaning.setText(searchWord[2]);
                }
            }
        }
    }

    @FXML
    void speakEnglish(ActionEvent event) {
        Thread speak = new Thread(new Runnable() {
            @Override
            public void run() {
                TextToSpeech.speak(input.getText(), "en");
            }
        });
        speak.start();
    }

    public static Boolean alertConfirm(String object) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Do you want to change '" + object + "'?", ButtonType.YES, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get().getText().equals("Yes");
    }

    @FXML
    void delete(Event event) {
        if (alertConfirm(input.getText())) {
            try {
                Database.removeDB(input.getText());
            } catch (SQLException e) {
                meaning.setText("");
                output.getEngine().loadContent("<p>The word '" + input.getText() + "' doesn't exist.");
            }
            output.getEngine().loadContent("<p>Delete word successfully!</p>");
        }
    }

    @FXML
    void openEditAddPane(Event event) throws IOException {
        FXMLLoader root = new FXMLLoader(getClass().getResource("ControlEdit.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root.load()));
        stage.show();
    }

    @FXML
    void showSave(Event event) {
        String save = savingList.getSelectionModel().getSelectedItem();
        if (save != null) {
            searchWord = Database.html(save, true);
            if (searchWord != null) {
                output.getEngine().loadContent(searchWord[1]);
                meaning.setText(searchWord[2]);
            }
        }
    }

    @FXML
    void savingDelete(Event event) {
        savingList.getItems().remove(savingList.getSelectionModel().getSelectedItem());
        HelloApplication.wordSet.remove(savingList.getSelectionModel().getSelectedItem());
    }
    @FXML private VBox searchVNBox;
    @FXML private ListView<String> listEnglish;
    @FXML private ListView<String> listVN;

    @FXML
    void showEnglish() {
        String en = listEnglish.getSelectionModel().getSelectedItem();
        if (en != null) {
            searchWord = Database.html(en, true);
            if (searchWord != null) {
                output.getEngine().loadContent(searchWord[1]);
                meaning.setText(searchWord[2]);
            }
        }
    }

    @FXML
    void showVN() {
        String vi = listVN.getSelectionModel().getSelectedItem();
        if (vi != null) {
            searchWord = Database.html(vi, false);
            if (searchWord != null) {
                input.setText(searchWord[0]);
                output.getEngine().loadContent(searchWord[1]);
                meaning.setText(searchWord[2]);
            }
        }
    }

    @FXML
    void closeListVN() {
        searchVNBox.setDisable(true);
        searchVNBox.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        savingList.getItems().addAll(HelloApplication.wordSet);
        input.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (input.isFocused()) {
                listEnglish.setDisable(false);
                listEnglish.setVisible(true);
            } else {
                listEnglish.setDisable(true);
                listEnglish.setVisible(false);
            }
        });
        inputVN.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (inputVN.isFocused()) {
                searchVNBox.setDisable(false);
                searchVNBox.setVisible(true);
            }
        });
    }
}
