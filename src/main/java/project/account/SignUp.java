package project.account;

import utilities.ConstVariables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class SignUp {
    // Variables
    private final String user_account;
    private final String user_password;
    private final String avatar;
    private final String user_nickname;
    private String privacyCode;
    private final String[] special_chars = {"!", "@", "#", "$", "%", "&"};
    private final String[] alphabets = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    Set<Integer> set;

    public SignUp(String user_account, String user_password, String avatar, String user_nickname) {
        this.user_account = user_account;
        this.user_password = user_password;
        this.avatar = avatar;
        this.user_nickname = user_nickname;
    }

    /**
     * Check stages
     */
    // Checking the input data from user
    public String startChecking() {
        String temp;
        int a = checkSimilarAccountName();
        int b = checkPassword();
        int result = a + b;
        if (result == 1 && a == 1 && b == 0) {
            // Situation 1: Account error -> The username is already in use
            temp = "accountError";
        } else if (result == 1 && b == 1 && a == 0) {
            // Situation 2: Password error -> Missing special requirements in password
            temp = "passwordError";
        } else if (result == 2 && a == 1 && b == 1) {
            // Situation 3: Account and password error -> Missing account input and special requirements in password
            temp = "apError";
        } else {
            // Situation 4: Successfully sign-up
            temp = "continue";
        }
        return temp;
    }

    public int checkSimilarAccountName() {
        int check = 0;
        try {
            if (checkFileExist()) {
                BufferedReader reader = new BufferedReader(new FileReader(ConstVariables.LIST_ACCOUNTS_PATH));
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    String[] array = currentLine.split(",");
                    if (user_account.equals(array[0])) {
                        check = 1;
                        return check;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return check;
    }

    public int checkPassword() {
        char[] chars = user_password.toCharArray();
        for (char temp : chars) {
            if (Character.isDigit(temp)) {
                return 0;
            }
        }
        return 1;
    }

    // Generate the privacy number
    public int getPrivacyNumber(String file) throws IOException {
        while (true) {
            int num = (int) (Math.random() * (10000 - 1000)) + 1000;
            if (!privacyCode_list(file, num)) {
                return num;
            }
        }
    }

    public boolean privacyCode_list(String file, int num) throws IOException {
        if (set == null) {
            return false;
        } else {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] array = currentLine.split(",");
                String temp = array[2].substring(1, array[2].length() - 1);
                set.add(Integer.parseInt(temp));
            }
            return set.contains(num);
        }
    }

    // Generate the privacy character;
    public String getPrivacyChars() {
        return special_chars[(int) (Math.random() * special_chars.length)];
    }

    public String getPrivacyAlphabets() {
        return alphabets[(int) (Math.random() * alphabets.length)];
    }

    // Add user account data in the file "accounts.data"
    public String addUserAccount() {
        FileWriter myList;
        try {
            if (checkFileExist()) {
                myList = new FileWriter(ConstVariables.LIST_ACCOUNTS_PATH, true);
            } else {
                myList = new FileWriter(ConstVariables.LIST_ACCOUNTS_PATH);
            }
            privacyCode = getPrivacyAlphabets() + getPrivacyNumber(ConstVariables.LIST_ACCOUNTS_PATH) + getPrivacyChars();
            myList.write(this + "\n");
            myList.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return privacyCode;
    }

    private boolean checkFileExist() {
        File file = new File(ConstVariables.LIST_ACCOUNTS_PATH);
        return file.exists();
    }

    public String toString() {
        return user_account + "," + user_password + "," + privacyCode + "," + avatar + "," + user_nickname;
    }
}
