package com.zkadisa.personalmoviedb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private ListView mylist;
    private ListAdapter adapter;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondactivitydesign);
        mylist = (ListView) findViewById(R.id.listView);

        List<ListItem> items = new ArrayList<>();

        Intent intent = getIntent();
        if(intent.getBooleanExtra("flag", true)){

            items.add(new ListItem("Alice", R.drawable.ic_launcher_background, "Math"));
            items.add(new ListItem("Bob", R.drawable.ic_launcher_background, "Chem"));
            items.add(new ListItem("Caine", R.drawable.ic_launcher_background, "Info"));
            items.add(new ListItem("Daryl", R.drawable.ic_launcher_background, "Geo"));
            items.add(new ListItem("Eve", R.drawable.ic_launcher_background, "Lit"));
            items.add(new ListItem("Fred", R.drawable.ic_launcher_background, "Phys"));

        }
        else{

            items.add(new ListItem("Mathematics", R.drawable.ic_launcher_background, "Mathematics"));
            items.add(new ListItem("Chemistry", R.drawable.ic_launcher_background, "Chemistry"));
            items.add(new ListItem("Informatics", R.drawable.ic_launcher_background, "Informatics"));
            items.add(new ListItem("Geography", R.drawable.ic_launcher_background, "Geography"));
            items.add(new ListItem("Lit", R.drawable.ic_launcher_background, "Literature"));
            items.add(new ListItem("Physics", R.drawable.ic_launcher_background, "Physics"));

        }

        adapter = new ListAdapter(this, items);
        mylist.setAdapter(adapter);

    }
}
