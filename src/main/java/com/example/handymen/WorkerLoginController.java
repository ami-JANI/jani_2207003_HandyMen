package com.example.handymen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class WorkerLoginController {

    public static String loggedWorkerEmail = null;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;


    @FXML
    public void onLoginClick(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Email or Password cannot be empty.");
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {

            String sql = "SELECT * FROM workers WHERE email = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                loggedWorkerEmail = email;

                showAlert("Success", "Login Successful!");

                Parent root = FXMLLoader.load(getClass().getResource("WorkerDashboard.fxml"));
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root, 1280, 960));
                stage.show();

            } else {
                showAlert("Error", "Incorrect Email or Password.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Database error!");
        }
    }


    @FXML
    public void onSignUpClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Worker_Signup.fxml"));
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

