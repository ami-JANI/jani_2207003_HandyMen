package com.example.handymen;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RequestedWorkController {

    @FXML
    private ListView<String> requestList;

    @FXML
    public void initialize() {
        loadRequested();
    }

    private void loadRequested() {

        try (Connection conn = DatabaseConnection.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    """
                    SELECT w.email, w.profession, s.work_date, s.slot
                    FROM worker_slots s
                    JOIN workers w ON s.worker_email = w.email
                    WHERE s.user_email=? AND s.status='requested'
                    """
            );
            ps.setString(1, UserLoginController.loggedUserEmail);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                String time = slotToTime(rs.getInt("slot"));

                requestList.getItems().add(
                        rs.getString("email") + " | " +
                                rs.getString("profession") + " | " +
                                rs.getString("work_date") + " | " +
                                time
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String slotToTime(int slot) {
        return switch (slot) {
            case 1 -> "9:00 - 10:00";
            case 2 -> "10:00 - 11:00";
            case 3 -> "11:00 - 12:00";
            case 4 -> "12:00 - 1:00";
            case 5 -> "1:00 - 2:00";
            case 6 -> "2:00 - 3:00";
            case 7 -> "3:00 - 4:00";
            case 8 -> "4:00 - 5:00";
            default -> "Unknown";
        };
    }
}
