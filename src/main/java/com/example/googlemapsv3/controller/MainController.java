package com.example.googlemapsv3.controller;

import com.example.googlemapsv3.algorithm.Logic;
import com.example.googlemapsv3.security.Cryptography;
import com.example.googlemapsv3.security.KeyGen;
import com.example.googlemapsv3.security.KeyStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class MainController {
    private static MainController mainController;
    @FXML
    public TextField txtOrigin;
    @FXML
    public TextField txtWaypoints;
    @FXML
    private StackPane stckPnMaps = new StackPane();

    public void calculateOptimalRoute(ActionEvent actionEvent) {
        Logic.getResponse();
    }
    @FXML
    void initialize(){
        stckPnMaps.getChildren().add(GoogleMapsDisplay.getWebView());
        GoogleMapsDisplay.showMap();
        mainController = this;
    }

    public static MainController getHelloController(){
        return mainController;
    }

    public static void displayAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Set the title of the alert
        alert.setTitle("Error");

        // Set the header text (optional)
        alert.setHeaderText(null);

        // Set the content text of the alert
        alert.setContentText(message);

        // Show the alert
        alert.showAndWait();
    }

    public static void keyGeneration(){
        try {
            if (Files.exists(Paths.get("this_is_definitely_not_the_first_part_of_the_master_key.dat")) &&
                    Files.exists(Paths.get("this_is_definitely_not_the_second_part_of_the_master_key.dat")) &&
                    Files.exists(Paths.get("these_are_not_the_keys_you_are_looking_for.dat"))){
                KeyStorage.getMasterKeyFromFiles();
                KeyStorage.getKeys();
                Cryptography.printKeys();
                //System.out.println(LocalDateTime.now() + ": Master key is forged.");
            }
            else {
                KeyGen.generateRSA();
                KeyGen.generateAES();
                KeyGen.generateAES(0);
                KeyStorage.setKeys();

                //System.out.println(LocalDateTime.now() + ": Master key does not exist yet.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}