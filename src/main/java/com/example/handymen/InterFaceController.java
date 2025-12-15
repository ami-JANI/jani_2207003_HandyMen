package com.example.handymen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InterFaceController {

    private void openPage(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 960));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openElectricianPage(ActionEvent event) {
        openPage(event, "/com/example/handymen/electrician.fxml");
    }

    @FXML
    private void openPlumberPage(ActionEvent event) {
        openPage(event, "/com/example/handymen/Plumber.fxml");
    }

    @FXML
    private void openPainterPage(ActionEvent event) {
        openPage(event, "/com/example/handymen/Painter.fxml");
    }

    @FXML
    private void openMasonPage(ActionEvent event) {
        openPage(event, "/com/example/handymen/Mason.fxml");
    }

    @FXML
    private void openMaidPage(ActionEvent event) {
        openPage(event, "/com/example/handymen/Maid.fxml");
    }

    @FXML
    private void openInternetPage(ActionEvent event) {
        openPage(event, "/com/example/handymen/Internet.fxml");
    }
}
