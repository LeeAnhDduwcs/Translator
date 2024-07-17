module main.translator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires javafx.web;
    requires jlayer;
    requires java.sql;
    requires java.desktop;

    opens main.translator to javafx.fxml;
    exports main.translator;
}