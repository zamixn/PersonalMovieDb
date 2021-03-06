package com.zkadisa.personalmoviedb.Misc;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zkadisa.personalmoviedb.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utilities {
    public static void Vibrate(final Context context)
    {
        boolean vibrate = false;
        if(vibrate)
        {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(100); // 5000 miliseconds = 5 seconds
        }
    }

    public static void ShowCustomToast(final Context context, String message)
    {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundResource(R.color.colorBackgroundDark);
        view.setPadding(10, 10, 10, 10);
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setText(message);
        text.setTextColor(context.getResources().getColor(R.color.colorText));
        toast.show();

    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (File file) throws Exception {
        FileInputStream fin = new FileInputStream(file);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }
}
