package com.zkadisa.personalmoviedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListAdapter extends ArrayAdapter<ListItem> implements Filterable {

    private List<ListItem> mList;
    private List<ListItem> filteredList;

    public ListAdapter(Context context, List<ListItem> objects){
        super(context, R.layout.listitemdesign, objects);
        this.mList = objects;
        this.filteredList = objects;
    }

    public int getCount(){
        return  filteredList.size();
    }
    public ListItem getItem(int position){
        return filteredList.get(position);
    }
    public List<ListItem> getItems(){
        return mList;
    }

    @Override
    public void sort(Comparator<? super ListItem> comparator){
        Collections.sort(filteredList, comparator);
        Collections.sort(mList, comparator);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if(v == null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listitemdesign, null);
        }

        TextView title = (TextView) v.findViewById(R.id.title);
        TextView description = (TextView) v.findViewById(R.id.description);
        ImageView image = (ImageView) v.findViewById(R.id.image);

        ListItem item = getItem(position);
        title.setText(item.getTitle());
        description.setText(item.getDescription());
        image.setImageResource(item.getImageId());

        return v;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final  List<ListItem> items = mList;
                int count = mList.size();
                final List<ListItem> newList = new ArrayList<ListItem>(count);

                String constraint = charSequence.toString().toLowerCase();
                for (int i = 0; i < count; i++) {
                    if(items.get(i).getTitle().toLowerCase().startsWith(constraint)){
                        newList.add(items.get(i));
                    }
                }

                FilterResults results = new FilterResults();
                results.values = newList;
                results.count = newList.size();
                return  results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (List<ListItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };

        return  filter;
    }
}
