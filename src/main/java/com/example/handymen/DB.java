package com.example.handymen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private static final String URL = "jdbc:sqlite:handyman.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            return null;
        }
    }
}
