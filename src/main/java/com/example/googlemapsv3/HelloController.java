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

import java.util.List;

public class HelloController {
    @FXML
    public ListView<String> lstVvResponse;
    private static HelloController helloController;

    public void calculateOptimalRoute(ActionEvent actionEvent) {
        Logic.getResponse();
    }

    @FXML
    void initialize(){
        helloController = this;
    }

    public static HelloController getHelloController(){
        return helloController;
    }
}