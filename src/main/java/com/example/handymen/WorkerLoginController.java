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

public class WorkerLoginController {

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

        if (email.equals("test@gmail.com") && password.equals("1234")) {
            showAlert("Success", "Login Successful!");
            // After login you can navigate to home page
        } else {
            showAlert("Error", "Incorrect Email or Password.");
        }
    }

    @FXML
    public void onSignUpClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Worker_signup.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
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
