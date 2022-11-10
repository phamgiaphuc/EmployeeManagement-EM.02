package application.employeemanagement.function;

import application.employeemanagement.Function;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.account.Account;
import utilities.ConstVariables;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Admin implements Initializable {
    // Variables
    private String account;
    private String privacyCode;
    private String old_password;
    private String old_avatar;
    private String old_nickname;
    private File avatar_path;

    @FXML
    private ImageView avatar;
    @FXML
    private TextField nickname;
    @FXML
    private PasswordField original_password;
    @FXML
    private PasswordField new_password_1;
    @FXML
    private PasswordField new_password_2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getInfo();
        setInfo();
    }

    // Set admin info
    private void setInfo() {
        avatar.setImage(new Image(Paths.get(old_avatar).toUri().toString()));
        nickname.setText(old_nickname);
    }

    // Get info
    private void getInfo() {
        account = Account.getAccount();
        privacyCode = Account.getPrivacyCode();
        readAdminInfo();
    }

    // Read admin info
    private void readAdminInfo() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ConstVariables.LIST_ACCOUNTS_PATH));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] data = currentLine.split(",");
                if (data[0].equals(account) && data[2].equals(privacyCode)) {
                    old_password = data[1];
                    old_avatar = data[3];
                    old_nickname = data[4];
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Functions
     */
    // Confirm button
    public void onConfirmButtonClick(MouseEvent event) throws IOException {
        String check_0 = original_password.getText();
        if (check_0.equals(old_password)) {
            String check_1 = new_password_1.getText();
            String check_2 = new_password_2.getText();
            if (check_1.equals(check_2)) {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Notification", ButtonType.YES, ButtonType.NO);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                confirmation.setContentText("Are you sure you want to change?");
                confirmation.initModality(Modality.APPLICATION_MODAL);
                confirmation.initOwner(stage);
                confirmation.showAndWait();
                if (confirmation.getResult() == ButtonType.YES) {
                    change_adminInfo(check_1, avatar_path.getAbsolutePath(), nickname.getText());
                    Alert editSuccess = new Alert(Alert.AlertType.CONFIRMATION, "Notification", ButtonType.OK);
                    editSuccess.setContentText("Change admin information successfully!");
                    editSuccess.initModality(Modality.APPLICATION_MODAL);
                    editSuccess.initOwner(stage);
                    editSuccess.showAndWait();
                    if (editSuccess.getResult() == ButtonType.OK) {
                        onBackButtonClick(event);
                    }
                }
            } else {
                errorAdmin("Please re-enter the new password again", event);
            }
        } else {
            errorAdmin("The old password does not match with this account password", event);
        }
    }

    private void change_adminInfo(String new_password, String new_avatar, String new_nickName) throws IOException {
        File file = new File(ConstVariables.LIST_ACCOUNTS_PATH);
        File temp = new File("temp.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            String[] data = currentLine.split(",");
            if (data[0].equals(account) && data[2].equals(privacyCode)) {
                writer.write(data[0] + "," + new_password + "," + data[2] + "," + new_avatar + "," + new_nickName + "\n");
            } else {
                writer.write(currentLine + "\n");
            }
        }
        writer.close();
        reader.close();
        temp.renameTo(file);
        System.out.println("Change account " + account + " successfully!");
    }

    public void onAvatarButtonClick(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(ConstVariables.AVATAR_FILE_PATH));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        avatar_path = fileChooser.showOpenDialog(stage);
        avatar.setImage(new Image(Paths.get(avatar_path.toString()).toUri().toString()));
    }

    // Alert
    private void errorAdmin(String error_text, MouseEvent event) {
        Alert error_admin = new Alert(Alert.AlertType.ERROR, "Invalid password", ButtonType.OK);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        error_admin.setTitle("Invalid password");
        error_admin.setContentText(error_text + "!");
        Image image = new Image("https://upload.wikimedia.org/wikipedia/commons/e/ec/Error-icon.png");
        ImageView imageView = new ImageView(image);
        error_admin.setGraphic(imageView);
        error_admin.initModality(Modality.APPLICATION_MODAL);
        error_admin.initOwner(stage);
        error_admin.showAndWait();
    }

    // Back button
    public void onBackButtonClick(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Function.class.getResource("main-view.fxml"));
        Parent addParent = loader.load();
        Scene addScene = new Scene(addParent);
        stage.setScene(addScene);
    }
}
