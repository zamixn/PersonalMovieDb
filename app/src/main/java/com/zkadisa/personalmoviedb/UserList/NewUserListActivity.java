package com.zkadisa.personalmoviedb.UserList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zkadisa.personalmoviedb.BaseActivityClass;
import com.zkadisa.personalmoviedb.MainActivity;
import com.zkadisa.personalmoviedb.SearchActivity;
import com.zkadisa.personalmoviedb.Misc.Utilities;
import com.zkadisa.personalmoviedb.R;

import java.util.Calendar;
import java.util.Date;

public class NewUserListActivity extends BaseActivityClass {

    private Context context = this;

    private EditText listTitleView;
    private Button saveNewUserList;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newuserlistactivitydesign);

        listTitleView = findViewById(R.id.userListTitle);
        saveNewUserList = findViewById(R.id.saveUserListButton);

        saveNewUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("tag", listTitleView.getText().toString());
                if(!listTitleView.getText().toString().isEmpty()){
                    Date currDate = Calendar.getInstance().getTime();
                    String title = listTitleView.getText().toString();
                    if(MainActivity.getDatabase().userListDao().get(title) != null){
                        Utilities.ShowCustomToast(context, "List with the same title already exists");
                        return;
                    }

                    UserList newList = new UserList(title, currDate);
                    Intent intent = new Intent();
                    intent.putExtra("data", newList);
                    setResult(RESULT_OK, intent);
                    finish();
                }else
                    Utilities.ShowCustomToast(context, "Invalid list title");
            }
        });

    }
}
