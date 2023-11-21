package com.example.googlemapsv3;

import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;

public class GoogleMapsDisplay {
    private static String googleApiKey = "AIzaSyCEhRZDgXI2xYen-eEvRzOj36Kp8r9HO4o";
    private static String googleMapsURL = "https://www.google.com/maps/embed/v1/place?key=" +
            googleApiKey + "&q=YOUR_LOCATION";
    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();
}
