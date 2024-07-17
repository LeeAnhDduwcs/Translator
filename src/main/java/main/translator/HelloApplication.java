package main.translator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.sql.Connection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class HelloApplication extends Application {
    public static Connection connection;
    public static Set<String> wordSet = new HashSet<>();
    public static final String SAVEPATH = "D:\\Program Files\\Intellij\\My Program\\Translator\\src\\main\\java\\main\\translator\\WordSavingList";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ControlTranslate.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Translator!");
        stage.setScene(scene);
        stage.setX(800);
        stage.setY(50);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionarydatabase", "root", "0ysq3_-asw6rd");
        setup();
        launch();
        save();
    }

    private static void setup() {
        try {
            Scanner scanner = new Scanner(new File(SAVEPATH));
            while (scanner.hasNext()) {
                wordSet.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void save() {
        try {
            FileWriter fileWriter = new FileWriter(new File(SAVEPATH));
            for (String s : wordSet) {
                fileWriter.write(s + "\n");
            }
            fileWriter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}