package com.example.handymen;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetup {

    public static void createWorkerTable() {
        String query = "CREATE TABLE IF NOT EXISTS workers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "email TEXT, " +
                "phone TEXT, " +
                "category TEXT, " +
                "experience TEXT, " +
                "rate TEXT, " +
                "location TEXT, " +
                "password TEXT" +
                ");";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(query);
            System.out.println("Worker table ensured.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
