package com.zkadisa.personalmoviedb.L3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.zkadisa.personalmoviedb.BaseActivityClass;
import com.zkadisa.personalmoviedb.R;

import java.io.File;

public class ImageFromFileActivity extends BaseActivityClass {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab3_imagefromfileactivity);

        ImageView image = findViewById(R.id.imagefromfile);
        String path = getIntent().getStringExtra("path");
        Log.i("image_path", path);
        File imgFile = new  File(path);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            image.setImageBitmap(myBitmap);
        }

    }
}
