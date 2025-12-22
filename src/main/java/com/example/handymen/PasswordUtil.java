package com.example.handymen;

import java.security.MessageDigest;

public class PasswordUtil {

    // Hash password using SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Verify entered password with stored hash
    public static boolean verifyPassword(String enteredPassword, String storedHash) {
        String enteredHash = hashPassword(enteredPassword);
        return enteredHash.equals(storedHash);
    }
}
