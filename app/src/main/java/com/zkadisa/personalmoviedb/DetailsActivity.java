package com.zkadisa.personalmoviedb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zkadisa.personalmoviedb.DataHandling.Entry;
import com.zkadisa.personalmoviedb.DataHandling.OMDbReader;
import com.zkadisa.personalmoviedb.DataHandling.YoutubeReader;
import com.zkadisa.personalmoviedb.Misc.VideoPagerAdapter;

public class DetailsActivity extends BaseActivityClass {

    private Context context = this;

    private ImageView poster_imageView;
    private TextView title_textView;
    private TextView type_textView;
    private TextView year_textView;
    private TextView genre_textView;
    private TextView directors_textView;
    private TextView writers_textView;
    private TextView actors_textView;
    private TextView plot_textView;

    private Gson gson = new Gson();

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailsactivitydesign);

        poster_imageView = findViewById(R.id.poster_imageView);
        title_textView = findViewById(R.id.titleTextView);
        type_textView = findViewById(R.id.typeTextView);
        year_textView = findViewById(R.id.yearTextView);
        plot_textView = findViewById(R.id.descriptionTextView);
        genre_textView = findViewById(R.id.genreTextView);
        directors_textView = findViewById(R.id.directorsTextView);
        writers_textView = findViewById(R.id.writersTextView);
        actors_textView = findViewById(R.id.actorsTextView);

        Intent intent = getIntent();

        SearchEntryListItem item = (SearchEntryListItem) intent.getSerializableExtra("data");
        title_textView.setText(item.getTitle());
        type_textView.setText(item.getType().toUpperCase());
        year_textView.setText(item.getYear());
        plot_textView.setText("");
        genre_textView.setText("");
        directors_textView.setText("");
        writers_textView.setText("");
        actors_textView.setText("");

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
                        genre_textView.setText(entry.Genre);
                        plot_textView.setText(entry.Plot);
                        directors_textView.setText(entry.Director);
                        writers_textView.setText(entry.Writer);
                        actors_textView.setText(entry.Actors);
                    }
                });

        VideoPagerAdapter sectionsPagerAdapter = new VideoPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        YoutubeReader.SearchYoutubeForVideosAndNotifyPager(item.getTitle() + " " + item.getYear(), context, sectionsPagerAdapter, 5);
    }
}
