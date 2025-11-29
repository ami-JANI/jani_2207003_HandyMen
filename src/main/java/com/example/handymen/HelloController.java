package com.example.handymen;

import javafx.fxml.FXML;

public class HelloController {

    @FXML
    protected void onWorkerClick() {
        System.out.println("Worker selected");

    }

    @FXML
    protected void onUserClick() {
        System.out.println("User selected");

    }
}
