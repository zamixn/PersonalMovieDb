package com.zkadisa.personalmoviedb;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ThirdActivity extends AppCompatActivity {

    private Context context = this;

    private ListView mylist;
    private ListAdapter adapter;

    private List<ListItem> items;

    private EditText titleTextBox;
    private EditText descriptionTextBox;
    private ImageView imageView;
    private Button addButton;
    private Button buttonChangeActivityButton;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addactivitydesign);

        mylist = (ListView) findViewById(R.id.listView);
        titleTextBox = (EditText) findViewById(R.id.titleEditText);
        descriptionTextBox = (EditText) findViewById(R.id.descriptionEditText);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setTag(R.mipmap.ic_launcher);
        addButton = (Button) findViewById(R.id.addButton);
        buttonChangeActivityButton = (Button) findViewById(R.id.changeActivityButton);

        items = new ArrayList<>();

        items.add(new ListItem("sfdgdfg", R.drawable.ic_launcher_background, "adassd"));

        adapter = new ListAdapter(this, items);
        mylist.setAdapter(adapter);

        addButton.setOnClickListener(addItemToList);
        buttonChangeActivityButton.setOnClickListener(changeActivity);
    }

    View.OnClickListener addItemToList = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            String title = titleTextBox.getText().toString();
            String description = descriptionTextBox.getText().toString();
            int image = (int)imageView.getTag();
            items.add(new ListItem(title, image, description));
            adapter.notifyDataSetChanged();
        }
    };



    View.OnClickListener changeActivity = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, FourthActivity.class);
            intent.putExtra("data", (Serializable) items);
            context.startActivity(intent);
        }
    };

}
