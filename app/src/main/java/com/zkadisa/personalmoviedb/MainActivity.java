package com.zkadisa.personalmoviedb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.zkadisa.personalmoviedb.DataHandling.OMDbReader;
import com.zkadisa.personalmoviedb.DataHandling.YoutubeReader;
import com.zkadisa.personalmoviedb.L1.FirstActivity;
import com.zkadisa.personalmoviedb.L1.ListAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context context = this;
    private static MainActivity instance;

    private EditText searchQueryEditText;

    private Button searchButton;

    private ListView searchList;
    private SearchResultListAdapter adapter;

    public static CustomIndicator customIndicator;

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

        customIndicator = findViewById(R.id.downloadProgressBar);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                OMDbReader.SearchOMDb(searchQueryEditText.getText().toString(), adapter);
                hideSoftKeyboard(MainActivity.this, view); // MainActivity is the name of the class and v is the View parameter used in the button listener method onClick.
            }
        });

//        Header header = (Header) findViewById(R.id.Header);

        YoutubeReader.SearchYoutube("Shrek trailer", context);
    }

    public void InvalidateView(final View v)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                v.invalidate();
            }
        });
    }
    public static void Invalidate(final View v)
    {
        instance.InvalidateView(v);
    }

    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
}