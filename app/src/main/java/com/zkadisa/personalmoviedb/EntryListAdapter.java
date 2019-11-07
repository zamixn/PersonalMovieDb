package com.zkadisa.personalmoviedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.zkadisa.personalmoviedb.DataHandling.Entry;

import java.util.List;

public class EntryListAdapter extends ArrayAdapter<EntryListItem> {

    public EntryListAdapter(Context context, List<EntryListItem> objects){
        super(context, R.layout.entrylistitemdesign, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if(v == null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.entrylistitemdesign, null);
        }

        ImageView poster = v.findViewById(R.id.posterImage);
        TextView title = v.findViewById(R.id.titleTextView);
        TextView year = v.findViewById(R.id.yearTextView);
        TextView type = v.findViewById(R.id.typeTextView);

        EntryListItem item = getItem(position);
        title.setText(item.getTitle());
        year.setText(item.getYear());
        type.setText(item.getType());

        Ion.with(poster).centerInside().placeholder(R.drawable.ic_loading_image).error(R.drawable.ic_error_image).load(item.getPoster());

        return v;
    }

    public boolean contains(EntryListItem e){
        for (int i = 0; i < getCount(); i++)
            if(getItem(i).getImdbID().compareTo(e.getImdbRating()) == 0)
                return true;
        return  false;
    }
}