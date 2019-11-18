package com.zkadisa.personalmoviedb.UserList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zkadisa.personalmoviedb.AppDatabase;
import com.zkadisa.personalmoviedb.BaseActivityClass;
import com.zkadisa.personalmoviedb.DataHandling.Entry;
import com.zkadisa.personalmoviedb.DataHandling.OMDbReader;
import com.zkadisa.personalmoviedb.DetailsActivity;
import com.zkadisa.personalmoviedb.EntryListAdapter;
import com.zkadisa.personalmoviedb.EntryListItem;
import com.zkadisa.personalmoviedb.MainActivity;
import com.zkadisa.personalmoviedb.Misc.Utilities;
import com.zkadisa.personalmoviedb.R;
import com.zkadisa.personalmoviedb.SearchEntryListItem;
import com.zkadisa.personalmoviedb.SearchForEntriesActivity;

import java.util.ArrayList;
import java.util.List;

public class UserListEditActivity extends BaseActivityClass {

    private Context context = this;
    private Gson gson = new Gson();

    private EditText titleTextView;
    private FloatingActionButton addButton;
    private ListView userListView;
    private List<EntryListItem> items;
    private EntryListAdapter adapter;

    private AppDatabase database;

    private UserList mList;
    private int parentIndex;
    private boolean wasModified = false;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlisteditactivitydesign);

        titleTextView = findViewById(R.id.listTitleView);
        titleTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.i("Toogle", b + "");
                if(b)
                    titleTextView.setText(mList.getTitle());
                else
                    titleTextView.setText(mList.getDisplayTitle());
            }
        });
        titleTextView.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                // the user is done typing.
                                Log.i("DetailsActivity", "Saving title " + titleTextView.getText().toString());
                                if(!titleTextView.getText().toString().equals(mList.getTitle())){
                                    mList.setTitle(titleTextView.getText().toString());
                                    wasModified = true;
                                    database.userListDao().update(mList);
                                }
                                titleTextView.setText(mList.getDisplayTitle());
                                MainActivity.hideSoftKeyboard(UserListEditActivity.this, v);
                                return true; // consume.
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                }
        );
        addButton = findViewById(R.id.addNewUserList);
        userListView = findViewById(R.id.userListView);

        adapter = new EntryListAdapter(this, new ArrayList<EntryListItem>());
        userListView.setAdapter(adapter);
        registerForContextMenu(userListView);

        database = MainActivity.getDatabase();

        Intent intent = getIntent();
        parentIndex = intent.getIntExtra("index", -1);
        mList = (UserList) intent.getSerializableExtra("data");
        updateTitle();
        items = new ArrayList<>();
        for (int i = 0; i < mList.getIdList().size(); i++){
            Ion.with(context)
                    .load(OMDbReader.GetSearchByIDURL(mList.getIdList().get(i)))
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if(e != null){
                                Log.e("UserListAdapter.java", e.getMessage());
                                return;
                            }

                            Entry entry = gson.fromJson(result.toString(), Entry.class);
                            EntryListItem item = new EntryListItem(entry);
                            items.add(item);
                            adapter.add(item);
                            adapter.notifyDataSetChanged();
                        }
                    });
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SearchForEntriesActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("data", adapter.getItem(i));
                context.startActivity(intent);
            }
        });
    }

    private void updateTitle(){
        titleTextView.setText(mList.getDisplayTitle());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.userlisteditoptionsmenu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        long itemID = info.position;
//        menu.setHeaderTitle("item: " + itemID);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        long itemID = info.position;
        switch (item.getItemId()){
            case R.id.userlist_remove:
                EntryListItem entryItem = items.get((int)itemID);
                mList.idList.remove(entryItem.getImdbID());
                adapter.remove(entryItem);
                adapter.notifyDataSetChanged();
                database.userListDao().update(mList);
                wasModified = true;
                updateTitle();
                break;
        }

        return super.onContextItemSelected(item);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                SearchEntryListItem se = (SearchEntryListItem) data.getSerializableExtra("data");
                if(!mList.containsId(se.getImdbID())) {
                    mList.addId(se.getImdbID());
                    database.userListDao().update(mList);
                    adapter.add(new EntryListItem(se));
                    adapter.notifyDataSetChanged();
                    wasModified = true;
                    updateTitle();
                }
                else{
                    Utilities.ShowCustomToast(context, "Already added");
                }
            }
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("index", parentIndex);
        intent.putExtra("modified", wasModified);
        intent.putExtra("data", mList);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}