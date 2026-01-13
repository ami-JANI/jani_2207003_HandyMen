package com.example.handymen;

import com.example.handymen.DatabaseConnection;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetup {

    public static void initializeDatabase() {
        createWorkerTable();
        createUserTable();
        createWorkerSlotsTable();
    }

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

        execute(query, "Worker table ensured.");
    }

    public static void createUserTable() {
        String query = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "email TEXT, " +
                "phone TEXT, " +
                "password TEXT, " +
                "address TEXT" +
                ");";

        execute(query, "User table ensured.");
    }

    public static void createWorkerSlotsTable() {
        String query = "CREATE TABLE IF NOT EXISTS worker_slots (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "worker_email TEXT, " +
                "work_date TEXT, " +
                "slot INTEGER, " +
                "status TEXT, " +
                "user_email TEXT" +
                ");";

        execute(query, "Worker slots table ensured.");
    }

    public static void createNotificationsTable() {
        String query = "CREATE TABLE IF NOT EXISTS notifications (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    worker_email TEXT,\n" +
                "    message TEXT,\n" +
                "    created_at TEXT\n" +
                ");\n";

        execute(query, "Notifications table ensured.");
    }


    private static void execute(String sql, String successMsg) {
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println(successMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
