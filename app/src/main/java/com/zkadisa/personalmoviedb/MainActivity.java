package com.zkadisa.personalmoviedb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.room.Room;

import com.zkadisa.personalmoviedb.DataHandling.OMDbReader;
import com.zkadisa.personalmoviedb.Misc.CustomIndicator;
import com.zkadisa.personalmoviedb.Misc.ScrollListView;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends BaseActivityClass {

    private Context context = this;
    private static MainActivity instance;

    private EditText searchQueryEditText;

    private Button searchButton;

    private ScrollListView searchList;
    private SearchResultListAdapter adapter;

    public static CustomIndicator customIndicator;

    private long previousBottomScrollTime = -500;

    private static AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivitydesign);
        instance = this;

        searchQueryEditText = findViewById(R.id.searchPhraseEditText);
        searchButton = findViewById(R.id.search_button);

        searchList = findViewById(R.id.searchEntryListView);
        adapter = new SearchResultListAdapter(this, new ArrayList<SearchEntryListItem>());
        searchList.setAdapter(adapter);

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SearchEntryListItem item = (SearchEntryListItem) searchList.getItemAtPosition(i);
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("data", (Serializable) item);
                context.startActivity(intent);
            }
        });
        searchList.setOnBottomReachedListener(new ScrollListView.OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
                if(OMDbReader.GetPageNumber() < 100 && (System.currentTimeMillis() - previousBottomScrollTime > 500)) {
                    OMDbReader.SearchOMDb(searchQueryEditText.getText().toString(), true, adapter);
                    previousBottomScrollTime = System.currentTimeMillis();
                }
            }
        });

        customIndicator = findViewById(R.id.downloadProgressBar);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                OMDbReader.SearchOMDb(searchQueryEditText.getText().toString(), false, adapter);
                hideSoftKeyboard(MainActivity.this, view);
            }
        });
        searchQueryEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchButton.performClick();
                    return true;
                }
                return false;
            }
        });

//        Header header = (Header) findViewById(R.id.Header);

        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "my_database")
                .allowMainThreadQueries().build();

//        context.deleteDatabase("my_database");
    }

    public static AppDatabase getDatabase(){
        return database;
    }

    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
}