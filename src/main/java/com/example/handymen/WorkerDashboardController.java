package com.example.handymen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
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
    @FXML private DatePicker calendar;
    @FXML private GridPane slotGrid;
    private final String workerEmail = WorkerLoginController.loggedWorkerEmail;
    @FXML
    public void initialize() {
        loadWorkerData();

        calendar.setValue(LocalDate.now());
        calendar.setOnAction(e -> loadSlots(calendar.getValue()));

        loadSlots(LocalDate.now());
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
    private void loadSlots(LocalDate date) {
        slotGrid.getChildren().clear();

        for (int i = 1; i <= 8; i++) {
            Button slotBtn = new Button("Slot " + i);
            slotBtn.setPrefWidth(120);

            String status = getSlotStatus(date, i);

            if (status.equals("BOOKED")) {
                slotBtn.setStyle("-fx-background-color:#e74c3c; -fx-text-fill:white;");
                slotBtn.setDisable(true);
            } else if (status.equals("PENDING")) {
                slotBtn.setStyle("-fx-background-color:#f39c12; -fx-text-fill:white;");
                int finalI = i;
                slotBtn.setOnAction(e -> approveSlot(date, finalI));
            } else {
                slotBtn.setStyle("-fx-background-color:#2ecc71; -fx-text-fill:white;");
                slotBtn.setDisable(true);
            }

            slotGrid.add(slotBtn, (i - 1) % 4, (i - 1) / 4);
        }
    }

    private String getSlotStatus(LocalDate date, int slot) {
        try (Connection con = DatabaseConnection.connect()) {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT status FROM worker_slots WHERE worker_email=? AND work_date=? AND slot=?"
            );
            ps.setString(1, workerEmail);
            ps.setString(2, date.toString());
            ps.setInt(3, slot);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "AVAILABLE";
    }

    private void approveSlot(LocalDate date, int slot) {
        try (Connection con = DatabaseConnection.connect()) {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE worker_slots SET status='BOOKED' WHERE worker_email=? AND work_date=? AND slot=?"
            );
            ps.setString(1, workerEmail);
            ps.setString(2, date.toString());
            ps.setInt(3, slot);
            ps.executeUpdate();

            showAlert("Approved", "Slot booked successfully.");
            loadSlots(date);
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

        String updateQuery = "UPDATE workers SET name=?,phone=? ,profession=?,experience=? WHERE email=?";


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

    @FXML public void onSignOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("First.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1280, 960));
        stage.show();
    }


    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadWorkerData();

        calendar.setValue(LocalDate.now());
        calendar.setOnAction(e -> loadSlots(calendar.getValue()));

        loadSlots(LocalDate.now());
    }
}
