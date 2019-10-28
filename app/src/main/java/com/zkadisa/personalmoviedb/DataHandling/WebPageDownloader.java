package com.zkadisa.personalmoviedb.DataHandling;

import android.os.AsyncTask;
import android.util.Log;

import com.zkadisa.personalmoviedb.CustomIndicator;
import com.zkadisa.personalmoviedb.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WebPageDownloader extends AsyncTask<String, Void, String> {

    private Exception exception;

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
            OMDbReader.ReceivedWebPageDownload(webPage);
        }
    }

    private static String DownloadWebPage(String webpage){
        return  DownloadWebPage(webpage, MainActivity.customIndicator);
    }

    private static String DownloadWebPage(String webpage, CustomIndicator indicator)
    {
        if(indicator != null) {
            indicator.setState(CustomIndicator.EXECUTING, 0.25f);
        }

        try {
            URL url = new URL(webpage);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            // Create URL object
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(con.getInputStream()));

            int size = 0;
            int expectedSize = 1500;
            int c;
            StringBuffer response = new StringBuffer();
            while ((c = reader.read()) != -1) {
                response.append((char)c);
                size++;
                if(size > expectedSize)
                    expectedSize *= 2;
                if(indicator != null)
                    indicator.setValue((float)size / (float)expectedSize);
            }

//            Log.i("File Size: ", size + "  " + response.toString().length());

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
