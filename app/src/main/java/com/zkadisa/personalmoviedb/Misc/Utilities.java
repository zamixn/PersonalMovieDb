package com.zkadisa.personalmoviedb.Misc;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zkadisa.personalmoviedb.R;

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
}
