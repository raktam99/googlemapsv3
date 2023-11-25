package com.example.googlemapsv3.security;

import com.example.googlemapsv3.models.Shipment;
import org.springframework.util.SerializationUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

public class Cryptography {

    /**
     * Key for encrypt and decrypt data in database
     */
    private static String keyForRestAES;

    /**
     * Key for encrypt and decrypt keys in files
     */
    private static String masterKeyAES;

    /**
     * Public RSA key for the data that is received
     * */
    private static String privateKeyRSA;

    /**
     * Public RSA key for the "in transit" data
     * */
    private static String publicKeyRSA;

    public static void setMasterAESKey(String key) {
        masterKeyAES = key;
    }
    public static String getMasterAESKey() {
        return masterKeyAES;
    }
    public static void setAESKey(String key) {
        keyForRestAES = key;
    }
    public static String getAESKey() {
        return keyForRestAES;
    }
    public static void setPrivateKeyRSA(String key){
        privateKeyRSA = key;
    }
    public static String getPrivateKeyRSA() {
        return privateKeyRSA;
    }
    public static void setPublicKeyRSA(String key){
        publicKeyRSA = key;
    }
    public static String getPublicKeyRSA() {
        return publicKeyRSA;
    }

    public static List<byte[]> encryptShipments(List<Shipment> shipments) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(Cryptography.getAESKey());

        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

        List<byte[]> list = new ArrayList<>();

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

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
        byte[] keyBytes = Base64.getDecoder().decode(Cryptography.getAESKey());

        List<Shipment> list = new ArrayList<>();

        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

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

    public static byte[] encryptKeysAsByteArrayToRest(String data) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(masterKeyAES);
        byte[] encryptedKey = Base64.getDecoder().decode(data);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(encryptedKey);
    }

    public static String decryptKeyFromByteArrayToRest(byte[] encryptedKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(masterKeyAES);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decryptedKey = cipher.doFinal(encryptedKey);

        return Base64.getEncoder().encodeToString(decryptedKey);
    }

    public static List<byte[]> splitAESKeyForStoringAsByteArrays() {
        byte[] aesInBytes = Base64.getDecoder().decode(masterKeyAES);
        String[] splitAES = new String[2];
        splitAES[0] = "";
        splitAES[1] = "";

        byte[] firstPart = new byte[16];
        byte[] secondPart = new byte[16];


        int j = 0, q = 0;

        for(int i = 0; i < aesInBytes.length; ++i) {
            if(i % 2 != 0) {
                firstPart[j] = aesInBytes[i];
                j++;
            } else {
                secondPart[q] = aesInBytes[i];
                q++;
            }
        }

        List<byte[]> splitKeyList = new ArrayList<>();
        splitKeyList.add(firstPart);
        splitKeyList.add(secondPart);

        return splitKeyList;
    }

    public static void forgeSplitKey(byte[] first, byte[] second) {

        // converting the byte[] arrays into Byte[] arrays, so they could be stored in a queue
        Byte[] firstPart = new Byte[first.length];
        Byte[] secondPart = new Byte[second.length];

        Arrays.setAll(firstPart, i -> first[i]);
        Arrays.setAll(secondPart, i -> second[i]);

        // creating queues, so I can use the data structure's FIFO method
        Queue<Byte> firstPartInQ = new LinkedList<>(Arrays.asList(firstPart));
        Queue<Byte> secondPartInQ = new LinkedList<>(Arrays.asList(secondPart));

        // byte[32], because the rsa key uses 258 bits, that is 32 bytes, so iterating through the key means iterating through 32 bytes.
        byte[] key = new byte[32];

        for(int i = 0; i < key.length; ++i) {

            if(i % 2 != 0){
                if(firstPartInQ.peek() != null){
                    key[i] = firstPartInQ.poll();
                }

            } else {
                if(secondPartInQ.peek() != null){
                    key[i] = secondPartInQ.poll();
                }
            }
        }

        // creating the String key from the key array
        masterKeyAES = Base64.getEncoder().encodeToString(key);
    }

    public static void printKeys(){
        System.out.println("AES key: " + keyForRestAES);
        System.out.println("Master Key: " + masterKeyAES);
        System.out.println("Public key: " + publicKeyRSA);
        System.out.println("Private key: " + privateKeyRSA);
    }

}
