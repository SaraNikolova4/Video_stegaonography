package com.example.proba6.config;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;


public class Encrypt {

    private static final Random random = new SecureRandom();
    private static final String karakteri = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int vrtenja = 10000;
    private static final int dolzina = 256;


    public static String randomstring(int lenght) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lenght; i++) {
            char c = karakteri.charAt(random.nextInt(karakteri.length()));
            sb.append(c);
        }
        return new String(sb);
    }

    public static byte[] hesh(char[] password, byte[] random) {
        PBEKeySpec spec = new PBEKeySpec(password, random, vrtenja, dolzina);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        //mora li
        finally {
            spec.clearPassword();
        }
    }

    public static String generatespassword(String password, String random) {
        String end = null;
        byte[] secure = hesh(password.toCharArray(), random.getBytes());
        return Base64.getEncoder().encodeToString(secure);
    }
}