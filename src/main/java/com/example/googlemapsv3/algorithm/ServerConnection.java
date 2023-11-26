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
import com.example.googlemapsv3.models.ClientNameAndPublicKey;
import com.example.googlemapsv3.models.Shipment;
import com.example.googlemapsv3.security.Cryptography;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ServerConnection {
    private static List<String> waypoints = new ArrayList<>();
    private static List<Shipment> shipments;
    private static String serverURL = "";

    public static List<Shipment> getShipments(){
        return shipments;
    }

    //Sends request to server, gets a List<Shipment> object as a response
    public static void getResponse(){
        try {
            waypoints = new ArrayList<>();

            HttpClient httpClient = HttpClient.newHttpClient();

            Gson gson = new Gson();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverURL + "/api/shipments/getAllSorted"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            //Extracting the object from response body
            response.thenApply(HttpResponse::body)
                    .thenAccept(json -> {
                        Type listType = new TypeToken<List<Shipment>>() {
                        }.getType();
                        shipments = gson.fromJson(json, listType);

                        waypoints.add(shipments.get(0).getOrigin());
                        for (Shipment sh : shipments) {
                            waypoints.add(sh.getDestination());
                        }
                    })
                    .join();

            MainController.getHelloController().txtOrigin.setText(waypoints.get(0));
            MainController.getHelloController().txtWaypoints.
                    setText(String.join(";", waypoints.subList(1, waypoints.size())));

            System.out.println("Got data from server!");
        }


        catch (Exception ex){
            if(ex.getMessage().startsWith("java.lang.NullPointerException") ||
            ex.getMessage().startsWith("com.google.gson.JsonSyntaxException")){
                MainController.displayAlert("There are no scheduled shipments!");
            }
            else {
                MainController.displayAlert("Failed to retrieve data from server!");
            }
        }
    }

    //Unused
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
