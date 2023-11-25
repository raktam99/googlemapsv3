package com.example.googlemapsv3;

import com.example.googlemapsv3.security.KeyGen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 645);
        stage.setTitle("Map Application!");
        stage.setScene(scene);
        stage.show();
        KeyGen.keyGeneration();
    }

    public static void main(String[] args) {
        launch();
    }
}