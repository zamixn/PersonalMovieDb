package com.zkadisa.personalmoviedb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.PrecomputedText;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FirstActivity extends AppCompatActivity {

    private ViewGroup viewGroup;
    private ViewGroup.LayoutParams textViewLayoutParams;
    private float textViewSize;
    private CharSequence textViewText;

    private Button toggleButton;
    private TextView myTextView;
    private static int lineCount;
    private static boolean isInvisible;

    private Button secondActivityButton;
    private Context context = this;

    private Button thirdActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstactivitydesign);

        toggleButton = (Button) findViewById(R.id.toggleButton);
        secondActivityButton = (Button) findViewById(R.id.secondActivityButton);
        thirdActivityButton = (Button) findViewById(R.id.thirdActivityButton);
        myTextView = (TextView) findViewById(R.id.title);

        viewGroup = (ViewGroup) myTextView.getParent();
        textViewLayoutParams = myTextView.getLayoutParams();
        textViewSize = myTextView.getTextSize();

        toggleButton.setOnClickListener(toggleButtonOnClick);
        secondActivityButton.setOnClickListener(startSecondActivity);
        secondActivityButton.setOnLongClickListener(startSecondActivityLong);
        thirdActivityButton.setOnClickListener(startThirdActivity);
    }

    View.OnClickListener toggleButtonOnClick = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            isInvisible = !isInvisible;
            if(isInvisible){
                textViewText = myTextView.getText();
                viewGroup.removeView(myTextView);
            }
            else{
                myTextView = new TextView(context);
                myTextView.setLayoutParams(textViewLayoutParams);
                myTextView.setTextSize(textViewSize);
                viewGroup.addView(myTextView);
                myTextView.setText(textViewText + "\n" + "line " + lineCount++);
            }

        }
    };

    public void runSecondActivity(boolean b) {
        Intent intent = new Intent(context, SecondActivity.class);
        intent.putExtra("flag", b);
        context.startActivity(intent);
    }

    View.OnClickListener startSecondActivity = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            runSecondActivity(false);
        }
    };

    View.OnLongClickListener startSecondActivityLong = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View view) {
            runSecondActivity(true);
            return  true;
        }
    };

    View.OnClickListener startThirdActivity = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ThirdActivity.class);
            context.startActivity(intent);
        }
    };

}
