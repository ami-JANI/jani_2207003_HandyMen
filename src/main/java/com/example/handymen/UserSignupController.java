package com.example.handymen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class UserSignupController {

    @FXML
    private TextField nameField;
    @FXML private CheckBox lengthCheck;
    @FXML private CheckBox upperLowerCheck;
    @FXML private CheckBox numberCheck;
    @FXML private CheckBox specialCharCheck;

    @FXML
    private TextField emailField;

    @FXML
    private TextField addressField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField confirmPasswordField;

    private Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:handymen.db");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    public void onSignUpSubmit(ActionEvent event) {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String pass = passwordField.getText();
        String confirmPass = confirmPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()
                || address.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            showAlert("Error", "All fields must be filled.");
            return;
        }
        if (!(lengthCheck.isSelected()
                && upperLowerCheck.isSelected()
                && numberCheck.isSelected()
                && specialCharCheck.isSelected())) {

            showAlert("Weak Password",
                    "Password must meet all security requirements.");
            return;
        }

        if (!pass.equals(confirmPass)) {
            showAlert("Error", "Passwords do not match.");
            return;
        }

        try (Connection conn = connect()) {

            if (conn == null) {
                showAlert("Error", "Database connection failed!");
                return;
            }

            String tableSQL = """
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        email TEXT UNIQUE NOT NULL,
                        phone TEXT NOT NULL,
                        address TEXT,
                        password TEXT NOT NULL
                    );
                    """;
            conn.createStatement().execute(tableSQL);


            String checkSQL = "SELECT email FROM users WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                showAlert("Error", "Email already registered. Try logging in.");
                return;
            }
            String sql = """
INSERT INTO users (name, email, phone, address, password)
VALUES (?, ?, ?, ?, ?)
""";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setString(5, pass);

            stmt.executeUpdate();



            showAlert("Success", "Account created successfully!");

            Parent root = FXMLLoader.load(getClass().getResource("user_login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 960));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Signup failed due to a database error.");
        }
    }
    @FXML
    private void onPasswordTyping() {
        String password = passwordField.getText();

        boolean lengthOk = password.length() >= 8;
        lengthCheck.setSelected(lengthOk);

        boolean upperLowerOk =
                password.matches(".*[A-Z].*") &&
                        password.matches(".*[a-z].*");
        upperLowerCheck.setSelected(upperLowerOk);

        boolean numberOk = password.matches(".*\\d.*");
        numberCheck.setSelected(numberOk);

        boolean specialOk = password.matches(".*[!@#$%^&*()_+=<>?/{}\\[\\]~`|-].*");
        specialCharCheck.setSelected(specialOk);
    }

    @FXML
    public void onBackToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("user_login.fxml"));
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
