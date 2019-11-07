package com.zkadisa.personalmoviedb.L2;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.zkadisa.personalmoviedb.DataHandling.WebPageDownloader;
import com.zkadisa.personalmoviedb.Misc.CustomIndicator;
import com.zkadisa.personalmoviedb.R;

public class MainActivity extends AppCompatActivity {

    private Context context = this;

    public static CustomIndicator customIndicator;
    public static JsonIndicator jsonIndicator;
    private Button requestButton;
    private Button requestButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab2_mainactivitydesign);

        jsonIndicator = findViewById(R.id.jsonIndicator);
        requestButton = findViewById(R.id.requestButton);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customIndicator = findViewById(R.id.cutomIndicator1);
                String url = "http://jsonplaceholder.typicode.com/posts";

                WebPageDownloader downloader = new WebPageDownloader();
                downloader.setType(WebPageDownloader.L2);
                downloader.execute(url);
                System.gc();
            }
        });
        requestButton2 = findViewById(R.id.requestButton2);
        requestButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customIndicator = findViewById(R.id.barIndicator);
                String url = "http://jsonplaceholder.typicode.com/posts";

                WebPageDownloader downloader = new WebPageDownloader();
                downloader.setType(WebPageDownloader.L2);
                downloader.execute(url);
                System.gc();
            }
        });


    }


}