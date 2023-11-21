package com.example.googlemapsv3.controller;

import com.example.googlemapsv3.algorithm.Logic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class HelloController {
    private static HelloController helloController;
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
        helloController = this;
    }

    public static HelloController getHelloController(){
        return helloController;
    }
}