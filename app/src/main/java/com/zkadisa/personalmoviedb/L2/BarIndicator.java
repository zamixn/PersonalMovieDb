package com.zkadisa.personalmoviedb.L2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;

import com.zkadisa.personalmoviedb.Misc.CustomIndicator;
import com.zkadisa.personalmoviedb.R;

import java.util.Timer;
import java.util.TimerTask;

public class BarIndicator extends CustomIndicator {

    public BarIndicator (Context context) {
        super(context);
    }
    public BarIndicator (Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public BarIndicator (Context context, AttributeSet attrs, int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);
    }

    private final int barCount = 5;

    private ArgbEvaluator argb = new ArgbEvaluator();
    private Paint paint = new Paint();

    private int state;
    public int getState() {
        return state;
    }
    public void setState(final int state, float value) {
        this.state = state;
        setValue(value);
        if(state != EXECUTING && state != NOTEXECUTED)
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
        this.value = value > 1 ? 1 : (value < 0 ? 0 : value);
        this.postInvalidate();
    }

    private int getColor(float value){
        return (int) argb.evaluate(value, getResources().getColor(R.color.colorGreen), getResources().getColor(R.color.colorGreenDark));
    }

    @Override
    protected void onDraw(Canvas canvas){

//        Log.i("value", value + "");
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        int barWidth = width / barCount;
        float valuePerBar = 1f / barCount;

        paint.setStrokeWidth(getResources().getDimension(R.dimen.downloadProgressBarHeight_Bar));
        Log.i("color:", value + " " + String.format("0x%08X", getColor(value)));

        for (float x = barWidth, v = valuePerBar; x <= (float)width * value; x += barWidth, v += valuePerBar)
        {
            paint.setColor(getColor(v));
            switch (state){
                case EXECUTING:
                    canvas.drawLine(x - barWidth, height / 2f, x, height / 2f, paint);
                    break;

                case SUCCESS:
                case FAILED:
                    canvas.drawLine(x - barWidth, height / 2f, width, height / 2f, paint);
                    break;

                default:
                    break;
            }
        }
//        switch (state){
//            case EXECUTING:
//                canvas.drawLine(0, height / 2f, (float)width * value, height / 2f, paint);
//                break;
//
//            case SUCCESS:
//            case FAILED:
//                canvas.drawLine(0, height / 2f, width, height / 2f, paint);
//                break;
//
//            default:
//                break;
//        }
    }
}
