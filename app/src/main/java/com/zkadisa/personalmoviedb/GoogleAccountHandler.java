package com.zkadisa.personalmoviedb;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class GoogleAccountHandler {

    public static GoogleAccountHandler instance;
    private GoogleSignInAccount account;

    public GoogleAccountHandler(GoogleSignInAccount account) {
        this.account = account;
    }

    public static boolean isInitialized(){
        return instance != null;
    }

    public static GoogleSignInAccount getAccount(){
        return  instance.account;
    }

    public static void initialize(GoogleSignInAccount account){
        instance = new GoogleAccountHandler(account);
    }
}
