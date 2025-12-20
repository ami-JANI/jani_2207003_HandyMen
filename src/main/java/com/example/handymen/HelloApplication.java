package com.example.handymen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("First.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 960);
        stage.setTitle("Handyman Finder - Login");
        stage.setScene(scene);
        stage.show();
        DatabaseSetup.createWorkerTable();
        DatabaseSetup.createUserTable();
    }

    public static void main(String[] args) {
        launch();
    }
}
