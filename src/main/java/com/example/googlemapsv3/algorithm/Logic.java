package com.example.googlemapsv3.algorithm;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.googlemapsv3.controller.MainController;
import com.example.googlemapsv3.models.Shipment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Logic {
    private static List<String> waypoints = new ArrayList<>();
    public static void getResponse(){
        try {
            waypoints = new ArrayList<>();

            HttpClient httpClient = HttpClient.newHttpClient();

            Gson gson = new Gson();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/shipments/0/getAllSorted"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            response.thenApply(HttpResponse::body)
                    .thenAccept(json -> {
                        Type listType = new TypeToken<List<Shipment>>() {
                        }.getType();
                        List<Shipment> shipments = gson.fromJson(json, listType);

                        if (shipments == null || shipments.isEmpty()) return;

                        System.out.println("From: " + shipments.get(0).getOrigin() + "\nTo:");
                        waypoints.add(shipments.get(0).getOrigin());
                        for (Shipment sh : shipments) {
                            System.out.println(sh.getDestination() + " " + sh.getShipmentId());
                            waypoints.add(sh.getDestination());
                        }
                    })
                    .join();

            if (waypoints == null || waypoints.isEmpty()) {
                MainController.getHelloController().txtOrigin.setText("No scheduled shipments!");
                MainController.getHelloController().txtWaypoints.setText("");
                return;
            }

            MainController.getHelloController().txtOrigin.setText(waypoints.get(0));
            MainController.getHelloController().txtWaypoints.
                    setText(String.join(";", waypoints.subList(1, waypoints.size())));
        }
        catch (Exception ex){
            MainController.getHelloController().txtOrigin.setText("Connection error!");
            MainController.getHelloController().txtWaypoints.setText("");
        }
    }
}
