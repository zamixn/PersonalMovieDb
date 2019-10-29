package com.zkadisa.personalmoviedb;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zkadisa.personalmoviedb.DataHandling.Entry;
import com.zkadisa.personalmoviedb.DataHandling.OMDbReader;

import java.io.IOException;
import java.net.URI;

public class DetailsActivity extends AppCompatActivity {

    private Context context = this;

    private ImageView poster_imageView;
    private TextView title_textView;
    private TextView plot_textView;
//    YouTubePlayerView youTubePlayerView;
//    Button button;
//    YouTubePlayer.OnInitializedListener onInitializedListener;

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

        VideoPagerAdapter sectionsPagerAdapter = new VideoPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


//        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
//        onInitializedListener = new YouTubePlayer.OnInitializedListener(){
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//
//                youTubePlayer.loadVideo("Hce74cEAAaE");
//
//                youTubePlayer.play();
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//            }
//        };
//        youTubePlayerView.initialize("AIzaSyDEFIp8fvVbIZ3ibX0OZscWv0T-jmfF6hc", onInitializedListener);

//        WebView mWebView = findViewById(R.id.WebView);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        mWebView.loadUrl("https://www.youtube.com/embed/Yw6u6YkTgQ4");
//        mWebView.setWebChromeClient(new WebChromeClient());

//        VideoView videoView = findViewById(R.id.VideoView);
//        MediaController mediaController= new MediaController(this);
//        mediaController.setAnchorView(videoView);
//        Uri uri=Uri.parse("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
//        videoView.setMediaController(mediaController);
//        videoView.setVideoURI(uri);
//        videoView.requestFocus();
//
//        videoView.start();

    }
}
