package com.example.handymen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.example.handymen.WorkerLoginController.loggedWorkerEmail;

public class WorkerDashboardController implements Initializable {

    @FXML private TextField nameField, emailField, phoneField, professionField, experienceField, rateField, locationField;
    @FXML private DatePicker calendar;
    @FXML private GridPane slotGrid;
    @FXML private VBox notificationPane;
    @FXML private VBox notificationList;

    private final String workerEmail = loggedWorkerEmail;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadWorkerData();
        calendar.setValue(LocalDate.now());
        calendar.setOnAction(e -> loadSlots(calendar.getValue()));
        loadSlots(LocalDate.now());
        //showPendingRequestAlert();
    }


    private void loadWorkerData() {
        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM workers WHERE email=?");
            ps.setString(1, workerEmail);
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
            int slot = i;
            Button btn = new Button("Slot " + slot);
            btn.setPrefWidth(130);

            SlotInfo info = getSlotInfo(date, slot);

            if (info == null) {

                btn.setStyle("-fx-background-color: lightgray;");
            }
            else if ("requested".equals(info.status)) {
                btn.setStyle("-fx-background-color: yellow;");
            }
            else if ("booked".equals(info.status)) {
                btn.setStyle("-fx-background-color: green; -fx-text-fill:white;");
            }

            btn.setOnAction(e -> {

                if (info == null) {
                                       return;
                }

                if ("requested".equals(info.status)) {
                    approveSlot(date, slot);
                }
                else if ("booked".equals(info.status)) {
                    clearSlot(date, slot);
                }

            });

            slotGrid.add(btn, (slot - 1) % 4, (slot - 1) / 4);
        }
    }
    private void clearSlot(LocalDate date, int slot) {
        try (Connection con = DatabaseConnection.connect()) {

            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM worker_slots WHERE worker_email=? AND work_date=? AND slot=?"
            );
            ps.setString(1, workerEmail);
            ps.setString(2, date.toString());
            ps.setInt(3, slot);

            ps.executeUpdate();
            loadSlots(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private SlotInfo getSlotInfo(LocalDate date, int slot) {
        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT status,user_email FROM worker_slots WHERE worker_email=? AND work_date=? AND slot=?"
            );
            ps.setString(1, workerEmail);
            ps.setString(2, date.toString());
            ps.setInt(3, slot);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new SlotInfo(rs.getString("status"), rs.getString("user_email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void approveSlot(LocalDate date, int slot) {
        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE worker_slots SET status='booked', seen=1 WHERE worker_email=? AND work_date=? AND slot=?"
            );
            ps.setString(1, workerEmail);
            ps.setString(2, date.toString());
            ps.setInt(3, slot);
            ps.executeUpdate();

            loadSlots(date);
            showAlert("Approved", "Slot booked successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showPendingRequestAlert() {
        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT work_date,slot FROM worker_slots WHERE worker_email=? AND status='requested'"
            );
            ps.setString(1, workerEmail);
            ResultSet rs = ps.executeQuery();

            StringBuilder msg = new StringBuilder();
            while (rs.next()) {
                msg.append(rs.getString("work_date"))
                        .append(" | Slot ")
                        .append(rs.getInt("slot"))
                        .append("\n");
            }

            if (!msg.isEmpty()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("New Requests");
                a.setHeaderText("You have new booking requests");
                a.setContentText(msg.toString());
                a.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void confirmCancel(LocalDate date, int slot) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Booking");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("This will cancel the booking.");

        alert.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                workerCancelSlot(date, slot);
            }
        });
    }


    private void cancelRequest(LocalDate date, int slot) {
        try (Connection conn = DatabaseConnection.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM worker_slots WHERE worker_email=? AND work_date=? AND slot=?"
            );
            ps.setString(1, workerEmail);
            ps.setString(2, date.toString());
            ps.setInt(3, slot);
            ps.executeUpdate();

            loadSlots(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void workerCancelSlot(LocalDate date, int slot) {

        try (Connection conn = DatabaseConnection.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM worker_slots WHERE worker_email=? AND work_date=? AND slot=?"
            );
            ps.setString(1, workerEmail);
            ps.setString(2, date.toString());
            ps.setInt(3, slot);
            ps.executeUpdate();

            loadSlots(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveChangesClick() {
        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE workers SET name=?,phone=?,profession=?,experience=? WHERE email=?"
            );
            ps.setString(1, nameField.getText());
            ps.setString(2, phoneField.getText());
            ps.setString(3, professionField.getText());
            ps.setString(4, experienceField.getText());
            ps.setString(5, workerEmail);

            ps.executeUpdate();
            showAlert("Success", "Profile updated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onSignOut(ActionEvent event) throws IOException {
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


    private static class SlotInfo {
        String status;
        String userEmail;
        SlotInfo(String s, String u) {
            status = s;
            userEmail = u;
        }
    }
    @FXML
    private void toggleNotifications() {
        boolean visible = notificationPane.isVisible();
        notificationPane.setVisible(!visible);

        if (!visible) {
            loadNotifications();
        }
    }

    private void loadNotifications() {

        notificationList.getChildren().clear();

        try (Connection conn = DatabaseConnection.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT user_email, work_date, slot FROM worker_slots " +
                            "WHERE worker_email=? AND status='requested' AND seen=0"
            );

            ps.setString(1, workerEmail);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String user = rs.getString("user_email");
                String date = rs.getString("work_date");
                int slot = rs.getInt("slot");

                Label lbl = new Label(
                        "👤 " + user +
                                "\n📅 " + date +
                                "\n⏰ Slot " + slot
                );
                lbl.setStyle("-fx-padding:10; -fx-border-color:#ddd;");

                notificationList.getChildren().add(lbl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearNotifications() {

        try (Connection conn = DatabaseConnection.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE worker_slots SET seen=1 " +
                            "WHERE worker_email=? AND status='requested'"
            );
            ps.setString(1, workerEmail);
            ps.executeUpdate();

            notificationList.getChildren().clear();

            showAlert("Notifications cleared",
                    "Requests are still pending.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
