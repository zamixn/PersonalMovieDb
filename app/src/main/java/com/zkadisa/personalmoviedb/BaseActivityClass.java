package com.zkadisa.personalmoviedb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.zkadisa.personalmoviedb.L1.FirstActivity;
import com.zkadisa.personalmoviedb.Misc.Utilities;
import com.zkadisa.personalmoviedb.UserList.UserListActivity;

public class BaseActivityClass extends AppCompatActivity {


    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
//        actionBar.setLogo(R.drawable.pmdb_logo);
//        actionBar.setDisplayUseLogoEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Utilities.Vibrate(context);
        switch (item.getItemId()) {
            case R.id.homeAction: {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                return true;
            }
            case R.id.listAction: {
                Intent intent = new Intent(context, UserListActivity.class);
                context.startActivity(intent);
                return true;
            }
            case R.id.lab1Action: {
                Intent intent = new Intent(context, FirstActivity.class);
                context.startActivity(intent);
                return true;
            }
            case R.id.lab2Action:{
                Intent intent = new Intent(context, com.zkadisa.personalmoviedb.L2.MainActivity.class);
                context.startActivity(intent);
                return true;
            }
            case R.id.lab3Action:
                Utilities.ShowCustomToast(context, "Nothing to show :)");
                return  true;

        }
        return super.onOptionsItemSelected(item);
    }
}
