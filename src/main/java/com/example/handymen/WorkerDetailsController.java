package com.example.handymen;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import static com.example.handymen.UserLoginController.loggedUserEmail;

public class WorkerDetailsController {

    @FXML private Label nameLabel, emailLabel, phoneLabel, professionLabel, experienceLabel, rateLabel, locationLabel;
    @FXML private DatePicker datePicker;
    @FXML private GridPane slotsGrid;

    private Worker worker;

    private final Color AVAILABLE = Color.LIGHTGRAY;
    private final Color REQUESTED = Color.YELLOW;
    private final Color BOOKED = Color.LIGHTGREEN;
    private final Color UNAVAILABLE = Color.RED;

    public void setWorker(Worker worker) {
        this.worker = worker;

        nameLabel.setText(worker.getName());
        emailLabel.setText(worker.getEmail());
        phoneLabel.setText(worker.getPhone());
        professionLabel.setText(worker.getProfession());
        experienceLabel.setText(worker.getExperience());
        rateLabel.setText(worker.getRate());
        locationLabel.setText(worker.getLocation());

        datePicker.setValue(LocalDate.now());
        loadSlots(LocalDate.now());
    }

    @FXML
    private void onDateSelected() {
        LocalDate date = datePicker.getValue();
        loadSlots(date);
    }

    private void loadSlots(LocalDate date) {
        slotsGrid.getChildren().clear();

        try (Connection conn = DatabaseConnection.connect()) {
            for (int i = 0; i < 8; i++) {
                int slot = i + 1;

                PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM worker_slots WHERE worker_email=? AND work_date=? AND slot=?"
                );
                ps.setString(1, worker.getEmail());
                ps.setString(2, date.toString());
                ps.setInt(3, slot);

                ResultSet rs = ps.executeQuery();

                Rectangle rect = new Rectangle(80, 50);
                rect.setStroke(Color.BLACK);

                if (rs.next()) {
                    String status = rs.getString("status");
                    switch (status) {
                        case "requested" -> rect.setFill(REQUESTED);
                        case "booked" -> rect.setFill(BOOKED);
                        case "unavailable" -> rect.setFill(UNAVAILABLE);
                        default -> rect.setFill(AVAILABLE);
                    }
                } else {
                    rect.setFill(AVAILABLE);
                }

                final int finalSlot = slot;
                rect.setOnMouseClicked(event -> handleSlotClick(rect, date, finalSlot));
                if (rect.getFill() == UNAVAILABLE || rect.getFill() == BOOKED) {
                    rect.setDisable(true);
                }

                slotsGrid.add(rect, i % 4, i / 4);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSlotClick(Rectangle rect, LocalDate date, int slot) {
        try (Connection conn = DatabaseConnection.connect()) {
            String currentColor = rect.getFill().toString();

            if (currentColor.equals(AVAILABLE.toString())) {

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO worker_slots(worker_email, work_date, slot, status, user_email) VALUES(?,?,?,?,?)"
                );
                ps.setString(1, worker.getEmail());
                ps.setString(2, date.toString());
                ps.setInt(3, slot);
                ps.setString(4, "requested");
                ps.setString(5, loggedUserEmail);
                ps.executeUpdate();

                rect.setFill(REQUESTED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClose() {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        stage.close();
    }
}
