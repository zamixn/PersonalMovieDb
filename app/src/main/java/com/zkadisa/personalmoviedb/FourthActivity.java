package com.zkadisa.personalmoviedb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.List;

public class FourthActivity extends AppCompatActivity {

    private enum Sorted { No, Ascendng, Descending}
    private Sorted sorted;

    private EditText filterText;
    private TextView sortedLabelTextView;
    private Button sortButton;
    private ListView mylist;
    private ListAdapter adapter;

    private Context context = this;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviewactivitydesign);
        mylist = (ListView) findViewById(R.id.listView);
        filterText = (EditText) findViewById(R.id.filterEditText);
        sortButton = (Button) findViewById(R.id.sortButton);
        sortedLabelTextView = (TextView) findViewById(R.id.sortedTextView);
        sortedLabelTextView.setText("");

        Intent intent = getIntent();

        List<ListItem> items = (List<ListItem>) intent.getSerializableExtra("data");

        sorted = Sorted.No;
        adapter = new ListAdapter(this, items);
        mylist.setAdapter(adapter);
        mylist.setClickable(true);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListItem item = (ListItem) mylist.getItemAtPosition(i);
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("data", (Serializable) item);
                context.startActivity(intent);
            }
        });

        sortButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (sorted){
                    case Ascendng:
                        adapter.sort(new ListItem.SortByTitleDescending());
                        sorted = Sorted.Descending;
                        sortedLabelTextView.setText(R.string.sortedLabelDesc);
                        break;
                    case No:
                    case Descending:
                        adapter.sort(new ListItem.SortByTitleAscending());
                        sortedLabelTextView.setText(R.string.sortedLabelAsc);
                        sorted = Sorted.Ascendng;
                        break;
                }
            }
        });

        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });

    }
}