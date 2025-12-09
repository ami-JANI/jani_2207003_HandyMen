package com.example.handymen;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import java.io.IOException;

public class InterFaceController {

    @FXML
    private StackPane contentPane;

    @FXML
    private void openElectricianPage() {
        loadPage("/com/example/handymen/ElectricianPage.fxml");
    }

    @FXML
    private void openPlumberPage() {
        loadPage("/com/example/handymen/PlumberPage.fxml");
    }

    @FXML
    private void openPainterPage() {
        loadPage("/com/example/handymen/PainterPage.fxml");
    }

    @FXML
    private void openMasonPage() {
        loadPage("/com/example/handymen/MasonPage.fxml");
    }

    @FXML
    private void openMaidPage() {
        loadPage("/com/example/handymen/MaidPage.fxml");
    }

    @FXML
    private void openInternetPage() {
        loadPage("/com/example/handymen/InternetPage.fxml");
    }

    private void loadPage(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().clear(); // clear previous content
            contentPane.getChildren().add(root); // add new content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
