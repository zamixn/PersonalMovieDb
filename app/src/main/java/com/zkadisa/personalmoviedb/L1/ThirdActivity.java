package com.zkadisa.personalmoviedb.L1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.zkadisa.personalmoviedb.R;

import java.io.Serializable;
import java.util.List;

public class ThirdActivity extends AppCompatActivity {

    private Context context = this;

    private ListView mylist;
    private ListAdapter adapter;

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

        List<ListItem> items = /*new ArrayList<>()*/ ListItem.PopulateWithItems();

//        items.add(new ListItem("temp", R.drawable.ic_launcher_background, "temporary"));

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
            adapter.add(new ListItem(title, image, description));
            adapter.notifyDataSetChanged();
        }
    };



    View.OnClickListener changeActivity = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, FourthActivity.class);
            intent.putExtra("data", (Serializable) adapter.getItems());
            context.startActivity(intent);
        }
    };

}
