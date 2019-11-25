package com.zkadisa.personalmoviedb;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.koushikdutta.ion.Ion;
import com.zkadisa.personalmoviedb.Misc.Utilities;

import java.util.List;

public class AccountActivity extends BaseActivityClass {

    private Context context = this;

    public static final int CONNECTION_TYPE_AUTOLOGIN = 0;
    public static final int CONNECTION_TYPE_CHEKCPROFILE = 1;

    private final int GOOGLE_SIGN_IN_RESULT_CODE = 0;
    private final int GOOGLE_SIGN_IN_REQUEST_AUTHORIZATION_CODE = 1;

    private ImageView accountPicture;
    private TextView accountName;
    private TextView informationText;
    private TextView explanationText;
    public Button loginButton;
    public Button logoutButton;
    public Button retryLoginButton;
    public Button doNotConnectButton;

    private int connectionType = -1;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountactivitydesign);

        accountPicture = findViewById(R.id.accountPicture);
        accountName = findViewById(R.id.accountName);
        informationText = findViewById(R.id.informationText);
        informationText.setText(getResources().getString(R.string.accountInformation_connecting));
        informationText.setVisibility(View.GONE);
        explanationText = findViewById(R.id.explanationText);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckLogin();
                view.setVisibility(View.GONE);
                informationText.setVisibility(View.VISIBLE);
                explanationText.setVisibility(View.GONE);
            }
        });
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOutOfGoogleServices();
                logoutButton.setVisibility(View.GONE);
            }
        });
        logoutButton.setVisibility(View.GONE);
        retryLoginButton = findViewById(R.id.retryLogin);
        retryLoginButton.setVisibility(View.GONE);
        retryLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInToGoogleServices();
                view.setVisibility(View.GONE);
                doNotConnectButton.setVisibility(View.GONE);
            }
        });
        doNotConnectButton = findViewById(R.id.doNotConnectButton);
        doNotConnectButton.setVisibility(View.GONE);
        doNotConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        connectionType = intent.getIntExtra("connectionType", -1);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(AccountActivity.this);
        if(account != null){
            GetGoogleSignInClient();
            if(connectionType == CONNECTION_TYPE_CHEKCPROFILE){
                UpdateAccountInformation(account);
            }
        }
    }

    private void CheckLogin(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(AccountActivity.this);
        if(account == null)
            SignInToGoogleServices();
        else if(connectionType == CONNECTION_TYPE_AUTOLOGIN) {
            InitiateHelpers(account);
            MainActivity.LoadDatabase();
            finish();
        }else
            UpdateAccountInformation(account);
    }

    private void GetGoogleSignInClient(){
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()//request email id
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void SignInToGoogleServices(){
        GetGoogleSignInClient();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_RESULT_CODE);
    }

    private void SignOutOfGoogleServices(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Utilities.ShowCustomToast(context, "Google Sign Out done.");
                        UninitiateHelpers();
                        revokeAccess();

                    }
                });
    }
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Utilities.ShowCustomToast(context, "Google access revoked.");
                        UpdateAccountInformation(null);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_RESULT_CODE) {
            if(resultCode == RESULT_OK) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                InitiateHelpers(task.getResult());
                MainActivity.LoadDatabase();

                if(connectionType == CONNECTION_TYPE_AUTOLOGIN){
                    finish();
                }else{
                    HandleSignInResult(task);
                }
            }else{
                retryLoginButton.setVisibility(View.VISIBLE);
                doNotConnectButton.setVisibility(View.VISIBLE);
                informationText.setText(getResources().getString(R.string.accountInformation_failedToConnect));
            }
        }
    }

    private void InitiateHelpers(GoogleSignInAccount account){
        if(!GoogleAccountHandler.isInitialized())
            GoogleAccountHandler.initialize(account, mGoogleSignInClient);
        if(!DriveServiceHelper.isInitialized())
            DriveServiceHelper.initialize(context, account);
    }
    private void UninitiateHelpers(){
        if(GoogleAccountHandler.isInitialized())
            GoogleAccountHandler.uninitialize();
        if(DriveServiceHelper.isInitialized())
            DriveServiceHelper.uninitialize();
    }

    private void HandleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            DriveServiceHelper.instance.queryFiles().addOnCompleteListener(new OnCompleteListener<FileList>() {
                @Override
                public void onComplete(@NonNull Task<FileList> task) {
                    if(task.getException() instanceof UserRecoverableAuthIOException){
                        UserRecoverableAuthIOException e = (UserRecoverableAuthIOException)task.getException();
                        startActivityForResult(e.getIntent(), GOOGLE_SIGN_IN_REQUEST_AUTHORIZATION_CODE);
                    }
                    List<File> files = task.getResult().getFiles();
                }
            });
//            DriveServiceHelper.instance.readFile("datadb").addOnCompleteListener(new OnCompleteListener<Pair<String, String>>() {
//                @Override
//                public void onComplete(@NonNull Task<Pair<String, String>> task) {
//                    if(task.getException() instanceof UserRecoverableAuthIOException){
//                        UserRecoverableAuthIOException e = (UserRecoverableAuthIOException)task.getException();
//                        startActivityForResult(e.getIntent(), GOOGLE_SIGN_IN_REQUEST_AUTHORIZATION_CODE);
//                    }
//                }
//            });

            UpdateAccountInformation(account);
            Utilities.ShowCustomToast(this, "Google Sign In Successful.");
        } catch (ApiException e) {
            Log.e("MAINCLASS", "signInResult:failed code=" + e.getStatusCode());
            Utilities.ShowCustomToast(this, "Failed to do Sign In : " + e.getStatusCode());
            UpdateAccountInformation(null);
        }
    }

    private void UpdateAccountInformation(GoogleSignInAccount acct){
        if (acct != null) {
//            InitiateHelpers(acct);

            informationText.setText("");
            explanationText.setText("");
            loginButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);

            String personName = acct.getDisplayName();
            Uri personPhoto = acct.getPhotoUrl();

            accountName.setText(personName);
            Ion.with(accountPicture)
                    .load(personPhoto.toString());
        }else{
            informationText.setText("");
            explanationText.setText(getResources().getString(R.string.accountInformation_reasonToConnect));
            explanationText.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);

            accountName.setText("");
            accountPicture.setImageResource(android.R.color.transparent);
        }
    }
}
