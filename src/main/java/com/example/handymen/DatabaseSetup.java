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
                "profession TEXT, " +
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
    public static void createUserTable() {
        String query = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "email TEXT, " +
                "phone TEXT, " +
                "password TEXT" +
                "address TEXT" +
                ");";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(query);
            System.out.println("User table ensured.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
