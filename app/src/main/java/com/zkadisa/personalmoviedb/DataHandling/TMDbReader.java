package com.zkadisa.personalmoviedb.DataHandling;

public class TMDbReader {

    private static String API_KEY = "304cd9928317bcf01846de592a68ca3b";
    private static String BASE_URL = "https://api.themoviedb.org/3/";

    public static String GetTopUpcomingMoviesURL(){
        String url = String.format("%smovie/upcoming?api_key=%s", BASE_URL, API_KEY);
        return url;
    }

    public static String GetPopularMoviesURL(){
        String url = String.format("%smovie/popular?api_key=%s", BASE_URL, API_KEY);
        return url;
    }

    public static String GetExternalIDsURL(String id){
        String url = String.format("%smovie/%s/external_ids?api_key=%s", BASE_URL, id, API_KEY);
        return url;
    }
}
