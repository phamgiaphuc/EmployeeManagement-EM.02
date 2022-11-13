package project.account;

import lombok.extern.slf4j.Slf4j;
import utilities.ConstVariables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class SignIn {
    // Variables
    private final String user_account;
    private final String user_password;

    public SignIn(String user_account, String user_password) {
        this.user_account = user_account;
        this.user_password = user_password;
    }

    /**
     * Check stages
     */
    // Check user account
    public int checkUser() {
        int check = 0;
        File file = new File(ConstVariables.LIST_ACCOUNTS_PATH);
        if (file.length() != 0) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(ConstVariables.LIST_ACCOUNTS_PATH));
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    String[] array = currentLine.split(",");
                    if (array[0].equals(user_account) && array[1].equals(user_password)) {
                        check = 1;
                        Account admin = new Account(array);
                        admin.divideUserInfo();
                        return check;
                    } else if (array[0].equals(user_account)) {
                        check = 2;
                    } else {
                        check = 3;
                    }
                }
                reader.close();
            } catch (IOException e) {
                log.info(e.getMessage());
                log.info("<SignIn - checkUser>: File of admin accounts is not found.");
            }
        }
        return check;
    }
}
