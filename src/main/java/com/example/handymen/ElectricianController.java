package com.example.handymen;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ElectricianController {

    @FXML private TableView<Worker> workerTable;

    @FXML private TableColumn<Worker, String> colEmail;
    @FXML private TableColumn<Worker, String> colName;
    @FXML private TableColumn<Worker, String> colPhone;
    @FXML private TableColumn<Worker, String> colExperience;
    @FXML private TableColumn<Worker, String> colArea;
    @FXML private TableColumn<Worker, String> colPrice;

    @FXML
    public void initialize() {
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colPhone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));
        colExperience.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getExperience()));
        colArea.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getArea()));
        colPrice.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrice()));

        loadData();
    }

    private void loadData() {
        ObservableList<Worker> list = FXCollections.observableArrayList();

        String query = "SELECT * FROM workers WHERE category = 'Electrician'";

        try {
            Connection conn = DatabaseConnection.connect();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                list.add(new Worker(
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("experience"),
                        rs.getString("area"),
                        rs.getString("category"),
                        rs.getString("price")
                ));
            }

            workerTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goHome() {
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/example/handymen/Interface.fxml"))));
            stage.show();
            workerTable.getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
