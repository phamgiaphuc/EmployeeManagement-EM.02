package application.employeemanagement.function;

import application.employeemanagement.Control;
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
import project.employee.Engineer;
import project.employee.Worker;
import project.employee.base.BaseService;
import project.employee.engineer.EngineerService;
import project.employee.worker.WorkerService;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Add extends BaseService implements Initializable {
    @FXML
    private TextField ID;
    @FXML
    private TextField NAME;
    @FXML
    private TextField AGE;
    @FXML
    private TextField ADDRESS;
    @FXML
    private Label INFO;
    @FXML
    private Label ID_ADVICE;
    @FXML
    private ComboBox<String> jobComboBox;
    @FXML
    private ComboBox<String> levelComboBox;
    @FXML
    private TableView<Employee> table;
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
    private Pane confirmation_userInfo;

    // Variables
    private String employee_ID;
    private String employee_NAME;
    private String employee_AGE;
    private String employee_ADDRESS;
    private String employee_JOB;
    private String employee_LEVEL;
    private String DATA;
    private String MISSING_DATA;

    // ObservableList
    ObservableList<String> job_list = FXCollections.observableArrayList("Economy", "Develop");
    ObservableList<String> economy_list = FXCollections.observableArrayList("Manager", "Analyst", "Accountant");
    ObservableList<String> develop_list = FXCollections.observableArrayList("Front-end", "Back-end", "Full-stack");
    ObservableList<Employee> addList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirmation_userInfo.setVisible(false);
    }

    /**
     * Functions
     */
    // STAGE 1: Filling employee
    // Suggesting ID button: Suggest an ID for the user
    public void idSuggestion() {
        try {
            int suggestID = suggestId();
            ID.setText(String.valueOf(suggestID));
            idAdviceOff();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getting input information from the user
    private String getEmployeeInfo() {
        employee_ID = checkInfo(ID.getText().trim());
        employee_NAME = checkInfo(NAME.getText().trim());
        employee_AGE = checkInfo(AGE.getText().trim());
        employee_ADDRESS = checkInfo(ADDRESS.getText().trim());
        employee_JOB = jobComboBox.getValue();
        employee_LEVEL = levelComboBox.getValue();
        return employee_ID + "@" + employee_NAME + "@" + employee_AGE + "@" + employee_ADDRESS + "@" + employee_JOB + "@" + employee_LEVEL;
    }

    // Checking if ID or AGE is an integer
    private int checkIfIsInteger(MouseEvent event) {
        if (checkInteger(employee_ID) == -1) {
            errorAdd("Wrong information error", "Please type the right integer/number for ID", event);
            ID.clear();
            ID.setPromptText("Enter the ID");
            return -1;
        }
        if (checkInteger(employee_AGE) == -1) {
            errorAdd("Wrong information error", "Please type the right integer/number for AGE", event);
            AGE.clear();
            AGE.setPromptText("Enter the AGE");
            return -1;
        } else {
            if (checkAgeRange(employee_AGE) == -1) {
                errorAdd("Invalid Age Error", "Please type the valid AGE", event);
                AGE.clear();
                AGE.setPromptText("Enter the AGE");
                return -1;
            }
        }
        return 0;
    }

    // Checking if the ID is already had in the list - if it is true, alert the user to input a new ID again.
    public int checkIDTextField(MouseEvent event) {
        int result = checkId(employee_ID);
        if (result == -1) {
            errorAdd("Duplicated ID error", "This ID " + employee_ID + " is already had. Please try another id", event);
            ID.clear();
            ID.setPromptText("Enter the ID");
            idAdviceOn();
            return -1;
        }
        return 0;
    }

    // Checking if theres is a string "null" in the final output of user's information
    public String checkEmptyText() {
        String isEmpty = "true";
        MISSING_DATA = "";
        String[] checkEmptyText = DATA.split("@");
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

    // Display a table of user information
    private void tableAddView() {
        addList = FXCollections.observableArrayList(new Employee(Integer.parseInt(employee_ID), employee_NAME, Integer.parseInt(employee_AGE), employee_ADDRESS, employee_JOB, employee_LEVEL));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        jobColumn.setCellValueFactory(new PropertyValueFactory<>("job"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        table.setItems(addList);
    }

    // Alert
    private void errorAdd(String error_title, String error_text, MouseEvent event) {
        Alert error_add = new Alert(Alert.AlertType.ERROR, error_title, ButtonType.OK);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        error_add.setTitle(error_title + "!");
        error_add.setContentText(error_text);
        Image image = new Image("https://upload.wikimedia.org/wikipedia/commons/e/ec/Error-icon.png");
        ImageView imageView = new ImageView(image);
        error_add.setGraphic(imageView);
        error_add.initModality(Modality.APPLICATION_MODAL);
        error_add.initOwner(stage);
        error_add.showAndWait();
    }

    private void idAdviceOn() {
        ID_ADVICE.setText("Recommend using suggesting ID button!");
    }

    public void idAdviceOff() {
        ID_ADVICE.setText("Must be a number!");
    }

    // Confirm button
    public void onConfirmButtonClick(MouseEvent event) {
        DATA = getEmployeeInfo();
        System.out.println(DATA);
        if (checkEmptyText().equals("true")) {
            if (checkIfIsInteger(event) == 0) {
                if (checkIDTextField(event) == 0) {
                    confirmation_userInfo.setVisible(true);
                    tableAddView();
                    INFO.setText(employee_ID + " - " + employee_NAME + " - " + employee_AGE + " - " + employee_ADDRESS + " - " + employee_JOB + " - " + employee_LEVEL);
                }
            }
        } else {
            errorAdd("Empty information error", MISSING_DATA + " is empty. Please fill properly", event);
        }
    }

    // STAGE 2: Confirm and add employee information to the data
    // Add button
    public void onAddButtonClick(MouseEvent event) throws IOException {
        Alert final_confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Notification", ButtonType.YES, ButtonType.CANCEL);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        final_confirmation.setContentText("Are you sure you want to add this employee?");
        final_confirmation.initModality(Modality.APPLICATION_MODAL);
        final_confirmation.initOwner(stage);
        final_confirmation.showAndWait();
        if (final_confirmation.getResult() == ButtonType.CANCEL) {
            final_confirmation.close();
        } else {
            addInfo();
            Alert add_successfully = new Alert(Alert.AlertType.CONFIRMATION, "Notification", ButtonType.OK);
            add_successfully.setContentText("Adding new employee successfully!");
            add_successfully.initModality(Modality.APPLICATION_MODAL);
            add_successfully.initOwner(stage);
            add_successfully.showAndWait();
            if (add_successfully.getResult() == ButtonType.OK) {
                FXMLLoader loader = new FXMLLoader(Control.class.getResource("add-view.fxml"));
                Parent addParent = loader.load();
                Scene addScene = new Scene(addParent);
                stage.setScene(addScene);
            }
        }
    }

    private void addInfo() {
        if (employee_JOB.equals("Worker")) {
            WorkerService workerService = new WorkerService();
            Worker worker = new Worker(Integer.parseInt(employee_ID), employee_NAME, Integer.parseInt(employee_AGE), employee_ADDRESS, employee_JOB, employee_LEVEL);
            workerService.addWorkerToFile(worker);
        } else {
            EngineerService engineerService = new EngineerService();
            Engineer engineer = new Engineer(Integer.parseInt(employee_ID), employee_NAME, Integer.parseInt(employee_AGE), employee_ADDRESS, employee_JOB, employee_LEVEL);
            engineerService.addEngineerToFile(engineer);
        }
    }

    // ADDITIONAL FEATURES
    // Back button
    public void onBackButtonClick(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Function.class.getResource("admin-view.fxml"));
        Parent addParent = loader.load();
        Scene addScene = new Scene(addParent);
        stage.setScene(addScene);
    }

    // ComboBox
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
}
