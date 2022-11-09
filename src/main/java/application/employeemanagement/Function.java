package application.employeemanagement;

import javafx.application.Platform;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.account.Account;
import project.employee.Employee;
import utilities.ConstVariables;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class Function implements Initializable {
    // Variables
    private String account;
    private String password;
    private String privacyCode;
    private String avatar;
    private String nickname;
    private volatile boolean stop = false;

    @FXML
    private ImageView avatar_imgV;
    @FXML
    private Label admin_01;
    @FXML
    private Label admin_02;
    @FXML
    private Label admin_03;
    @FXML
    private Label realTimeClock;
    @FXML
    private TextField searching_text;
    @FXML
    private TableView<Employee> realtimeDataTable;
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
    ObservableList<Employee> dataList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getInfo();
        setInfo();
        setRealTimeClock();
        show_employeeData();
    }

    /**
     * Set basic admin information when they enter
     */
    // Get info
    private void getInfo() {
        account = Account.getAccount();
        password = Account.getPassword();
        privacyCode = Account.getPrivacyCode();
        avatar = Account.getAvatar();
        nickname = Account.getNickName();
    }

    // Set info
    private void setInfo() {
        avatar_imgV.setImage(new Image(Paths.get(avatar).toUri().toString()));
        admin_01.setText(nickname);
        admin_02.setText("Welcome back, admin: " + nickname);
        admin_03.setText("I am A-bot. Ready to help!");
    }

    // Acus bot = A-bot
    public void miniBot(MouseEvent event) {

    }

    // Real time clock
    public void setRealTimeClock() {
        Thread thread = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            while (!stop) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final String time_now = sdf.format(new Date());
                Platform.runLater(() -> realTimeClock.setText(time_now));
            }
        });
        thread.start();
    }

    /**
     * Search bar
     */
    // Searching button
    public void onSearchingButtonClick(MouseEvent event) {
        String text = searching_text.getText().trim();
        switch (text.toLowerCase()) {
            case "" -> missing_searchingText(event);
            case "add" -> onAddButtonClick(event);
            default -> invalid_searchingText(event);
        }
    }

    // Clear button
    public void onClearingSearchingText() {
        searching_text.clear();
    }

    // Missing searching option
    private void missing_searchingText(MouseEvent event) {
        Alert missingText = new Alert(Alert.AlertType.ERROR, "Empty searching option", ButtonType.OK);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        missingText.setTitle("Empty searching option");
        missingText.setContentText("The searching option is empty!");
        missingText.initModality(Modality.APPLICATION_MODAL);
        missingText.initOwner(stage);
        missingText.showAndWait();
        if (missingText.getResult() == ButtonType.OK) {
            missingText.close();
        }
    }

    // Invalid searching option
    private void invalid_searchingText(MouseEvent event) {
        Alert invalidText = new Alert(Alert.AlertType.ERROR, "Invalid searching option", ButtonType.OK);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        invalidText.setTitle("Invalid searching option");
        invalidText.setContentText("The searching option is invalid!");
        invalidText.initModality(Modality.APPLICATION_MODAL);
        invalidText.initOwner(stage);
        invalidText.showAndWait();
        if (invalidText.getResult() == ButtonType.OK) {
            invalidText.close();
        }
    }

    /**
     * Table view
     */
    public void show_employeeData() {
        try {
            realtimeDataTable.refresh();
            BufferedReader reader = new BufferedReader(new FileReader(ConstVariables.EMPLOYEE_DATA_PATH));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] data = currentLine.split("@");
                showTableView(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showTableView(String[] data) {
        dataList.add(new Employee(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2]), data[3], data[4], data[5]));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        jobColumn.setCellValueFactory(new PropertyValueFactory<>("job"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        realtimeDataTable.setItems(dataList);
    }

    /**
     * Functions:
     */
    // Dashboard
    public void onDashboardClick(MouseEvent event) {

    }

    // Signing out
    public void onSignOutClick(MouseEvent event) throws IOException {
        Alert signOutConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Sign-out confirmation", ButtonType.CANCEL, ButtonType.OK);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        signOutConfirmation.setTitle("Signing-out confirmation");
        signOutConfirmation.setContentText("Do you want to sign-out?");
        signOutConfirmation.initModality(Modality.APPLICATION_MODAL);
        signOutConfirmation.initOwner(stage);
        signOutConfirmation.showAndWait();
        if (signOutConfirmation.getResult() == ButtonType.OK) {
            stop = true;
            FXMLLoader loader = new FXMLLoader(Control.class.getResource("welcome-view.fxml"));
            Parent addParent = loader.load();
            Scene addScene = new Scene(addParent);
            stage.setScene(addScene);
        }
    }

    // Add
    public void onAddButtonClick(MouseEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(Function.class.getResource("add-view.fxml"));
            Parent addParent = loader.load();
            Scene addScene = new Scene(addParent);
            stage.setScene(addScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
