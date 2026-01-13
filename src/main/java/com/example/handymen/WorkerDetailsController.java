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

    @FXML private Label nameLabel, emailLabel, phoneLabel, professionLabel,
            experienceLabel, rateLabel, locationLabel;
    @FXML private DatePicker datePicker;
    @FXML private GridPane slotsGrid;

    private Worker worker;

    // 🎨 COLORS
    private final Color AVAILABLE   = Color.GRAY;
    private final Color REQUESTED   = Color.GOLD;
    private final Color BOOKED      = Color.RED;

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
        loadSlots(datePicker.getValue());
    }
    private void handleSlotClick(Rectangle rect, LocalDate date, int slot) {

        rect.setOnMouseClicked(event -> {

            if (event.getClickCount() == 2) {
                // DOUBLE CLICK → CANCEL
                cancelSlot(rect, date, slot);
                return;
            }

            // SINGLE CLICK → REQUEST
            if (rect.getFill().equals(Color.LIGHTGREEN)) {
                requestSlot(rect, date, slot);
            }
        });
    }
    private void requestSlot(Rectangle rect, LocalDate date, int slot) {
        try (Connection conn = DatabaseConnection.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO worker_slots " +
                            "(worker_email, work_date, slot, status, user_email, seen) " +
                            "VALUES (?, ?, ?, ?, ?, ?)"
            );

            ps.setString(1, worker.getEmail());
            ps.setString(2, date.toString());
            ps.setInt(3, slot);
            ps.setString(4, "requested");
            ps.setString(5, loggedUserEmail);
            ps.setInt(6, 0);

            ps.executeUpdate();

            rect.setFill(REQUESTED);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelRequest(Rectangle rect, LocalDate date, int slot) {
        try (Connection conn = DatabaseConnection.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM worker_slots WHERE worker_email=? AND work_date=? AND slot=? AND user_email=?"
            );
            ps.setString(1, worker.getEmail());
            ps.setString(2, date.toString());
            ps.setInt(3, slot);
            ps.setString(4, loggedUserEmail);
            ps.executeUpdate();

            rect.setFill(AVAILABLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void cancelSlot(Rectangle rect, LocalDate date, int slot) {
        try (Connection conn = DatabaseConnection.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM worker_slots WHERE worker_email=? AND work_date=? AND slot=? AND user_email=?"
            );
            ps.setString(1, worker.getEmail());
            ps.setString(2, date.toString());
            ps.setInt(3, slot);
            ps.setString(4, loggedUserEmail);
            ps.executeUpdate();

            rect.setFill(Color.LIGHTGREEN);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ================= LOAD SLOTS =================
    private void loadSlots(LocalDate date) {
        slotsGrid.getChildren().clear();

        try (Connection conn = DatabaseConnection.connect()) {

            for (int i = 1; i <= 8; i++) {
                int slotNo = i;

                Rectangle rect = new Rectangle(80, 50);
                rect.setStroke(Color.BLACK);

                PreparedStatement ps = conn.prepareStatement(
                        "SELECT status, user_email FROM worker_slots " +
                                "WHERE worker_email=? AND work_date=? AND slot=?"
                );
                ps.setString(1, worker.getEmail());
                ps.setString(2, date.toString());
                ps.setInt(3, slotNo);

                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    rect.setFill(Color.LIGHTGRAY);

                    rect.setOnMouseClicked(e ->
                            requestSlot(rect, date, slotNo)
                    );
                }

                else {
                    String status = rs.getString("status");
                    String userEmail = rs.getString("user_email");

                    if ("requested".equals(status)) {

                        if (userEmail.equals(loggedUserEmail)) {

                            rect.setFill(Color.GOLD);

                            rect.setOnMouseClicked(e ->
                                    cancelRequest(rect, date, slotNo)
                            );

                        } else {

                            rect.setFill(Color.RED);
                            rect.setDisable(true);
                        }
                    }

                    else if ("booked".equals(status)) {

                        if (userEmail.equals(loggedUserEmail)) {
                            rect.setFill(Color.GREEN);

                        } else {
                            rect.setFill(Color.RED);
                            rect.setDisable(true);
                        }
                    }
                }

                slotsGrid.add(rect, (i - 1) % 4, (i - 1) / 4);
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
