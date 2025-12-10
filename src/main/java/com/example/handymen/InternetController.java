package com.example.handymen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InternetController {

    @FXML
    private TableView<Worker> internetTable;

    @FXML
    private TableColumn<Worker, String> nameColumn;

    @FXML
    private TableColumn<Worker, String> emailColumn;

    @FXML
    private TableColumn<Worker, String> phoneColumn;

    @FXML
    private Button backButton;

    private ObservableList<Worker> internetList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        loadInternetWorkers();
    }

    private void loadInternetWorkers() {
        internetList.clear();
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT name, email, phone FROM workers WHERE category = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "Internet");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                internetList.add(new Worker(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }

            internetTable.setItems(internetList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backToHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("interface.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1280, 960));
        stage.show();
    }

    public static class Worker {
        private final String name;
        private final String email;
        private final String phone;

        public Worker(String name, String email, String phone) {
            this.name = name;
            this.email = email;
            this.phone = phone;
        }

        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
    }
}
