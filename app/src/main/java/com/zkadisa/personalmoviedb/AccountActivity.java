package com.zkadisa.personalmoviedb;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.koushikdutta.ion.Ion;
import com.zkadisa.personalmoviedb.Misc.Utilities;

import java.util.Collections;
import java.util.List;

public class AccountActivity extends BaseActivityClass {

    private Context context = this;

    private final int GOOGLE_SIGN_IN_RESULT_CODE = 0;
    private final int GOOGLE_SIGN_IN_REQUEST_AUTHORIZATION_CODE = 1;

    private ImageView accountPicture;
    private TextView accountName;
    private TextView informationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountactivitydesign);

        accountPicture = findViewById(R.id.accountPicture);
        accountName = findViewById(R.id.accountName);
        informationText = findViewById(R.id.informationText);
        informationText.setText(getResources().getString(R.string.accountInformation_connecting));

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(AccountActivity.this);
        if(account == null)
            SignInToGoogleServices();
        else
            UpdateAccountInformation(account);
    }

    private void SignInToGoogleServices(){
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()//request email id
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_RESULT_CODE) {
            if(resultCode == RESULT_OK) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                HandleSignInResult(task);
            }else
                informationText.setText(getResources().getString(R.string.accountInformation_failedToConnect));
        }
    }

    private void HandleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if(!GoogleAccountHandler.isInitialized())
                GoogleAccountHandler.initialize(account);
            if(!DriveServiceHelper.isInitialized())
                DriveServiceHelper.initialize(context, account);

            DriveServiceHelper.instance.queryFiles().addOnCompleteListener(new OnCompleteListener<FileList>() {
                @Override
                public void onComplete(@NonNull Task<FileList> task) {
                    if(task.getException() instanceof UserRecoverableAuthIOException){
                        UserRecoverableAuthIOException e = (UserRecoverableAuthIOException)task.getException();
                        startActivityForResult(e.getIntent(), GOOGLE_SIGN_IN_REQUEST_AUTHORIZATION_CODE);
                    }
                    List<File> files = task.getResult().getFiles();
                    for (File f :
                            files) {
                        Log.i("FileFile", f.getName());
                    }
                }
            });

//            driveHelper.createFile();

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
            informationText.setText("");

            String personName = acct.getDisplayName();
            Uri personPhoto = acct.getPhotoUrl();

            accountName.setText(personName);
            Ion.with(accountPicture)
                    .load(personPhoto.toString());
        }
    }
}
