package com.example.handymen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MasonController {

    @FXML
    private TableView<Worker> workerTable;  // matches fx:id in FXML

    @FXML
    private TableColumn<Worker, String> nameCol;

    @FXML
    private TableColumn<Worker, String> emailCol;

    @FXML
    private TableColumn<Worker, String> phoneCol;

    @FXML
    private TableColumn<Worker, String> experienceCol;

    @FXML
    private TableColumn<Worker, String> rateCol;

    @FXML
    private TableColumn<Worker, String> locationCol;

    @FXML
    private Button backBtn;  // matches FXML fx:id

    private ObservableList<Worker> masonList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // link table columns to Worker properties
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        experienceCol.setCellValueFactory(new PropertyValueFactory<>("experience"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        loadMasons();
    }

    private void loadMasons() {
        masonList.clear();
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT name, email, phone, experience, rate, location FROM workers WHERE category = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "Mason");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                masonList.add(new Worker(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("experience"),
                        rs.getString("rate"),
                        rs.getString("location")
                ));
            }

            workerTable.setItems(masonList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backToHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Interface.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1280, 960));
        stage.show();
    }

    // Worker class to hold table data
    public static class Worker {
        private final String name;
        private final String email;
        private final String phone;
        private final String experience;
        private final String rate;
        private final String location;

        public Worker(String name, String email, String phone, String experience, String rate, String location) {
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.experience = experience;
            this.rate = rate;
            this.location = location;
        }

        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getExperience() { return experience; }
        public String getRate() { return rate; }
        public String getLocation() { return location; }
    }
}
