package com.zkadisa.personalmoviedb.Misc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;

import com.zkadisa.personalmoviedb.R;

import java.util.Timer;
import java.util.TimerTask;

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
        return (int) argb.evaluate(value, getResources().getColor(R.color.LightOrange), getResources().getColor(R.color.Orange));
    }

    @Override
    protected void onDraw(Canvas canvas){

//        Log.i("value", value + "");
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        paint.setStrokeWidth(getResources().getDimension(R.dimen.downloadProgressBarHeight));
        paint.setColor(state == FAILED ? getResources().getColor(R.color.Red) : getColor(value));

        switch (state){
            case NOTEXECUTED:
                break;
            case EXECUTING:
                canvas.drawLine(0, height / 2f, width * value, height / 2f, paint);
                break;

            case SUCCESS:
            case FAILED:
                canvas.drawLine(0, height / 2f, width, height / 2f, paint);
                break;

            default:
                break;
        }
    }
}