package com.example.handymen;

public class UserSession {
    private static String userLocation;

    public static void setUserLocation(String location) {
        userLocation = location;
    }

    public static String getUserLocation() {
        return userLocation;
    }
}
