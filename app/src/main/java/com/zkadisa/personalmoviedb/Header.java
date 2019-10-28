package com.zkadisa.personalmoviedb;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zkadisa.personalmoviedb.L1.FirstActivity;

public class Header extends RelativeLayout {
    private final String TAG = this.getClass().getName();

    private Button l1Button;
    private ImageButton logoButton;

    public Header(Context context) {
        super(context);
        initHeader();
    }

    public Header(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeader();
    }

    public Header(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeader();
    }


//    public void initHeader(final Context context) {
//        inflateHeader(context);
//    }
    public void initHeader() {
        inflateHeader();
    }

//    private void inflateHeader(final Context context) {
//        LayoutInflater inflater = (LayoutInflater) getContext()
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.header, this);
//        l1Button = (Button) findViewById(R.id.lab1Button);
//
//        l1Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, FirstActivity.class);
//                context.startActivity(intent);
//            }
//        });
//    }
    private void inflateHeader() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header, this);
        l1Button = (Button) findViewById(R.id.lab1Button);
        logoButton = (ImageButton)findViewById(R.id.logo);

        final Context context = getContext();
        l1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.Vibrate(context);
                Intent intent = new Intent(context, FirstActivity.class);
                context.startActivity(intent);
            }
        });

        logoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.Vibrate(context);
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
