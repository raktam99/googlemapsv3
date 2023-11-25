package com.example.googlemapsv3.algorithm;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.googlemapsv3.controller.MainController;
import com.example.googlemapsv3.models.ClientName;
import com.example.googlemapsv3.models.ClientNameAndPublicKey;
import com.example.googlemapsv3.models.Shipment;
import com.example.googlemapsv3.security.Cryptography;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Logic {
    private static List<String> waypoints = new ArrayList<>();

    public static List<Shipment> shipments;
    public static void getResponse(){
        try {
            waypoints = new ArrayList<>();

            shipments = new ArrayList<>();

            HttpClient httpClient = HttpClient.newHttpClient();

            Gson gson = new Gson();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/shipments/getAllSorted"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            response.thenApply(HttpResponse::body)
                    .thenAccept(json -> {
                        Type listType = new TypeToken<List<Shipment>>() {
                        }.getType();
                        shipments = gson.fromJson(json, listType);

                        if (shipments == null || shipments.isEmpty()){ return;}

                        System.out.println("From: " + shipments.get(0).getOrigin() + "\nTo:");
                        waypoints.add(shipments.get(0).getOrigin());
                        for (Shipment sh : shipments) {
                            System.out.println(sh.getDestination() + " " + sh.getShipmentId());
                            waypoints.add(sh.getDestination());
                        }
                    })
                    .join();

            if (waypoints == null || waypoints.isEmpty()) {
                MainController.displayAlert("There are no scheduled shipments!");
                return;
            }

            MainController.getHelloController().txtOrigin.setText(waypoints.get(0));
            MainController.getHelloController().txtWaypoints.
                    setText(String.join(";", waypoints.subList(1, waypoints.size())));
        }
        catch (Exception ex){
            MainController.displayAlert("Failed to retrieve data from server!");
        }
    }

    public static void sendPublicKey() throws UnknownHostException {
        HttpClient httpClient = HttpClient.newHttpClient();
        ClientNameAndPublicKey client = new ClientNameAndPublicKey();

        client.setClientName(InetAddress.getLocalHost().getHostName());
        client.setPublicKey(Cryptography.getPublicKeyRSA());

        Gson gson = new Gson();
        String json = gson.toJson(client);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(""))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        response.thenApply(HttpResponse::body).thenAccept(System.out::println).join();
    }
}
