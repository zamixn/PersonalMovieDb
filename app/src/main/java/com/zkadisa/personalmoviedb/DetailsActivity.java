package com.zkadisa.personalmoviedb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zkadisa.personalmoviedb.DataHandling.Entry;
import com.zkadisa.personalmoviedb.DataHandling.OMDbReader;

public class DetailsActivity extends AppCompatActivity {

    private Context context = this;

    private ImageView poster_imageView;
    private TextView title_textView;
    private TextView plot_textView;

    private Gson gson = new Gson();

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailsactivitydesign);

        poster_imageView = findViewById(R.id.poster_imageView);
        title_textView = findViewById(R.id.titleTextView);
        plot_textView = findViewById(R.id.descriptionTextView);

        Intent intent = getIntent();

        SearchEntryListItem item = (SearchEntryListItem) intent.getSerializableExtra("data");

        title_textView.setText(item.getTitle());

        Ion.with(poster_imageView)
                .placeholder(R.drawable.ic_loading_image)
                .error(R.drawable.ic_error_image)
                .load(item.getPoster());

        Ion.with(context)
                .load(OMDbReader.GetSearchByIDURL(item.getImdbID()))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e != null){
                            Log.e("Details Activity", e.getMessage());
                            return;
                        }

                        Entry entry = gson.fromJson(result.toString(), Entry.class);
                        plot_textView.setText(entry.Plot);
                    }
                });
    }
}
