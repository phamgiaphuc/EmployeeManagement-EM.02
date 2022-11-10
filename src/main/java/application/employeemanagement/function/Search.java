package application.employeemanagement.function;

import application.employeemanagement.Function;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.employee.Employee;
import utilities.ConstVariables;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Search {
    @FXML
    private ComboBox<String> showOptionComboBox;
    @FXML
    public TableView<Employee> table;
    @FXML
    private TableColumn<Employee, Integer> idColumn;
    @FXML
    private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, Integer> ageColumn;
    @FXML
    private TableColumn<Employee, String> addressColumn;
    @FXML
    private TableColumn<Employee, String> jobColumn;
    @FXML
    private TableColumn<Employee, String> levelColumn;
    @FXML
    private TextField idSearch;
    public String line;

    // ObservableList
    ObservableList<String> showOption_list = FXCollections.observableArrayList("Show all", "Show economy list", "Show develop list");
    ObservableList<Employee> showList = FXCollections.observableArrayList();


    /**
     * Functions:
     */
    // Show options
    public void onShowButtonClick(MouseEvent event) throws IOException {
        idSearch.clear();
        String option = showOptionComboBox.getValue();
        if (option == null) {
            errorSearch("Please choose an option to show", event);
        } else {
            table.getItems().clear();
            BufferedReader reader = new BufferedReader(new FileReader(ConstVariables.EMPLOYEE_DATA_PATH));
            String currentLine;
            if (option.equals("Show all")) {
                while ((currentLine = reader.readLine()) != null) {
                    String[] data = currentLine.split("@");
                    tableShowView(data);
                }
                reader.close();
            } else if (option.equals("Show economy list")) {
                while ((currentLine = reader.readLine()) != null) {
                    String[] data = currentLine.split("@");
                    if (data[4].equals("Economy")) {
                        tableShowView(data);
                    }
                }
            } else {
                while ((currentLine = reader.readLine()) != null) {
                    String[] data = currentLine.split("@");
                    if (data[4].equals("Develop")) {
                        tableShowView(data);
                    }
                }
            }
            reader.close();
        }
    }

    // Search employee by ID
    public void onSearchIDButtonClick(MouseEvent event) throws IOException {
        if (idSearch.getText().equals("")) {
            errorSearch("Please choose an ID to search", event);
        } else {
            table.getItems().clear();
            BufferedReader reader = new BufferedReader(new FileReader(ConstVariables.EMPLOYEE_DATA_PATH));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] searchData = currentLine.split("@");
                if (searchData[0].equals(idSearch.getText())) {
                    tableShowView(searchData);
                }
            }
            reader.close();
        }
    }

    private void tableShowView(String[] data) {
        showList.add(new Employee(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2]), data[3], data[4], data[5]));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        jobColumn.setCellValueFactory(new PropertyValueFactory<>("job"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        table.setItems(showList);
    }

    // Alert
    private void errorSearch(String error_text, MouseEvent event) {
        Alert error_add = new Alert(Alert.AlertType.ERROR, "Empty Show Option", ButtonType.OK);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        error_add.setTitle("Empty Show Option");
        error_add.setContentText(error_text + "!");
        Image image = new Image("https://upload.wikimedia.org/wikipedia/commons/e/ec/Error-icon.png");
        ImageView imageView = new ImageView(image);
        error_add.setGraphic(imageView);
        error_add.initModality(Modality.APPLICATION_MODAL);
        error_add.initOwner(stage);
        error_add.showAndWait();
    }

    // Back button
    public void onBackButtonClick(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Function.class.getResource("main-view.fxml"));
        Parent addParent = loader.load();
        Scene addScene = new Scene(addParent);
        stage.setScene(addScene);
    }

    public void showOption_comboBox() {
        showOptionComboBox.setItems(showOption_list);
    }

}
