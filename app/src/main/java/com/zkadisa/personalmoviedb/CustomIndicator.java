package com.zkadisa.personalmoviedb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class CustomIndicator extends View {

    public CustomIndicator (Context context) {
        super(context);
    }
    public CustomIndicator (Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomIndicator (Context context, AttributeSet attrs, int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);
    }

    public static final int NOTEXECUTED = 0;
    public static final int EXECUTING = 1;
    public static final int SUCCESS = 2;
    public static final int FAILED = 3;

    private int state;
    public int getState() {
        return state;
    }
    public void setState(final int state, float value) {
        this.state = state;
        setValue(value);
        if(state != EXECUTING)
        {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    setState(NOTEXECUTED, 0);
                }
            }, state == SUCCESS ? 1000 : 500);
        }
    }

    private float value;
    public float getValue() {
        return value;
    }
    public void setValue(float value) {
        this.value = value;
        MainActivity.Invalidate(this);
    }

    @Override
    protected void onDraw(Canvas canvas){
//        Log.i("value", value + "");
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        Paint paint = new Paint();
        paint.setStrokeWidth(getResources().getDimension(R.dimen.downloadProgressBarHeight));
        switch (state){
            case NOTEXECUTED:
                break;
            case EXECUTING:
                paint.setColor(getResources().getColor(R.color.LightOrange));
                canvas.drawLine(0, height / 2, height * value, height / 2, paint);
                break;

            case SUCCESS:
                paint.setColor(getResources().getColor(R.color.Orange));
                canvas.drawLine(0, height / 2, width, height / 2, paint);
                break;

            case FAILED:
                paint.setColor(getResources().getColor(R.color.Red));
                canvas.drawLine(0, height / 2, width, height / 2, paint);
                break;

            default:
                break;
        }
    }
}
