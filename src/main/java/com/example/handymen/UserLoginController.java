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

public class UserLoginController {
    public static String loggedUserEmail = null;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:handymen.db");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    public void onLoginClick(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Email or Password cannot be empty.");
            return;
        }

        try (Connection conn = connect()) {

            if (conn == null) {
                showAlert("Error", "Database connection failed!");
                return;
            }

            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                loggedUserEmail = email;

                // SAVE SESSION
                Session.setUser(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("name")
                );

                Parent root = FXMLLoader.load(getClass().getResource("interface.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root, 1280, 960));
                stage.show();
            }

            else {
                showAlert("Error", "Incorrect Email or Password.");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert("Error", "Login failed due to a database error.");
        }
    }

    @FXML
    public void onSignUpClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("user_signup.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
