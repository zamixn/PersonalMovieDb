package com.zkadisa.personalmoviedb;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class UserListActivity extends AppCompatActivity {

    private Context context = this;

    private Gson gson = new Gson();

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlistactivitydesign);


    }
}
