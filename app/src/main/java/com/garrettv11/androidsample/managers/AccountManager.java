package com.garrettv11.androidsample.managers;

/**
 * Created by garrett.kim on 9/15/17.
 */

public class AccountManager {
    private static final AccountManager ourInstance = new AccountManager();

    public static AccountManager getInstance() {
        return ourInstance;
    }

    private AccountManager() {
    }
    //dummy method to provide to the network manager
    public static String getAuthToken() {
        return "";
    }
}
