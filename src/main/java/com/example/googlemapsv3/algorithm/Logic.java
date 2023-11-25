package com.example.googlemapsv3.algorithm;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.googlemapsv3.controller.MainController;
import com.example.googlemapsv3.models.ClientName;
import com.example.googlemapsv3.models.ClientNameAndPublicKey;
import com.example.googlemapsv3.models.Shipment;
import com.example.googlemapsv3.security.Cryptography;
import com.example.googlemapsv3.security.KeyGen;
import com.example.googlemapsv3.security.KeyStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.util.SerializationUtils;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;

public class Logic {
    private static List<String> waypoints = new ArrayList<>();

    public static List<Shipment> shipments;
    public static void getResponse(){
        try {
            ClientName me = new ClientName();

            me.setClientName(InetAddress.getLocalHost().getHostName());

            waypoints = new ArrayList<>();

            shipments = new ArrayList<>();

            HttpClient httpClient = HttpClient.newHttpClient();

            Gson gson = new Gson();
            String jsonSend = gson.toJson(me);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/shipments/getAll"))
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

            List<byte[]> encrypted = encryptShipments(shipments);
            List<Shipment> decrypted = decryptShipments(encrypted);
            System.out.println("asd");
        }
        catch (Exception ex){
            MainController.displayAlert("Failed to connect to server!");
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

    public static void keyGeneration(){
        try {
            if (Files.exists(Paths.get("this_is_definitely_not_the_first_part_of_the_master_key.dat")) &&
                    Files.exists(Paths.get("this_is_definitely_not_the_second_part_of_the_master_key.dat")) &&
                    Files.exists(Paths.get("these_are_not_the_keys_you_are_looking_for.dat"))){
                KeyStorage.getMasterKeyFromFiles();
                KeyStorage.getKeys();
                Cryptography.printKeys();
                //System.out.println(LocalDateTime.now() + ": Master key is forged.");
            }
            else {
                KeyGen.generateRSA();
                KeyGen.generateAES();
                KeyGen.generateAES(0);
                KeyStorage.setKeys();

                //Logic.sendPublicKey();

                //System.out.println(LocalDateTime.now() + ": Master key does not exist yet.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<byte[]> encryptShipments(List<Shipment> shipments) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(Cryptography.getPublicKeyRSA());

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        List<byte[]> list = new ArrayList<>();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        for (Shipment sh: shipments) {
            list.add(SerializationUtils.serialize(sh));
        }

        for (int i = 0; i < list.size(); ++i){
            byte[] c = cipher.doFinal(list.get(i));
            list.set(i, c);
        }

        System.out.println("Encryption successful!");

        return list;
    }

    public static List<Shipment> decryptShipments(List<byte[]> data) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(Cryptography.getPrivateKeyRSA());

        List<Shipment> list = new ArrayList<>();

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        for (int i = 0; i < data.size(); ++i){
            byte[] c = cipher.doFinal(data.get(i));
            data.set(i, c);
        }

        for (byte[] b:data) {
            list.add((Shipment) SerializationUtils.deserialize(b));
        }

        System.out.println("Decryption successful!");

        return list;
    }
}
