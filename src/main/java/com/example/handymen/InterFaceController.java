package com.example.handymen;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class InterFaceController {

    @FXML
    private VBox sidePanel;
    @FXML
    private GridPane mainGrid;
    @FXML
    private Pane overlay;

    private final double MENU_WIDTH = 280;

    @FXML
    public void initialize() {
        sidePanel.setTranslateX(MENU_WIDTH);
    }

    @FXML
    private void toggleMenu() {
        boolean isOpen = sidePanel.isVisible();

        if (!isOpen) {
            sidePanel.setVisible(true);
            overlay.setVisible(true);
            mainGrid.setDisable(true);

            TranslateTransition tt = new TranslateTransition(Duration.millis(200), sidePanel);
            tt.setFromX(280);
            tt.setToX(0);
            tt.play();

        } else {
            TranslateTransition tt = new TranslateTransition(Duration.millis(200), sidePanel);
            tt.setFromX(0);
            tt.setToX(280);
            tt.setOnFinished(event -> {
                sidePanel.setVisible(false);
                overlay.setVisible(false);
                mainGrid.setDisable(false);
            });
            tt.play();
        }
    }


    @FXML
    void openProfile(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("user_profile.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1280, 960));
        stage.show();
    }

    @FXML
    void openRequests(ActionEvent e) {
        System.out.println("Requests");
    }

    @FXML
    void openOngoingWork(ActionEvent e) {
        System.out.println("Ongoing Work");
    }

    @FXML
    void signOut(ActionEvent e) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("First.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1280, 960));
        stage.show();
    }
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
