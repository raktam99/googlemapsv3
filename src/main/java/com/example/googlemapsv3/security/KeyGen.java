package com.example.googlemapsv3.security;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;

public class KeyGen {

    //Generates RSA keypair
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
    }

    //Generates AES key
    public static void generateAES() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);

        SecretKey secretKey = keyGen.generateKey();
        Cryptography.setAESKey(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    //Generates master AES key
    public static void generateAES(int i) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);

        SecretKey secretKey = keyGen.generateKey();

        Cryptography.setMasterAESKey(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static void keyGeneration(){
        try {
            if (Files.exists(Paths.get("this_is_definitely_not_the_first_part_of_the_master_key.dat")) &&
                    Files.exists(Paths.get("this_is_definitely_not_the_second_part_of_the_master_key.dat")) &&
                    Files.exists(Paths.get("these_are_not_the_keys_you_are_looking_for.dat"))){
                KeyStorage.getMasterKeyFromFiles();
                KeyStorage.getKeys();
                System.out.println("Got keys from file!");
            }
            else {
                KeyGen.generateRSA();
                KeyGen.generateAES();
                KeyGen.generateAES(0);
                KeyStorage.setKeys();
                System.out.println("Generated keys!");

                //Logic.sendPublicKey();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
