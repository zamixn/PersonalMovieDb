package com.zkadisa.personalmoviedb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FourthActivity extends AppCompatActivity {

    private ListView mylist;
    private ListAdapter adapter;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondactivitydesign);
        mylist = (ListView) findViewById(R.id.listView);

        Intent intent = getIntent();

        List<ListItem> items = (List<ListItem>) intent.getSerializableExtra("data");

        adapter = new ListAdapter(this, items);
        mylist.setAdapter(adapter);

    }
}