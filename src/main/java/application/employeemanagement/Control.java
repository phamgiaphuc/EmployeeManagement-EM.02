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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.account.Restoration;
import project.account.SignIn;
import project.account.SignUp;
import utilities.ConstVariables;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Control implements Initializable {
    // Option stage = Menu
    @FXML
    private Label choose_option;
    // Sign-in stage
    @FXML
    private Pane signIn_pane;
    @FXML
    private TextField account;
    @FXML
    private PasswordField password;
    @FXML
    private TextField pass_0;
    @FXML
    private CheckBox revealPass_signIn;
    // Sign-up stage
    @FXML
    private Pane signUp_pane;
    @FXML
    private TextField user_account;
    @FXML
    private PasswordField user_password;
    @FXML
    private PasswordField reEnter_password;
    @FXML
    private TextField pass_1;
    @FXML
    private TextField pass_2;
    @FXML
    private CheckBox revealPass_signUp;
    @FXML
    private TextField user_nickname;
    @FXML
    private ImageView avatar;
    private File avatar_path;
    // Account info stage
    @FXML
    private Pane user_info;
    @FXML
    private Label account_info;
    @FXML
    private Label password_info;
    @FXML
    private Label privacyCode_info;
    @FXML
    private ImageView avatar_image;
    @FXML
    private Label nickName;
    // Restore password stage
    @FXML
    private Pane restorePass_pane;
    @FXML
    private TextField restorePass_account;
    @FXML
    private TextField restorePass_code;
    @FXML
    private Label password_after_restore;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signIn_pane.setVisible(false);
        signUp_pane.setVisible(false);
        user_info.setVisible(false);
        restorePass_pane.setVisible(false);
        choose_option.setVisible(true);
    }

    /**
     * Functions:
     */
    // 1.Exit stage
    public void onExitClick(MouseEvent event) {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Exit confirmation", ButtonType.CANCEL, ButtonType.OK);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        exitAlert.setTitle("Exit Stage");
        exitAlert.setContentText("Are you sure you want to exit?");
        String exit_icon_path = ConstVariables.EXIT_ICON_PATH;
        Image image = new Image(Paths.get(exit_icon_path).toUri().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        exitAlert.setGraphic(imageView);
        exitAlert.initModality(Modality.APPLICATION_MODAL);
        exitAlert.initOwner(stage);
        exitAlert.showAndWait();
        if (exitAlert.getResult() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        } else {
            exitAlert.close();
        }
    }

    // 2.Sign-in stage
    public void onSignInClick() {
        if (revealPass_signIn.isSelected()) {
            revealPass_signIn.setSelected(false);
            password.setVisible(true);
        }
        pass_0.setVisible(false);
        choose_option.setVisible(false);
        signUp_pane.setVisible(false);
        user_info.setVisible(false);
        restorePass_pane.setVisible(false);
        signIn_pane.setVisible(true);
        account.setText("");
        password.setText("");
    }

    public void signingIn(MouseEvent event) throws IOException {
        if (account.getText().equals("") || password.getText().equals("")) {
            errorSituation("Signing in error", "Make sure you have filled in everything correctly!", event);
        } else {
            if (revealPass_signIn.isSelected()) {
                password.setText(pass_0.getText());
            }
            SignIn signIn = new SignIn(account.getText(), password.getText());
            int check = signIn.checkUser();
            switch (check) {
                case 1 -> {
                    // Situation 1: Logged in successfully
                    System.out.println("Account " + account.getText() + " sign-in successfully!");
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(Control.class.getResource("main-view.fxml"));
                    Parent addParent = loader.load();
                    Scene addScene = new Scene(addParent);
                    stage.setScene(addScene);
                }
                case 2 -> // Situation 2: Wrong password
                        errorSituation("Password error", "Invalid password. Make sure your password is correct!", event);
                case 3 -> // Situation 3: Invalid account
                        errorSituation("Invalid account error", "Your account is not existed. Please check again!", event);
                case 0 -> // Situation 4: Empty account
                        emptyAccountSituation(event);
            }
        }
    }

    // Reveal password in sign-in stage
    public void revealPassword_signIn() {
        if (revealPass_signIn.isSelected()) {
            pass_0.setVisible(true);
            pass_0.setText(password.getText());
            password.setVisible(false);
        } else {
            password.setVisible(true);
            password.setText(pass_0.getText());
            pass_0.setVisible(false);
        }
    }

    // Go to sign-up page by clicking the "Sign-up" link
    public void onLinkSignUpClick() {
        onSignUpClick();
    }

    // Go to restore password page by clicking the "Click me" link
    public void onLinkForgetPasswordClick() {
        onSignInClick();
        signIn_pane.setVisible(false);
        restorePass_pane.setVisible(true);
        restorePass_account.setText("");
        restorePass_code.setText("");
        password_after_restore.setText("");
    }

    // 3.Restore password stage
    public void onSubmitClick(MouseEvent event) throws IOException {
        if (restorePass_account.getText().equals("") || restorePass_code.getText().equals("")) {
            errorSituation("Submitting error", "Make sure you have filled in everything!", event);
        } else {
            Restoration restoration = new Restoration(restorePass_account.getText(), restorePass_code.getText());
            int check = restoration.checkUser();
            String user_password = restoration.result();
            System.out.println("Account: " + restorePass_account.getText() + " - password: " + user_password);
            switch (check) {
                case 1 -> password_after_restore.setText(user_password);
                case 2 -> errorSituation("Privacy code error", "Invalid privacy code. Make sure your code is correct!", event);
                case 3 -> errorSituation("Invalid account error", "Your account is not existed. Please check again!", event);
                case 0 -> emptyAccountSituation(event);
            }
        }
    }

    // 4.Sign-up stage
    public void onSignUpClick() {
        if (revealPass_signUp.isSelected()) {
            revealPass_signUp.setSelected(false);
            user_password.setVisible(true);
            reEnter_password.setVisible(true);
        }
        choose_option.setVisible(false);
        signIn_pane.setVisible(false);
        user_info.setVisible(false);
        restorePass_pane.setVisible(false);
        signUp_pane.setVisible(true);
        pass_1.setVisible(false);
        pass_1.setText("");
        pass_2.setVisible(false);
        pass_2.setText("");
        user_account.setText("");
        user_password.setText("");
        reEnter_password.setText("");
        user_nickname.setText("");
        avatar.setImage(new Image(Paths.get(ConstVariables.SIGNUP_ICON_PATH).toUri().toString()));
        avatar_path = new File(ConstVariables.SIGNUP_ICON_PATH);
    }

    // When user sign-up an account
    public void signingUpUserAccount(MouseEvent event) {
        if (user_password.getText().equals("") || user_account.getText().equals("") || reEnter_password.getText().equals("") || user_nickname.getText().equals("")) {
            errorSituation("Signing up error", "Make sure you have filled in everything correctly!", event);
        } else {
            if (revealPass_signUp.isSelected()) {
                user_password.setText(pass_1.getText());
                reEnter_password.setText(pass_2.getText());
            }
            if (!user_password.getText().equals(reEnter_password.getText())) {
                errorSituation("Signing up error", "Make sure the passwords are the same!", event);
            } else {
                SignUp signUp = new SignUp(user_account.getText(), user_password.getText(), avatar_path.getAbsolutePath(), user_nickname.getText());
                String check = signUp.startChecking();
                switch (check) {
                    // Situation 1: Account error -> The username is already in use
                    case "accountError" -> errorSituation("Account error", "The username is already in use!", event);
                    // Situation 2: Password error -> Missing special requirements in password
                    case "passwordError" -> errorSituation("Password error", "Make sure your password is correct!", event);
                    // Situation 3: Account and password error -> Missing account input and special requirements in password
                    case "apError" -> errorSituation("Error!", "You need to sign up correctly!", event);
                    // Situation 4: Successfully sign-up
                    case "continue" -> {
                        Alert signingUp = new Alert(Alert.AlertType.CONFIRMATION, "Signing up account", ButtonType.YES, ButtonType.NO);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        signingUp.setTitle("Signing up account");
                        signingUp.setContentText("Are you sure to sign up the account?");
                        signingUp.initModality(Modality.APPLICATION_MODAL);
                        signingUp.initOwner(stage);
                        signingUp.showAndWait();
                        if (signingUp.getResult() == ButtonType.YES) {
                            // Each new account will have a privacy code to get back the password
                            String privacyCode = signUp.addUserAccount();
                            Alert signingUpSuccess = new Alert(Alert.AlertType.CONFIRMATION, "Signing account", ButtonType.OK, ButtonType.CANCEL);
                            signingUpSuccess.setTitle("Signing up account");
                            signingUpSuccess.setContentText("Signing up account successfully");
                            signingUpSuccess.initModality(Modality.APPLICATION_MODAL);
                            signingUpSuccess.initOwner(stage);
                            signingUpSuccess.showAndWait();
                            if (signingUpSuccess.getResult() == ButtonType.OK) {
                                signUp_pane.setVisible(false);
                                userInformation(user_account.getText(), user_password.getText(), avatar_path.getAbsolutePath(), privacyCode, user_nickname.getText());
                            }
                        }
                    }
                }
            }
        }
    }

    // Avatar button
    public void onAvatarButtonClick(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(ConstVariables.AVATAR_FILE_PATH));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        avatar_path = fileChooser.showOpenDialog(stage);
        avatar.setImage(new Image(Paths.get(avatar_path.toString()).toUri().toString()));
    }

    // 5.User information stage
    public void userInformation(String account, String password, String avatar, String privacyCode, String user_nickname) {
        user_info.setVisible(true);
        account_info.setText(account);
        password_info.setText(password);
        avatar_image.setImage(new Image(Paths.get(avatar).toUri().toString()));
        privacyCode_info.setText(privacyCode);
        nickName.setText(user_nickname);
    }

    // Reveal password in sign-up stage
    public void revealPassword_signUp() {
        if (revealPass_signUp.isSelected()) {
            pass_1.setVisible(true);
            pass_2.setVisible(true);
            pass_1.setText(user_password.getText());
            pass_2.setText(reEnter_password.getText());
            user_password.setVisible(false);
            reEnter_password.setVisible(false);
        } else {
            user_password.setVisible(true);
            reEnter_password.setVisible(true);
            user_password.setText(pass_1.getText());
            reEnter_password.setText(pass_2.getText());
            pass_1.setVisible(false);
            pass_2.setVisible(false);
        }
    }

    // 6.Back
    public void onBackButtonClick() {
        choose_option.setVisible(true);
        signUp_pane.setVisible(false);
        signIn_pane.setVisible(false);
    }

    public void onBackButton_restorePass() {
        onSignInClick();
        restorePass_pane.setVisible(false);
    }

    public void onBackButton_userInfo() {
        onSignUpClick();
        user_info.setVisible(false);
    }

    // 7.Situation
    private void errorSituation(String error_title, String error_text, MouseEvent event) {
        Alert errorSituation = new Alert(Alert.AlertType.ERROR, error_title, ButtonType.OK);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        errorSituation.setTitle(error_title + "!");
        errorSituation.setContentText(error_text);
        errorSituation.initModality(Modality.APPLICATION_MODAL);
        errorSituation.initOwner(stage);
        errorSituation.showAndWait();
    }

    private void emptyAccountSituation(MouseEvent event) {
        Alert emptyAccountSituation = new Alert(Alert.AlertType.ERROR, "Empty account error", ButtonType.CANCEL, ButtonType.OK);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        emptyAccountSituation.setTitle("Empty account error" + "!");
        emptyAccountSituation.setContentText("You need to sign up an account first. Press OK to go the the registration stage!");
        emptyAccountSituation.initModality(Modality.APPLICATION_MODAL);
        emptyAccountSituation.initOwner(stage);
        emptyAccountSituation.showAndWait();
        if (emptyAccountSituation.getResult() == ButtonType.OK) {
            onSignUpClick();
        }
    }

    // 8. More Information & Profile
    public void onMoreInfoClick() {
        try {
            URL url = new URL("https://github.com/phamgiaphuc/EmployeeManagement-EM.02");
            Desktop.getDesktop().browse(url.toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void authorProfile() {
        try {
            URL url = new URL("https://github.com/phamgiaphuc");
            Desktop.getDesktop().browse(url.toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}