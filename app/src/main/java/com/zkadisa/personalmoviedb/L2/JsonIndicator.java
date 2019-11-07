package com.zkadisa.personalmoviedb.L2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zkadisa.personalmoviedb.MainActivity;
import com.zkadisa.personalmoviedb.R;

public class JsonIndicator extends View {

    public JsonIndicator (Context context) {
        super(context);
    }
    public JsonIndicator (Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public JsonIndicator (Context context, AttributeSet attrs, int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);
    }

    private int value;
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
        this.postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
//        Log.i("value", value + "");
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        canvas.drawText(value + "", width / 2, height / 2, paint);
    }
}
