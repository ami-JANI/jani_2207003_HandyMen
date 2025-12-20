package com.example.handymen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class WorkerSignupController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> professionField;
    @FXML private TextField experienceField;
    @FXML private TextField rateField;
    @FXML private TextField locationField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    public void onSignUpSubmit(ActionEvent event) {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String profession = professionField.getValue();
        String exp = experienceField.getText();
        String rate = rateField.getText();
        String location = locationField.getText();
        String pass = passwordField.getText();
        String confirmPass = confirmPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                profession == null || exp.isEmpty() || rate.isEmpty() ||
                location.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {

            showAlert("Error", "All fields must be filled.");
            return;
        }

        if (!pass.equals(confirmPass)) {
            showAlert("Error", "Passwords do not match.");
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {


            String check = "SELECT email FROM workers WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(check);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                showAlert("Error", "Email already registered.");
                return;
            }

            String insert = "INSERT INTO workers (name, email, phone, profession, experience, rate, location, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insert);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, profession);
            ps.setString(5, exp);
            ps.setString(6, rate);
            ps.setString(7, location);
            ps.setString(8, pass);

            ps.executeUpdate();

            showAlert("Success", "Account created successfully! Please login.");

            Parent root = FXMLLoader.load(getClass().getResource("Worker_login.fxml"));
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 960));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to create account.");
        }
    }

    @FXML
    public void onBackToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Worker_login.fxml"));
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1280, 960));
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.show();
    }
}
