package project.account;

import utilities.ConstVariables;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
    public int checkUser() throws IOException {
        int check = 0;
        String file = ConstVariables.LIST_ACCOUNTS_PATH;
        BufferedReader reader = new BufferedReader(new FileReader(file));
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
        return check;
    }
}
