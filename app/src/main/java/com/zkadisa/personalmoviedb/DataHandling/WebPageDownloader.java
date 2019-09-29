package com.zkadisa.personalmoviedb.DataHandling;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private static String DownloadWebPage(String webpage)
    {
        try {
            // Create URL object
            URL url = new URL(webpage);
            BufferedReader readr =
                    new BufferedReader(new InputStreamReader(url.openStream()));

            // read each line from stream till end
            String line;
            String webPage = "";
            while ((line = readr.readLine()) != null) {
                webPage += line;
            }

            readr.close();
            Log.d("WEB", "Successfully Downloaded.");
            return webPage;
        }
        // Exceptions
        catch (MalformedURLException mue) {
            Log.d("WEB", "Malformed URL Exception raised.");
        }
        catch (IOException ie) {
            Log.d("WEB", "IOException raised.");
        }
        return  null;
    }
}
