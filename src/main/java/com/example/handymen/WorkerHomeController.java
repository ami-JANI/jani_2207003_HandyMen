//package com.example.handymen;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//public class WorkerHomeController {
//
//    @FXML
//    private TextField nameField;
//
//    @FXML
//    private TextField phoneField;
//
//    @FXML
//    private TextField skillField;
//
//    @FXML
//    private TextField experienceField;
//
//    @FXML
//    private Button saveButton;
//
//    private String loggedInEmail;
//
//    // 🔹 Called by WorkerLoginController after successful login
//    public void setWorkerEmail(String email) {
//        this.loggedInEmail = email;
//        loadWorkerData();
//    }
//
//    // -----------------------------------------------------------------------
//    // LOAD WORKER DATA FROM DATABASE
//    // -----------------------------------------------------------------------
//    private void loadWorkerData() {
//        String query = "SELECT name, phone, skill, experience FROM workers WHERE email = ?";
//
//        try (Connection conn = DatabaseConnection.connect();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            stmt.setString(1, loggedInEmail);
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                nameField.setText(rs.getString("name"));
//                phoneField.setText(rs.getString("phone"));
//                skillField.setText(rs.getString("skill"));
//                experienceField.setText(rs.getString("experience"));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // -----------------------------------------------------------------------
//    // SAVE CHANGES BUTTON
//    // -----------------------------------------------------------------------
//    @FXML
//    private void onSaveChangesClick() {
//
//        String newName = nameField.getText();
//        String newPhone = phoneField.getText();
//        String newSkill = skillField.getText();
//        String newExperience = experienceField.getText();
//
//        String updateQuery = "UPDATE workers SET name=?, phone=?, skill=?, experience=? WHERE email=?";
//
//        try (Connection conn = DatabaseConnection.connect();
//             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
//
//            stmt.setString(1, newName);
//            stmt.setString(2, newPhone);
//            stmt.setString(3, newSkill);
//            stmt.setString(4, newExperience);
//            stmt.setString(5, loggedInEmail);
//
//            int rows = stmt.executeUpdate();
//
//            if (rows > 0) {
//                showAlert("Success", "Your data has been successfully updated!");
//            } else {
//                showAlert("Error", "Failed to update data.");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            showAlert("Error", "Something went wrong.");
//        }
//    }
//
//    private void showAlert(String title, String msg) {
//        Alert a = new Alert(Alert.AlertType.INFORMATION);
//        a.setTitle(title);
//        a.setHeaderText(null);
//        a.setContentText(msg);
//        a.show();
//    }
//}
