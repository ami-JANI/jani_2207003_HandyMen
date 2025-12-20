package com.example.handymen;

public class Session {

    private static int userId;
    private static String userEmail;
    private static String userName;

    public static void setUser(int id, String email, String name) {
        userId = id;
        userEmail = email;
        userName = name;
    }

    public static int getUserId() {
        return userId;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static String getUserName() {
        return userName;
    }

    public static void clear() {
        userId = 0;
        userEmail = null;
        userName = null;
    }
}
