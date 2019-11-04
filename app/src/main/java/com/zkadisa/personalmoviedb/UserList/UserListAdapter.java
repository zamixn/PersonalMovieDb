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

import com.koushikdutta.ion.Ion;
import com.zkadisa.personalmoviedb.R;
import com.zkadisa.personalmoviedb.SearchEntryListItem;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<UserList> {

    public UserListAdapter(Context context, List<UserList> objects){
        super(context, R.layout.userlistlistitemdesign, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if(v == null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.userlistlistitemdesign, null);
        }

        TextView title = v.findViewById(R.id.title);
        LinearLayout imageLayout = v.findViewById(R.id.imageListLayout);

        UserList item = getItem(position);
        title.setText(item.title);

        for(int i=0;i<10;i++)
        {
            ImageView image = new ImageView(v.getContext());
            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80,60));
            image.setMaxHeight(20);
            image.setMaxWidth(20);
            image.setImageResource(R.drawable.pmdb_logo);

//            Ion.with(poster).centerInside().placeholder(R.drawable.ic_loading_image).error(R.drawable.ic_error_image).load(item.getPoster());
            // Adds the view to the layout
            imageLayout.addView(image);
        }

        return v;
    }
}
