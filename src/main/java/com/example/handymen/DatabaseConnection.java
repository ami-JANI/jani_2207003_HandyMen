package com.example.handymen;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:handymen.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Database connected.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
