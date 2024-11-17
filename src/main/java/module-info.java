module com.beginsecure.socialnetworkjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;

    opens com.beginsecure.socialnetworkjfx to javafx.fxml;
    opens controller to javafx.fxml;
    exports controller;
    exports com.beginsecure.socialnetworkjfx;
}