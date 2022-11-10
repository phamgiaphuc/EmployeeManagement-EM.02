package application.employeemanagement.function;

import application.employeemanagement.Function;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.employee.Employee;
import project.employee.base.BaseService;
import utilities.ConstVariables;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringJoiner;

public class Edit extends BaseService implements Initializable {
    @FXML
    private Label final_line;
    @FXML
    private Label edit_line;
    @FXML
    private Label number_area;
    @FXML
    private Label area;
    @FXML
    private Label job;
    @FXML
    private Label level;
    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField age;
    @FXML
    private TextField address;
    @FXML
    private TextField idSearch;
    @FXML
    private ComboBox<String> showOptionComboBox;
    @FXML
    private ComboBox<String> jobComboBox;
    @FXML
    private ComboBox<String> levelComboBox;
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
    private Pane confirmation_stage;
    // Variables
    private String employee_ID;
    private String employee_NAME;
    private String employee_AGE;
    private String employee_ADDRESS;
    private String employee_JOB;
    private String employee_LEVEL;
    private String[] change_line;
    private String line;
    private Set<String> check_economy;
    private Set<String> check_develop;
    private String MISSING_DATA;
    private String NEW_DATA;

    // ObservableList
    ObservableList<String> showOption_list = FXCollections.observableArrayList("Show all", "Show economy list", "Show develop list");
    ObservableList<String> job_list = FXCollections.observableArrayList("Economy", "Develop");
    ObservableList<String> economy_list = FXCollections.observableArrayList("Manager", "Analyst", "Accountant");
    ObservableList<String> develop_list = FXCollections.observableArrayList("Front-end", "Back-end", "Full-stack");
    ObservableList<Employee> showList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        first_view();
        clear_editLine();
    }

    /**
     * Functions:
     */
    // Show options
    public void onShowButtonClick(MouseEvent event) throws IOException {
        idSearch.clear();
        String option = showOptionComboBox.getValue();
        if (option == null) {
            errorSearch("Empty Show Option", "Please choose an ID to search", event);
        } else {
            clear_editLine();
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
            errorSearch("Empty Show Option", "Please choose an ID to search", event);
        } else {
            clear_editLine();
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

    // Confirm button
    public void onConfirmButtonClick(MouseEvent event) {
        // Employee information before editing
        String FIRST_DATA = edit_line.getText();
        // Check if there is an employee information to edit
        if (!FIRST_DATA.equals("No edit line...") && !FIRST_DATA.equals("Please choose a row to edit...")) {
            // Employee information after editing
            NEW_DATA = getEmployeeInfo();
            // Check the SECOND_DATA if there is any empty information
            if (checkEmptyText().equals("true")) {
                if (valid_job_leve()) {
                    // Check ID and AGE
                    if (checkIfIsInteger(event) == 0) {
                        if (!change_line[0].equals(employee_ID)) {
                            // Check if the ID is duplicated/ valid ID
                            if (checkIDTextField(event) == 0) {
                                // Check if the JOB and LEVEL are correct
                                number_changedAreas();
                                final_line.setText(employee_ID + " - " + employee_NAME + " - " + employee_AGE + " - " + employee_ADDRESS + " - " + employee_JOB + " - " + employee_LEVEL);
                                confirmation_stage.setVisible(true);
                            }
                        } else {
                            number_changedAreas();
                            confirmation_stage.setVisible(true);
                            final_line.setText(employee_ID + " - " + employee_NAME + " - " + employee_AGE + " - " + employee_ADDRESS + " - " + employee_JOB + " - " + employee_LEVEL);
                        }
                    }
                } else {
                    errorSearch("Invalid level option", employee_LEVEL + " is invalid. Please choose again", event);
                }
            } else {
                errorSearch("Empty information error", MISSING_DATA + " is empty. Please fill properly", event);
            }
        } else {
            errorSearch("Empty editing information error", "Please choose a line to edit", event);
        }
    }

    public void editableRow() {
        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                line = String.valueOf(table.getSelectionModel().getSelectedItem());
                if (line.equals("null")) {
                    edit_line.setText("Please choose a row to edit...");
                } else {
                    change_line = line.split("@");
                    StringJoiner s = new StringJoiner(" - ");
                    for (String value : change_line) {
                        s.add(value);
                    }
                    edit_line.setText(s.toString());
                    id.setText(change_line[0]);
                    name.setText(change_line[1]);
                    age.setText(change_line[2]);
                    address.setText(change_line[3]);
                    job.setText(change_line[4]);
                    level.setText(change_line[5]);
                }
            }
        });
    }

    // Edit button
    public void onEditButtonClick(MouseEvent event) throws IOException {
        Alert final_confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Notification", ButtonType.YES, ButtonType.CANCEL);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        final_confirmation.setContentText("Are you sure you want to edit this employee?");
        final_confirmation.initModality(Modality.APPLICATION_MODAL);
        final_confirmation.initOwner(stage);
        final_confirmation.showAndWait();
        if (final_confirmation.getResult() == ButtonType.YES) {
            editAndAdd(NEW_DATA, change_line[0]);
            Alert editSuccess = new Alert(Alert.AlertType.CONFIRMATION, "Notification", ButtonType.OK);
            editSuccess.setContentText("Add Employee's Information successfully!");
            editSuccess.initModality(Modality.APPLICATION_MODAL);
            editSuccess.initOwner(stage);
            editSuccess.showAndWait();
            if (editSuccess.getResult() == ButtonType.OK) {
                first_view();
                clear_editLine();
                table.getItems().clear();
                BufferedReader reader = new BufferedReader(new FileReader(ConstVariables.EMPLOYEE_DATA_PATH));
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    String[] data = currentLine.split("@");
                    tableShowView(data);
                }
                reader.close();
            }
        }
    }

    // Alert
    private void errorSearch(String error_title, String error_text, MouseEvent event) {
        Alert error_search = new Alert(Alert.AlertType.ERROR, error_title, ButtonType.OK);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        error_search.setTitle(error_title);
        error_search.setContentText(error_text + "!");
        Image image = new Image("https://upload.wikimedia.org/wikipedia/commons/e/ec/Error-icon.png");
        ImageView imageView = new ImageView(image);
        error_search.setGraphic(imageView);
        error_search.initModality(Modality.APPLICATION_MODAL);
        error_search.initOwner(stage);
        error_search.showAndWait();
    }

    // Show TableView
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

    // Check if the JOB and LEVEL are correct
    private boolean valid_job_leve() {
        switch (employee_JOB) {
            case "Economy" -> {
                if (check_economy.contains(employee_LEVEL)) {
                    return true;
                }
            }
            case "Develop" -> {
                if (check_develop.contains(employee_LEVEL)) {
                    return true;
                }
            }
        }
        return false;
    }

    // The number of areas that admin changes
    private void number_changedAreas() {
        int count = 0;
        StringJoiner s = new StringJoiner(", ");
        if (!change_line[0].equals(employee_ID)) {
            count += 1;
            s.add("ID");
        }
        if (!change_line[1].equals(employee_NAME)) {
            count += 1;
            s.add("NAME");
        }
        if (!change_line[2].equals(employee_AGE)) {
            count += 1;
            s.add("AGE");
        }
        if (!change_line[3].equals(employee_ADDRESS)) {
            count += 1;
            s.add("ADDRESS");
        }
        if (!change_line[4].equals(employee_JOB)) {
            count += 1;
            s.add("JOB");
        }
        if (!change_line[5].equals(employee_LEVEL)) {
            count += 1;
            s.add("LEVEL");
        }
        String CHANGED_AREA = s.toString();
        number_area.setText(String.valueOf(count));
        if (count == 0) {
            area.setText("area.");
        } else if (count == 1) {
            area.setText("area: " + CHANGED_AREA + ".");
        } else {
            area.setText("areas: " + CHANGED_AREA + ".");
        }
    }

    // Getting input information from the user
    private String getEmployeeInfo() {
        employee_ID = checkInfo(id.getText().trim());
        employee_NAME = checkInfo(name.getText().trim());
        employee_AGE = checkInfo(age.getText().trim());
        employee_ADDRESS = checkInfo(address.getText().trim());
        if (jobComboBox.getValue() == null) {
            employee_JOB = change_line[4];
        } else {
            employee_JOB = jobComboBox.getValue();
        }
        if (levelComboBox.getValue() == null) {
            employee_LEVEL = change_line[5];
        } else {
            employee_LEVEL = levelComboBox.getValue();
        }
        return employee_ID + "@" + employee_NAME + "@" + employee_AGE + "@" + employee_ADDRESS + "@" + employee_JOB + "@" + employee_LEVEL;
    }

    // Check if theres is a string "null" in the final output of user's information
    private String checkEmptyText() {
        String isEmpty = "true";
        MISSING_DATA = "";
        String[] checkEmptyText = NEW_DATA.split("@");
        for (int i = 0; i < checkEmptyText.length; i++) {
            if (checkEmptyText[i].equals("null")) {
                isEmpty = "false";
                MISSING_DATA = missingFeature(i);
            }
        }
        if (MISSING_DATA.endsWith(", ")) {
            MISSING_DATA = MISSING_DATA.substring(0, MISSING_DATA.length() - 2);
        }
        System.out.println(MISSING_DATA);
        return isEmpty;
    }

    // Check if ID or AGE is an integer
    private int checkIfIsInteger(MouseEvent event) {
        if (checkInteger(employee_ID) == -1) {
            errorSearch("Wrong information error", "Please type the right integer/number for ID", event);
            id.clear();
            id.setPromptText("Enter the ID");
            return -1;
        }
        if (checkInteger(employee_AGE) == -1) {
            errorSearch("Wrong information error", "Please type the right integer/number for AGE", event);
            age.clear();
            age.setPromptText("Enter the AGE");
            return -1;
        } else {
            if (checkAgeRange(employee_AGE) == -1) {
                errorSearch("Invalid Age Error", "Please type the valid AGE", event);
                age.clear();
                age.setPromptText("Enter the AGE");
                return -1;
            }
        }
        return 0;
    }

    // Check if the ID is already had in the list - if it is true, alert the user to input a new ID again.
    private int checkIDTextField(MouseEvent event) {
        int result = checkId(employee_ID);
        if (result == -1) {
            errorSearch("Duplicated ID error", "This ID " + employee_ID + " is already had. Please try another id", event);
            id.clear();
            id.setPromptText("Enter the ID");
            return -1;
        }
        return 0;
    }

    // Missing information
    private String missingFeature(int i) {
        String missingFeature;
        Map<Integer, String> mapFeature = new HashMap<>();
        mapFeature.put(0, "ID");
        mapFeature.put(1, "NAME");
        mapFeature.put(2, "AGE");
        mapFeature.put(3, "ADDRESS");
        mapFeature.put(4, "JOB");
        mapFeature.put(5, "LEVEL");
        String temp = MISSING_DATA;
        missingFeature = temp + mapFeature.get(i) + ", ";
        return missingFeature;
    }

    // Back button
    public void onBackButtonClick(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Function.class.getResource("main-view.fxml"));
        Parent addParent = loader.load();
        Scene addScene = new Scene(addParent);
        stage.setScene(addScene);
    }

    // ComboBox for show options
    public void showOption_comboBox() {
        showOptionComboBox.setItems(showOption_list);
    }

    // ComboBox for job options
    public void job_comboBox() {
        jobComboBox.setItems(job_list);
    }

    // ComboBox for level options
    public void level_comboBox(MouseEvent event) {
        String job = jobComboBox.getValue();
        if (job.equals("Economy")) {
            levelComboBox.setItems(economy_list);
        } else if (job.equals("Develop")) {
            levelComboBox.setItems(develop_list);
        } else {
            Alert empty_job_option = new Alert(Alert.AlertType.ERROR, "Empty job option", ButtonType.OK);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            empty_job_option.setTitle("Empty job option");
            empty_job_option.setContentText("The job is missing!");
            empty_job_option.initModality(Modality.APPLICATION_MODAL);
            empty_job_option.initOwner(stage);
            empty_job_option.showAndWait();
            if (empty_job_option.getResult() == ButtonType.OK) {
                empty_job_option.close();
            }
        }
    }

    private void clear_editLine() {
        edit_line.setText("No edit line...");
        id.clear();
        name.clear();
        age.clear();
        address.clear();
        job.setText("JOB...");
        level.setText("LEVEL...");
    }

    private void first_view() {
        confirmation_stage.setVisible(false);
        check_economy = new HashSet<>();
        check_develop = new HashSet<>();
        check_economy.add("Manager");
        check_economy.add("Analyst");
        check_economy.add("Accountant");
        check_develop.add("Front-end");
        check_develop.add("Back-end");
        check_develop.add("Full-stack");
    }
}
