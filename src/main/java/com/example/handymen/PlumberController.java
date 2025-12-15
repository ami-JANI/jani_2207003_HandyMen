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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PlumberController {

    @FXML
    private TableView<Worker> plumberTable;

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
    public void initialize() {

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        experienceCol.setCellValueFactory(new PropertyValueFactory<>("experience"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        loadPlumbers();
    }


    private void loadPlumbers() {
        ObservableList<Worker> list = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.connect()) {


            String sql = "SELECT * FROM workers WHERE category = 'Plumber'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Worker(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("experience"),
                        rs.getString("rate"),
                        rs.getString("location"),
                        rs.getString("category")
                ));
            }

            plumberTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load plumber data.");
        }
    }


    @FXML
    public void backToHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("InterFace.fxml"));
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
}
