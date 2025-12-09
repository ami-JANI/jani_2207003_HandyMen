package com.example.handymen;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    private void openPage(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onWorkerClick(ActionEvent event) throws IOException {
        openPage(event, "/com/example/handymen/Worker_login.fxml");
    }

    public void onUserClick(ActionEvent event) throws IOException {
        openPage(event, "/com/example/handymen/User_login.fxml");
    }
}
