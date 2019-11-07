package com.zkadisa.personalmoviedb.UserList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.bitmap.Transform;
import com.zkadisa.personalmoviedb.DataHandling.Entry;
import com.zkadisa.personalmoviedb.DataHandling.OMDbReader;
import com.zkadisa.personalmoviedb.R;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<UserList> {

    private Gson gson = new Gson();
    private boolean initialized = false;

    public UserListAdapter(Context context, List<UserList> objects){
        super(context, R.layout.userlistlistitemdesign, objects);
    }

    private ImageView getNewImageView(View v){
        ImageView image = new ImageView(v.getContext());
        image.setLayoutParams(new LinearLayout.LayoutParams(
                57,
                80));
        image.setAdjustViewBounds(false);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        UserList item = getItem(position);
        if(!initialized && v != null)
            parent.removeAllViewsInLayout();
        if(v == null || !initialized){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.userlistlistitemdesign, null);

            LinearLayout imageLayout = v.findViewById(R.id.imageListLayout);
            for(int i = 0; i < item.getIdList().size() && i < 10; i++)
            {
                final ImageView image = getNewImageView(v);
                imageLayout.addView(image);

                Ion.with(v.getContext())
                        .load(OMDbReader.GetSearchByIDURL(item.getIdList().get(i)))
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                if(e != null){
                                    Log.e("UserListAdapter.java", e.getMessage());
                                    return;
                                }

                                Entry entry = gson.fromJson(result.toString(), Entry.class);

//                            image.setImageResource(R.drawable.pmdb_logo);
                                Ion.with(image).resize(80, 80).placeholder(R.drawable.ic_loading_image).error(R.drawable.ic_error_image).load(entry.Poster);
                            }
                        });
            }
            initialized = true;
        }

        TextView title = v.findViewById(R.id.title);
        title.setText(item.getDisplayTitle());

        return v;
    }

    public void setInitialized(boolean value){
        initialized = false;
    }
}
