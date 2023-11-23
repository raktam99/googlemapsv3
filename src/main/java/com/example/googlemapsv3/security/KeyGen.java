package com.example.googlemapsv3.security;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class KeyGen {

    public static void generateRSA() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);

        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey prKey = pair.getPrivate();
        PublicKey puKey = pair.getPublic();


        // Encode for printing out

        String encodedPublicKey = Base64.getEncoder().encodeToString(puKey.getEncoded());
        String encodedPrivateKey = Base64.getEncoder().encodeToString(prKey.getEncoded());

        Cryptography.setPrivateKeyRSA(encodedPrivateKey);
        Cryptography.setPublicKeyRSA(encodedPublicKey);

        System.out.println("Public key: " + encodedPublicKey);
        System.out.println("Private key: " + encodedPrivateKey);
    }

    public static void generateAES() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);

        SecretKey secretKey = keyGen.generateKey();
        Cryptography.setAESKey(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println(encodedKey);
    }

    public static void generateAES(int i) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);

        SecretKey secretKey = keyGen.generateKey();

        Cryptography.setMasterAESKey(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println(encodedKey);
    }
}
