package com.zkadisa.personalmoviedb.UserList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zkadisa.personalmoviedb.AppDatabase;
import com.zkadisa.personalmoviedb.BaseActivityClass;
import com.zkadisa.personalmoviedb.MainActivity;
import com.zkadisa.personalmoviedb.SearchActivity;
import com.zkadisa.personalmoviedb.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserListActivity extends BaseActivityClass {

    private Context context = this;

    private FloatingActionButton addButton;
    private ListView userListView;
    private UserListAdapter adapter;

    private AppDatabase database;

    private List<UserList> lists;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlistactivitydesign);

        addButton = findViewById(R.id.addNewUserList);
        userListView = findViewById(R.id.userListView);

        adapter = new UserListAdapter(this, new ArrayList<UserList>());
        userListView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewUserListActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserList list = adapter.getItem(i);
                Intent intent = new Intent(context, UserListEditActivity.class);
                intent.putExtra("index", i);
                intent.putExtra("data", list);
                startActivityForResult(intent, 2);
            }
        });
        registerForContextMenu(userListView);

        database = MainActivity.getDatabase();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        adapter.clear();
        lists = database.userListDao().getAllUserLists();
        Collections.sort(lists,
                new Comparator<UserList>() {
                    @Override
                    public int compare(UserList a, UserList b) {
                        return  a.getDate().compareTo(b.getDate());
                    }
                });
        for(UserList l : lists)
            adapter.add(l);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        MainActivity.SaveDatabase();
        super.onStop();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.userlistoptionsmenu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        long itemID = info.position;
//        menu.setHeaderTitle("item: " + itemID);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        long itemID = info.position;
        Log.i("uindex: ", (int)itemID + " " + lists.size());
        UserList list = lists.get((int)itemID);
        switch (item.getItemId()){
            case R.id.userlist_remove:
                lists.remove((int)itemID);
                database.userListDao().delete(list);
                adapter = new UserListAdapter(context, lists);
                adapter.notifyDataSetChanged();
                userListView.setAdapter(adapter);
//                adapter.remove(list);
//                adapter.notifyDataSetChanged();
                break;
        }

        return super.onContextItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("r:", requestCode + "");
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                UserList list = (UserList) data.getSerializableExtra("data");
                database.userListDao().insert(list);
                lists.add(list);
                adapter.add(list);
                adapter.notifyDataSetChanged();
            }
        }else if (requestCode == 2) {
            if(resultCode == RESULT_OK) {
                Log.i("on result", "result");
                int index = data.getIntExtra("index", -1);
                boolean wasModified = data.getBooleanExtra("modified", false);
                if(wasModified) {
                    UserList list = (UserList) data.getSerializableExtra("data");
                    lists.set(index, list);
                    Log.i("resetting list:", lists.get(index).getDisplayTitle());
                    adapter = new UserListAdapter(context, lists);
                    adapter.notifyDataSetChanged();
                    userListView.setAdapter(adapter);

                }
            }
        }
    }
}
