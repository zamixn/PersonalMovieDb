package com.zkadisa.personalmoviedb.L1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zkadisa.personalmoviedb.R;

public class DetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleView;
    private TextView descriptionView;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailsactivitydesignlab);

        imageView = (ImageView) findViewById(R.id.imageView);
        titleView = (TextView) findViewById(R.id.titleView);
        descriptionView = (TextView) findViewById(R.id.descriptionView);

        Intent intent = getIntent();

        ListItem item = (ListItem) intent.getSerializableExtra("data");


        titleView.setText(item.getTitle());
        descriptionView.setText(item.getDescription());
        imageView.setImageResource(item.getImageId());
    }
}
