package project.account;

public class Account {
    private final String[] adminInfo;
    private static String admin_account;
    private static String admin_password;
    private static String admin_privacyCode;
    private static String admin_avatar;
    private static String admin_nickname;

    public Account(String[] adminInfo) {
        this.adminInfo = adminInfo;
    }

    public void divideUserInfo() {
        admin_account = adminInfo[0];
        admin_password = adminInfo[1];
        admin_privacyCode = adminInfo[2];
        admin_avatar = adminInfo[3];
        admin_nickname = adminInfo[4];
    }

    public static String getAccount() {
        return admin_account;
    }

    public static String getPassword() {
        return admin_password;
    }

    public static String getPrivacyCode() {
        return admin_privacyCode;
    }

    public static String getAvatar() {
        return admin_avatar;
    }

    public static String getNickName() {
        return admin_nickname;
    }
}
