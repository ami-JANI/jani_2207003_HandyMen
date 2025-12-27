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

public class MaidController {

    @FXML private TableView<Worker> maidTable;
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

        filterBox.setValue("My Location");

        loadWorkers("Maid", true);

        filterBox.setOnAction(e -> {
            boolean onlyMyLocation = filterBox.getValue().equals("My Location");
            loadWorkers("Maid", onlyMyLocation);
        });
    }


    private void loadWorkers(String profession, boolean onlyMyLocation) {
        ObservableList<Worker> list = FXCollections.observableArrayList();

        String sql;

        if (onlyMyLocation) {
            sql = "SELECT * FROM workers WHERE profession=? AND LOWER(TRIM(location)) = LOWER(TRIM(?))";
        } else {
            sql = "SELECT * FROM workers WHERE profession=?";
        }

        try (Connection con = DatabaseConnection.connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, profession);

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

            maidTable.setItems(list);

            if (list.isEmpty()) {
                alert("Info", "No workers found in your location.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            alert("Error", "Could not load workers.");
        }
    }


    @FXML
    public void backToHome(ActionEvent e) throws IOException {
        Parent r = FXMLLoader.load(getClass().getResource("interface.fxml"));
        Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
        s.setScene(new Scene(r, 1280, 960));
        s.show();
    }

    void alert(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.show();
    }
}