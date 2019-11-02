package com.zkadisa.personalmoviedb.DataHandling;

import android.util.Log;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.zkadisa.personalmoviedb.R;
import com.zkadisa.personalmoviedb.SearchEntryListItem;
import com.zkadisa.personalmoviedb.SearchResultListAdapter;

public class OMDbReader {

    private static String apiKey = "532287dd";
    private static String baseURL = String.format("http://www.omdbapi.com/?apikey=%s", apiKey);

    private static Gson gson = new Gson();

    private enum  QueryTpe {Search, Details}
    private static QueryTpe queryTpe;
    private static int pageNumber;
    private static boolean allowNextPage;

    private static SearchResultListAdapter adapter;
    public static void SearchOMDb(String query, boolean appendPage, SearchResultListAdapter adapter){
        OMDbReader.adapter = adapter;
        if(appendPage) {
            pageNumber++;
            if(!allowNextPage) {
                Log.d("OMDb", "Next Page Not Allowed");
                return;
            }
        }
        else {
            pageNumber = 1;
            allowNextPage = true;
        }
        queryTpe = QueryTpe.Search;
        String url = String.format("%s&s=%s&page=%s", baseURL, query, pageNumber);
        Log.d("OMDb", url);
        WebPageDownloader downloader = new WebPageDownloader();
        downloader.execute(url);
    }

    public static String GetSearchByIDURL(String id){
        String url = String.format("%s&i=%s", baseURL, id);
        return  url;
    }

    public static void ReceivedWebPageDownload(String result){
        Log.d("OMDb", "page downloaded. json: " + result);

        switch (queryTpe){
            case Details:
                ReceivedDetails(result);
                break;
            case Search:
                ReceivedSearchResults(result);
                break;
        }
    }

    private static void ReceivedSearchResults(String result){
        try {
            SearchResult r = gson.fromJson(result, SearchResult.class);
            if(!r.Response) {
                allowNextPage = false;
                throw new Exception("Response: False");
            }

            for (int i = 0; i < r.totalResults; i++){

                SearchEntry s = r.Search[i];
                adapter.add(new SearchEntryListItem(s.Title, s.Year, s.imdbID, s.Type, s.Poster));
            }
            adapter.notifyDataSetChanged();
            Log.d("OMDb", r.totalResults + "");
        }catch (Exception e){
            Log.d("OMDb", e.getMessage());
        }
    }

    private static void ReceivedDetails(String result){
        try {
            Entry e = gson.fromJson(result, Entry.class);
            Log.d("OMDb", e.Title);
        }catch (Exception e){
            Log.d("OMDb", "Failed to deserialize");
        }
    }

    public static int GetPageNumber()
    {
        return pageNumber;
    }
}
