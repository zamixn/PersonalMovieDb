package com.zkadisa.personalmoviedb.UserList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zkadisa.personalmoviedb.BaseActivityClass;
import com.zkadisa.personalmoviedb.R;
import com.zkadisa.personalmoviedb.SearchEntryListItem;

import java.util.ArrayList;

public class UserListActivity extends BaseActivityClass {

    private Context context = this;

    private FloatingActionButton addButton;
    private ListView userListView;
    private UserListAdapter adapter;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlistactivitydesign);

        addButton = findViewById(R.id.addNewUserList);
        userListView = findViewById(R.id.userListView);

        adapter = new UserListAdapter(this, new ArrayList<UserList>());
        userListView.setAdapter(adapter);

        adapter.add(new UserList("List1"));
        adapter.add(new UserList("List2"));
        adapter.notifyDataSetChanged();


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, NewUserListActivity.class);
                startActivityForResult(i, 1);
            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        userListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.remove(adapter.getItem(i));
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                UserList list = (UserList) data.getSerializableExtra("data");
                adapter.add(list);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
