package com.zkadisa.personalmoviedb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.room.Room;

import com.zkadisa.personalmoviedb.DataHandling.OMDbReader;
import com.zkadisa.personalmoviedb.Misc.CustomIndicator;
import com.zkadisa.personalmoviedb.Misc.ScrollListView;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchForEntriesActivity extends BaseActivityClass {

    private Context context = this;

    private EditText searchQueryEditText;
    private Button searchButton;

    private ScrollListView searchList;
    private SearchResultListAdapter adapter;

    private long previousBottomScrollTime = -500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchforentriesactivitydesign);

        searchQueryEditText = findViewById(R.id.searchPhraseEditText);
        searchButton = findViewById(R.id.search_button);

        searchList = findViewById(R.id.searchEntryListView);
        adapter = new SearchResultListAdapter(this, new ArrayList<SearchEntryListItem>());
        searchList.setAdapter(adapter);

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SearchEntryListItem item = (SearchEntryListItem) searchList.getItemAtPosition(i);
                Intent intent = new Intent();
                intent.putExtra("data", item);
                setResult(RESULT_OK, intent);
                finish();
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
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                OMDbReader.SearchOMDb(searchQueryEditText.getText().toString(), false, adapter);
                MainActivity.hideSoftKeyboard(SearchForEntriesActivity.this, view);
            }
        });
    }
}