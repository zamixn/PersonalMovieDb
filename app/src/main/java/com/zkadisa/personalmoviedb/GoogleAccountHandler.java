package com.zkadisa.personalmoviedb;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class GoogleAccountHandler {

    public static GoogleAccountHandler instance;
    private GoogleSignInAccount account;
    private GoogleSignInClient mGoogleSignInClient;

    public GoogleAccountHandler(GoogleSignInAccount account, GoogleSignInClient mGoogleSignInClient) {
        this.account = account;
        this.mGoogleSignInClient = mGoogleSignInClient;
    }

    public static boolean isInitialized(){
        return instance != null;
    }

    public static GoogleSignInAccount getAccount(){
        return  instance.account;
    }

    public static void initialize(GoogleSignInAccount account, GoogleSignInClient mGoogleSignInClient){
        instance = new GoogleAccountHandler(account, mGoogleSignInClient);
    }
}
