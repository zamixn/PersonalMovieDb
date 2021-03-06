package com.zkadisa.personalmoviedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.List;

public class SearchResultListAdapter extends ArrayAdapter<SearchEntryListItem> {

    public SearchResultListAdapter(Context context, List<SearchEntryListItem> objects){
        super(context, R.layout.searchentrylistitemdesign, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if(v == null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.searchentrylistitemdesign, null);
        }

        ImageView poster = v.findViewById(R.id.posterImage);
        TextView title = v.findViewById(R.id.titleTextView);
        TextView year = v.findViewById(R.id.yearTextView);
        TextView type = v.findViewById(R.id.typeTextView);

        SearchEntryListItem item = getItem(position);
        title.setText(item.getTitle());
        year.setText(item.getYear());
        type.setText(item.getType());

        Ion.with(poster).centerInside().placeholder(R.drawable.ic_loading_image).error(R.drawable.ic_error_image).load(item.getPoster());

        return v;
    }
}
