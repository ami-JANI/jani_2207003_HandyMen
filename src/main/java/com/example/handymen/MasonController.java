
package com.example.handymen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MasonController {

    @FXML private TableView<Worker> masonTable;
    @FXML private TableColumn<Worker, String> nameCol, emailCol, phoneCol, experienceCol, rateCol, locationCol;
    @FXML private ComboBox<String> filterBox;

    @FXML
    public void initialize() {

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        experienceCol.setCellValueFactory(new PropertyValueFactory<>("experience"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        filterBox.setItems(FXCollections.observableArrayList(
                "My Location",
                "All Workers"
        ));

        filterBox.setValue("My Location");

        loadWorkers(true);

        filterBox.setOnAction(e -> {
            boolean onlyMyLocation = filterBox.getValue().equals("My Location");
            loadWorkers(onlyMyLocation);
        });
    }

    private void loadWorkers(boolean onlyMyLocation) {

        ObservableList<Worker> list = FXCollections.observableArrayList();
        String sql;

        if (onlyMyLocation) {
            sql = """
                  SELECT * FROM workers
                  WHERE profession=?
                  AND LOWER(TRIM(location)) = LOWER(TRIM(?))
                  """;
        } else {
            sql = "SELECT * FROM workers WHERE profession=?";
        }

        try (Connection con = DatabaseConnection.connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "Mason");

            if (onlyMyLocation) {
                ps.setString(2, UserSession.getUserLocation());
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Worker(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("experience"),
                        rs.getString("rate"),
                        rs.getString("location"),
                        rs.getString("profession")
                ));
            }

            masonTable.setItems(list);

            if (list.isEmpty()) {
                alert("Info", onlyMyLocation
                        ? "No masons found in your location."
                        : "No masons available.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            alert("Error", "Failed to load masons.");
        }
    }

    @FXML
    public void backToHome(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("interface.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1280, 960));
        stage.show();
    }

    private void alert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
