package application.employeemanagement.function;

import application.employeemanagement.Function;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import project.account.Account;
import utilities.ConstVariables;

import java.io.BufferedReader;
import java.io.FileReader;
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

    // Back button
    public void onBackButtonClick(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Function.class.getResource("main-view.fxml"));
        Parent addParent = loader.load();
        Scene addScene = new Scene(addParent);
        stage.setScene(addScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getInfo();
        setInfo();
    }

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
}
