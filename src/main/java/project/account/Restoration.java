package project.account;

import lombok.extern.slf4j.Slf4j;
import utilities.ConstVariables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class Restoration {
    // Variables
    private final String user_account;
    private final String user_code;
    private String user_password;

    public Restoration(String user_account, String user_code) {
        this.user_account = user_account;
        this.user_code = user_code;
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
                BufferedReader reader = new BufferedReader(new FileReader(file));
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
            } catch (IOException e) {
                log.info(e.getMessage());
                log.info("<Restoration - checkUser>: File of admin accounts is not found.");
            }
        }
        return check;
    }

    public String result() {
        return user_password;
    }
}
