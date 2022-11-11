package application.employeemanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

public class Welcome extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Welcome.class.getResource("welcome-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String company_icon_path = "src/main/resources/application/employeemanagement/photo/welcome-view/acus_company.png";
        stage.getIcons().add(new Image(Paths.get(company_icon_path).toUri().toString()));
        stage.setTitle("Acus Company Management Program");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> {
            stage.close();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}