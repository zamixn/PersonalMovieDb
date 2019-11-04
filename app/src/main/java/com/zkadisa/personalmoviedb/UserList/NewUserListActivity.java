package com.zkadisa.personalmoviedb.UserList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zkadisa.personalmoviedb.BaseActivityClass;
import com.zkadisa.personalmoviedb.Misc.Utilities;
import com.zkadisa.personalmoviedb.R;

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
                    UserList newList = new UserList(listTitleView.getText().toString());

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
