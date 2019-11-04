package com.zkadisa.personalmoviedb.DataHandling;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zkadisa.personalmoviedb.Misc.VideoPagerAdapter;

public class YoutubeReader {

    private static final String TAG = YoutubeReader.class.getSimpleName();

    private static final String API_KEY = "AIzaSyDEFIp8fvVbIZ3ibX0OZscWv0T-jmfF6hc";
    private static String baseURL = String.format("https://www.googleapis.com/youtube/v3/search?key=%s", API_KEY);

    private static Gson gson = new Gson();

    private enum  QueryTpe {Search}
    private static QueryTpe queryTpe;

    public static void SearchYoutubeForVideosAndNotifyPager(String keyword, final Context context, final VideoPagerAdapter adapter, final int max)
    {
        String url = String.format(baseURL + "&type=video&part=snippet&q=%s", keyword);
        url = url.replace(" ","%20");

        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        YoutubeSearchResults results = gson.fromJson(result, YoutubeSearchResults.class);
                        for (int i = 0; i < results.items.length && i < max; i++){
                            adapter.addFragment(results.items[i].snippet.title, GetEmbeddedURL(results.items[i].id.videoId));
                            Log.i(TAG, results.items[i].snippet.title + " " + results.items[i].id.videoId);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        Log.i(TAG, url);
    }

    public static String GetEmbeddedURL(String id)
    {
        return String.format("https://www.youtube.com/embed/%s", id);
    }
}


