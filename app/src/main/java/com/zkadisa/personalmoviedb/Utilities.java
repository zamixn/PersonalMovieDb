package com.zkadisa.personalmoviedb;

import android.content.Context;
import android.os.Vibrator;

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
}
