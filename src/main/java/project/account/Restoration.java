package project.account;

import utilities.ConstVariables;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Restoration {
    // Variables
    private final String user_account;
    private final String user_code;
    private String user_password;

    public Restoration (String user_account, String user_code) {
        this.user_account = user_account;
        this.user_code = user_code;
    }

    /**
     * Check stages
     */
    // Check user account
    public int checkUser() throws IOException {
        int check = 0;
        user_password = "null";
        BufferedReader reader = new BufferedReader(new FileReader(ConstVariables.LIST_ACCOUNTS_PATH));
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            String[] array = currentLine.split(",");
            if (array[0].equals(user_account) && array[2].equals(user_code)) {
                check = 1;
                user_password = array[1];
                return check;
            } else if (array[0].equals(user_account)) {
                check = 2;
            } else {
                check = 3;
            }
        }
        reader.close();
        result();
        return check;
    }

    public String result () {
        return user_password;
    }
}
