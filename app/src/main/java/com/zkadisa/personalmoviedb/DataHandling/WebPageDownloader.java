package com.zkadisa.personalmoviedb.DataHandling;

import android.os.AsyncTask;
import android.util.Log;

import com.zkadisa.personalmoviedb.Misc.CustomIndicator;
import com.zkadisa.personalmoviedb.SearchActivity;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebPageDownloader extends AsyncTask<String, Void, String> {

    private Exception exception;
    public static final int MAIN = 1;
    public static final int L2 = 2;
    private static WebPageDownloader instance;
    private int type = MAIN;

    public WebPageDownloader(){
        instance = this;
    }

    public void setType(int type){
        this.type = type;
    }

    protected String doInBackground(String... urls) {
        try {
            return DownloadWebPage(urls[0]);
        } catch (Exception e) {
            this.exception = e;
            return null;
        } finally {
        }
    }

    protected void onPostExecute(String webPage) {
        Log.d("WEB", "exeption: " + (exception != null ? exception.getMessage() : "null"));
        if(exception == null){
            if(type == MAIN)
                OMDbReader.ReceivedWebPageDownload(webPage);
            else if(type == L2) {
                try{
                    JSONArray jsonArray = new JSONArray(webPage);
                    com.zkadisa.personalmoviedb.L2.MainActivity.jsonIndicator.setValue(jsonArray.length());
                }catch (Exception e) {
                    Log.e("JSON EXCEPTION", e.getMessage(), e);
                }
            }
        }
    }

    private static String DownloadWebPage(String webpage){
        return  DownloadWebPage(webpage, instance.type == MAIN ? SearchActivity.customIndicator :
                com.zkadisa.personalmoviedb.L2.MainActivity.customIndicator);
    }

    private static String DownloadWebPage(String webpage, CustomIndicator indicator)
    {
        if(indicator != null) {
            indicator.setState(CustomIndicator.EXECUTING, 0.1f);
        }
        Log.d("WEB", indicator + "");

        try {
            URL url = new URL(webpage);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            // Create URL object
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            int size = 0;
            int length = con.getContentLength();

            int expectedSize = length == -1 || length == 0 ? (instance.type == MAIN ? 2 : 605) : length;
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                response.append(line);
                size++;
                if(size > expectedSize)
                    expectedSize *= 2;
                if(indicator != null) {
                    indicator.setValue((float) size / (float) expectedSize);
//                    Log.d("WEB", "Sending: " + ((float)size / (float)expectedSize));
                }
                if(size % 10 == 0 && instance.type == L2)
                    Thread.sleep(1);
            }

//            int size = 0;
//            int expectedSize = 1500;
//            int c;
//            StringBuffer response = new StringBuffer();
//            while ((c = reader.read()) != -1) {
//                response.append((char)c);
//                size++;
//                if(size > expectedSize)
//                    expectedSize *= 2;
//                if(indicator != null)
//                    indicator.setValue((float)size / (float)expectedSize);
//            }

            reader.close();

            if(indicator != null) {
                indicator.setState(CustomIndicator.SUCCESS, 1f);
            }
            Log.d("WEB", "Successfully Downloaded.");
            return response.toString();
        }
        // Exceptions
        catch (Exception e) {
            Log.d("WEB", "Exception raised.");
            if(indicator != null) {
                indicator.setState(CustomIndicator.FAILED, 1f);
            }
        }
        return  null;
    }
}
