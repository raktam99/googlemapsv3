package com.example.googlemapsv3.controller;

import com.example.googlemapsv3.algorithm.ServerConnection;
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
    public StackPane stckPnMaps = new StackPane();

    public void sendRequest(ActionEvent actionEvent) throws Exception {
        txtWaypoints.setText("Waypoints");
        txtOrigin.setText("Origin");
        ServerConnection.getResponse();

        //Encryption and decryption of retrieved data
        if(ServerConnection.getShipments() != null){
            List<byte[]> encrypted = Cryptography.encryptShipments(ServerConnection.getShipments());
            List<Shipment> decrypted = Cryptography.decryptShipments(encrypted);
        }
    }
    @FXML
    void initialize(){
        mainController = this;
    }

    //Opens the HTML file on the stackpanel
    public void showMap(){
        stckPnMaps.getChildren().add(GoogleMapsDisplay.getWebView());
        GoogleMapsDisplay.loadMap();
    }

    public static MainController getHelloController(){
        return mainController;
    }

    public static void displayAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}