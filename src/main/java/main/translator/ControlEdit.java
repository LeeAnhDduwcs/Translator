package main.translator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControlEdit implements Initializable {

    @FXML
    private TextArea html;

    @FXML
    private WebView showHtml;

    @FXML
    private TextField word;

    @FXML private TextArea meaning;

    @FXML
    void add(ActionEvent event) {
        try {
            if (ControlTranslate.alertConfirm(word.getText())) {
                Database.insertDB(word.getText(), html.getText(), meaning.getText());
                showHtml.getEngine().loadContent("<p>Add the word '" + word.getText() + "' successfully!");
            }
        } catch (SQLException e) {
            showHtml.getEngine().loadContent("<p>The word '" + word.getText() + "' already exists.");
        }
    }

    @FXML
    void edit(ActionEvent event) {
        try {
            if (ControlTranslate.alertConfirm(word.getText())){
                Database.updateDB(word.getText(), html.getText(),meaning.getText());
                showHtml.getEngine().loadContent("<p>Edit the word '" + word.getText() + "' successfully!");
            }

        } catch (SQLException e) {
            showHtml.getEngine().loadContent("<p>The word '" + word.getText() + "' doesn't exists.");
        }
    }

    @FXML
    void showContent(KeyEvent event) {
        showHtml.getEngine().loadContent("<pre>" + meaning.getText() + "</pre>" + html.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        word.setText(ControlTranslate.searchWord[0]);
        html.setText(ControlTranslate.searchWord[1]);
        meaning.setText(ControlTranslate.searchWord[2]);
        showHtml.getEngine().loadContent(html.getText());

    }
}
