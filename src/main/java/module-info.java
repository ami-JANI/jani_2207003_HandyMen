module com.example.handymen {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    opens com.example to javafx.fxml;
    exports com.example;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires javafx.base;
    requires javafx.graphics;

    opens com.example.handymen to javafx.fxml;
    exports com.example.handymen;
}