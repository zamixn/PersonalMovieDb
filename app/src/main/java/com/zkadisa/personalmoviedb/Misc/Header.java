package com.zkadisa.personalmoviedb.Misc;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.zkadisa.personalmoviedb.L1.FirstActivity;
import com.zkadisa.personalmoviedb.SearchActivity;
import com.zkadisa.personalmoviedb.R;
import com.zkadisa.personalmoviedb.UserList.UserListActivity;

public class Header extends RelativeLayout {
    private final String TAG = this.getClass().getName();

    private Button listsButton;
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

    public void initHeader() {
        inflateHeader();
    }


    private void inflateHeader() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header, this);
        listsButton = (Button)findViewById(R.id.UserListButton);
        l1Button = (Button) findViewById(R.id.lab1Button);
        logoButton = (ImageButton)findViewById(R.id.logo);

        final Context context = getContext();
        listsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.Vibrate(context);
                Intent intent = new Intent(context, UserListActivity.class);
                context.startActivity(intent);

            }
        });

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
                Intent intent = new Intent(context, SearchActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
