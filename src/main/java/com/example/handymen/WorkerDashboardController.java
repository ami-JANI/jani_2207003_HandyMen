package com.example.handymen;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static com.example.handymen.WorkerLoginController.loggedWorkerEmail;

public class WorkerDashboardController implements Initializable {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField professionField;
    @FXML private TextField experienceField;
    @FXML private TextField rateField;
    @FXML private TextField locationField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadWorkerData();
    }

    private void loadWorkerData() {
        String email = loggedWorkerEmail;

        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT * FROM workers WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("phone"));
                professionField.setText(rs.getString("profession"));
                experienceField.setText(rs.getString("experience"));
                rateField.setText(rs.getString("rate"));
                locationField.setText(rs.getString("location"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveChangesClick() {

        String newName = nameField.getText();
        String newPhone = phoneField.getText();
        String newProfession = professionField.getText();
        String newExperience = experienceField.getText();

        String updateQuery = "UPDATE workers SET name=?,phone=?,experience=? ,profession=? WHERE email=?";


        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setString(1, newName);
            stmt.setString(2, newPhone);
            stmt.setString(3, newProfession);
            stmt.setString(4, newExperience);
            stmt.setString(5, loggedWorkerEmail);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                showAlert("Success", "Your data has been successfully updated!");
            } else {
                showAlert("Error", "Failed to update data.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Something went wrong.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }
}
