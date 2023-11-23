package com.example.googlemapsv3.security;

import java.io.*;
import java.util.List;

public class KeyStorage {
    public static void getKeys() throws Exception {
        String decryptedKey;
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader("these_are_not_the_keys_you_are_looking_for.dat"))){
            String line;
            while((line = bufferedReader.readLine()) != null) {
                if(line.startsWith("privateKey=")) {
                    line = line.substring(11);
                    String[] lineSplit = line.split(";");
                    byte[] byteArray = new byte[lineSplit.length];

                    for (int i = 0; i < lineSplit.length; ++i) {
                        byteArray[i] = Byte.parseByte(lineSplit[i]);
                    }

                    decryptedKey = Cryptography.decryptKeyFromByteArrayToRest(byteArray);
                    Cryptography.setPrivateKeyRSA(decryptedKey);
                } else if(line.startsWith("publicKey=")) {

                    line = line.substring(10);
                    String[] lineSplit = line.split(";");
                    byte[] byteArray = new byte[lineSplit.length];

                    for (int i = 0; i < lineSplit.length; ++i) {
                        byteArray[i] = Byte.parseByte(lineSplit[i]);
                    }

                    decryptedKey = Cryptography.decryptKeyFromByteArrayToRest(byteArray);
                    Cryptography.setPublicKeyRSA(decryptedKey);
                } else if(line.startsWith("aesKey=")) {

                    line = line.substring(7);
                    String[] lineSplit = line.split(";");
                    byte[] byteArray = new byte[lineSplit.length];

                    for (int i = 0; i < lineSplit.length; ++i) {
                        byteArray[i] = Byte.parseByte(lineSplit[i]);
                    }

                    decryptedKey = Cryptography.decryptKeyFromByteArrayToRest(byteArray);
                    Cryptography.setAESKey(decryptedKey);
                }
            }
        }
    }

    public static void getMasterKeyFromFiles() throws Exception {

        byte[] firstPart;
        byte[] secondPart;

        try(FileInputStream inputStream = new FileInputStream("this_is_definitely_not_the_first_part_of_the_master_key.dat")){
            firstPart = inputStream.readAllBytes();
        }

        try(FileInputStream inputStream = new FileInputStream("this_is_definitely_not_the_second_part_of_the_master_key.dat")){
            secondPart = inputStream.readAllBytes();
        }

        Cryptography.forgeSplitKey(firstPart, secondPart);
    }

    public static void setKeys() throws Exception {
        byte[] encryptedPrivateKey = Cryptography.encryptKeysAsByteArrayToRest(Cryptography.getPrivateKeyRSA());
        byte[] encryptedPublicKey = Cryptography.encryptKeysAsByteArrayToRest(Cryptography.getPublicKeyRSA());
        byte[] encryptedAESKey = Cryptography.encryptKeysAsByteArrayToRest(Cryptography.getAESKey());
        List<byte[]> splitMasterKey = Cryptography.splitAESKeyForStoringAsByteArrays();

        StringBuilder stringBuilder;


        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("these_are_not_the_keys_you_are_looking_for.dat"))) {
            // writing private rsa key to file
            stringBuilder = new StringBuilder();
            stringBuilder.append("privateKey=");
            for (byte b: encryptedPrivateKey) {
                stringBuilder.append(b + ";");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            bufferedWriter.write((stringBuilder.toString()));
            bufferedWriter.newLine();

            // writing own public rsa key to file
            stringBuilder = new StringBuilder();
            stringBuilder.append("publicKey=");
            for (byte b: encryptedPublicKey) {
                stringBuilder.append(b + ";");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            bufferedWriter.write((stringBuilder.toString()));
            bufferedWriter.newLine();

            // writing aes key to file
            stringBuilder = new StringBuilder();
            stringBuilder.append("aesKey=");
            for (byte b: encryptedAESKey) {
                stringBuilder.append(b + ";");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            bufferedWriter.write((stringBuilder.toString()));
            bufferedWriter.newLine();
        }

        try(FileOutputStream outputStream = new FileOutputStream("this_is_definitely_not_the_first_part_of_the_master_key.dat")){
            outputStream.write(splitMasterKey.get(0));
        }

        try(FileOutputStream outputStream = new FileOutputStream("this_is_definitely_not_the_second_part_of_the_master_key.dat")){
            outputStream.write(splitMasterKey.get(1));
        }
    }
}
