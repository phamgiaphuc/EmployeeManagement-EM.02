module application.employeemanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires javafx.media;
    requires java.desktop;

    opens application.employeemanagement to javafx.fxml;
    opens application.employeemanagement.function to javafx.fxml;
    opens project.employee to javafx.base;
    exports application.employeemanagement;
}