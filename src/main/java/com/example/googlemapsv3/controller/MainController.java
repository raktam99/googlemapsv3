package com.example.googlemapsv3.controller;

import com.example.googlemapsv3.algorithm.Logic;
import com.example.googlemapsv3.models.Shipment;
import com.example.googlemapsv3.security.Cryptography;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;


import java.util.List;

public class MainController {
    private static MainController mainController;
    @FXML
    public TextField txtOrigin;
    @FXML
    public TextField txtWaypoints;
    @FXML
    private StackPane stckPnMaps = new StackPane();

    public void sendRequest(ActionEvent actionEvent) throws Exception {
        txtWaypoints.setText("Waypoints");
        txtOrigin.setText("Origin");
        Logic.getResponse();
        List<byte[]> encrypted = Cryptography.encryptShipments(Logic.shipments);
        List<Shipment> decrypted = Cryptography.decryptShipments(encrypted);
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
}