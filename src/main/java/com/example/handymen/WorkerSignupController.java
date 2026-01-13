package com.example.handymen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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

    @FXML private CheckBox lengthCheck;
    @FXML private CheckBox upperLowerCheck;
    @FXML private CheckBox numberCheck;
    @FXML private CheckBox specialCharCheck;

    @FXML
    public void onPasswordTyping() {
        String password = passwordField.getText();

        lengthCheck.setSelected(password.length() >= 8);

        upperLowerCheck.setSelected(
                password.matches(".*[A-Z].*") &&
                        password.matches(".*[a-z].*")
        );

        numberCheck.setSelected(password.matches(".*\\d.*"));

        specialCharCheck.setSelected(
                password.matches(".*[!@#$%^&*].*")
        );
    }

    private boolean isPasswordValid() {
        return lengthCheck.isSelected()
                && upperLowerCheck.isSelected()
                && numberCheck.isSelected()
                && specialCharCheck.isSelected();
    }

    @FXML
    public void onSignUpSubmit(ActionEvent event) {

        if (nameField.getText().isEmpty()
                || emailField.getText().isEmpty()
                || phoneField.getText().isEmpty()
                || professionField.getValue() == null
                || experienceField.getText().isEmpty()
                || rateField.getText().isEmpty()
                || locationField.getText().isEmpty()
                || passwordField.getText().isEmpty()
                || confirmPasswordField.getText().isEmpty()) {

            showAlert("Error", "All fields must be filled.");
            return;
        }

        if (!isPasswordValid()) {
            showAlert("Error", "Password does not meet security requirements.");
            return;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showAlert("Error", "Passwords do not match.");
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {

            PreparedStatement checkStmt =
                    conn.prepareStatement("SELECT email FROM workers WHERE email = ?");
            checkStmt.setString(1, emailField.getText());

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                showAlert("Error", "Email already registered.");
                return;
            }

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO workers (name, email, phone, profession, experience, rate, location, password) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );

            ps.setString(1, nameField.getText());
            ps.setString(2, emailField.getText());
            ps.setString(3, phoneField.getText());
            ps.setString(4, professionField.getValue());
            ps.setString(5, experienceField.getText());
            ps.setString(6, rateField.getText());
            ps.setString(7, locationField.getText());
            ps.setString(8, passwordField.getText());

            ps.executeUpdate();

            showAlert("Success", "Account created successfully!");

            Parent root = FXMLLoader.load(getClass().getResource("Worker_login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 960));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Signup failed.");
        }
    }

    @FXML
    public void onBackToLogin(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Worker_login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1280, 960));
        stage.show();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
