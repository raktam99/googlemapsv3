module com.example.googlemapsv3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires google.maps.services;
    requires java.net.http;
    requires com.google.gson;
    requires javafx.web;
    requires jdk.jsobject;


    opens com.example.googlemapsv3 to javafx.fxml;
    exports com.example.googlemapsv3;
    exports com.example.googlemapsv3.models;
    opens com.example.googlemapsv3.models to com.google.gson, javafx.fxml;
    exports com.example.googlemapsv3.controller;
    opens com.example.googlemapsv3.controller to javafx.fxml;
    exports com.example.googlemapsv3.algorithm;
    opens com.example.googlemapsv3.algorithm to javafx.fxml;
}