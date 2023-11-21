package com.example.googlemapsv3.controller;

import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;

public class GoogleMapsDisplay {
    private static String htmlContent = getHTML();
    private static WebView webView = new WebView();
    private static WebEngine webEngine = webView.getEngine();

    public static void showMap(){
        webEngine.load(htmlContent);
    }

    private static String getHTML(){
        return GoogleMapsDisplay.class.getResource("/com/example/googlemapsv3/GoogleMapsHTML.html").toExternalForm();
    }

    public static WebView getWebView(){
        return webView;
    }
}
