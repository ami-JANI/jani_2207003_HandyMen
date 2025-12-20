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
import java.sql.*;

public class PainterController {

    @FXML private TableView<Worker> painterTable;
    @FXML private TableColumn<Worker, String> nameCol, emailCol, phoneCol, experienceCol, rateCol, locationCol;

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        experienceCol.setCellValueFactory(new PropertyValueFactory<>("experience"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        loadWorkers("Painter");
    }

    private void loadWorkers(String c) {
        ObservableList<Worker> list = FXCollections.observableArrayList();
        try (Connection con = DatabaseConnection.connect()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM workers WHERE profession=?");
            ps.setString(1, c);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Worker(
                        rs.getString("name"), rs.getString("email"),
                        rs.getString("phone"), rs.getString("experience"),
                        rs.getString("rate"), rs.getString("location"),
                        rs.getString("profession")
                ));
            }
            painterTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
            alert("Error", "Could not load " + c + " workers.");
        }
    }

    @FXML
    public void backToHome(ActionEvent e) throws IOException {
        Parent r = FXMLLoader.load(getClass().getResource("interface.fxml"));
        Stage s = (Stage) ((Node)e.getSource()).getScene().getWindow();
        s.setScene(new Scene(r, 1280, 960)); s.show();
    }

    void alert(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.show();
    }
}
