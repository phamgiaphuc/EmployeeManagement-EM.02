package application.employeemanagement;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.account.Account;

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
    /**
     * Functions:
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getInfo();
        setInfo();
        setRealTimeClock();
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

    /**
     * Acus bot = A-bot
     */
    public void miniBot(MouseEvent event) {

    }

    /**
     * Real time clock
     */
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
}
