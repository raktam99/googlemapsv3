package com.example.googlemapsv3;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.googlemapsv3.models.Shipment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Logic {
    private static String serverApiKey = "0";
    public static void getResponse(){
        HttpClient httpClient = HttpClient.newHttpClient();

        Gson gson = new Gson();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/shipments/0/getAllSorted"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        response.thenApply(HttpResponse::body)
                .thenAccept( json -> {
                    Type listType = new TypeToken<List<Shipment>>(){}.getType();
                    List<Shipment> shipments = gson.fromJson(json, listType);
                    System.out.println("From: " + shipments.get(0).getOrigin() + "\nTo:");
                    for (Shipment sh: shipments) {
                        System.out.println(sh.getDestination() + " " + sh.getShipmentId());
                    }
                })
                .join();
    }
}
