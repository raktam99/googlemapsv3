package com.example.googlemapsv3;

import com.example.googlemapsv3.models.Shipment;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.util.List;

public class HelloController {
    private static HelloController helloController;
    @FXML
    private StackPane stckPnMaps = new StackPane();

    public void calculateOptimalRoute(ActionEvent actionEvent) {
        Logic.getResponse();
    }
    @FXML
    void initialize(){
        stckPnMaps.getChildren().add(GoogleMapsDisplay.webView);
        GoogleMapsDisplay.showMap();
        helloController = this;
    }
}