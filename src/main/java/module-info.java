module com.example.gps_g12 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    //requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens com.example.gps_g12.goalTracker.controller;
    opens com.example.gps_g12.goalTracker to javafx.fxml;
    exports com.example.gps_g12.goalTracker;
}