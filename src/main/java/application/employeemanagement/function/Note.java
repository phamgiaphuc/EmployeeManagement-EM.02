package application.employeemanagement.function;

import application.employeemanagement.Function;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Note {
    @FXML
    private DatePicker picker;
    @FXML
    private TextArea notes;
    @FXML
    private Map<LocalDate, String> data = new HashMap<>();

    public void initialize() {
        load();
        picker.valueProperty().addListener((o, oldDate, date) -> {
            notes.setText(data.getOrDefault(date, ""));
        });
        picker.setValue(LocalDate.now());
    }

    public void updateNotes() {
        data.put(picker.getValue(), notes.getText());
    }

    private void save() {
        try (ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(Paths.get("src/main/java/project/employee/data/note.txt")))) {
            stream.writeObject(data);
            System.out.println("Saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        Path file = Paths.get("src/main/java/project/employee/data/note.txt");
        if (Files.exists(file)) {
            try (ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(file))) {
                data = (Map<LocalDate, String>) stream.readObject();
                System.out.println("Loaded!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Back button
    public void onBackButtonClick(MouseEvent event) throws IOException {
        save();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Function.class.getResource("admin-view.fxml"));
        Parent addParent = loader.load();
        Scene addScene = new Scene(addParent);
        stage.setScene(addScene);
    }
}
