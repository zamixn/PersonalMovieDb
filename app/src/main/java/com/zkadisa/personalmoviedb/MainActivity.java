package com.zkadisa.personalmoviedb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.zkadisa.personalmoviedb.DataHandling.OMDbReader;
import com.zkadisa.personalmoviedb.L1.FirstActivity;
import com.zkadisa.personalmoviedb.L1.ListAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context context = this;

    private EditText searchQueryEditText;

    private Button searchButton;
    private Button l1Button;

    private ListView searchList;
    private SearchResultListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivitydesign);

        searchQueryEditText = findViewById(R.id.searchPhraseEditText);
        searchButton = findViewById(R.id.search_button);
        l1Button = findViewById(R.id.lab1Button);

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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                OMDbReader.SearchOMDb(searchQueryEditText.getText().toString(), adapter);
            }
        });
        l1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FirstActivity.class);
                context.startActivity(intent);
            }
        });
    }
}