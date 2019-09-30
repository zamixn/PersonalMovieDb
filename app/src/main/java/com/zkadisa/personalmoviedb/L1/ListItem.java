package com.zkadisa.personalmoviedb.L1;

import com.zkadisa.personalmoviedb.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListItem implements Serializable {

    private String title;
    private int imageId;
    private String description;

    public ListItem(){}

    public ListItem(String title, int imageId, String description){
        this.setTitle(title);
        this.setImageId(imageId);
        this.setDescription(description);
    }

    public String getTitle(){
        return  title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class SortByTitleAscending implements Comparator<ListItem>{
        public  int compare(ListItem a, ListItem b) {
            return  a.getTitle().compareTo(b.getTitle());
        }
    }
    public static class SortByTitleDescending implements Comparator<ListItem>{
        public  int compare(ListItem a, ListItem b) {
            return  b.getTitle().compareTo(a.getTitle());
        }
    }

    public static List<ListItem> PopulateWithItems(){
        List<ListItem> items = new ArrayList<>();
        items.add(new ListItem("Alice 0", R.drawable.ic_launcher_background, "Math"));
        items.add(new ListItem("Chris 1", R.drawable.ic_launcher_background, "Info"));
        items.add(new ListItem("Bob 2", R.drawable.ic_launcher_background, "Chem"));
        items.add(new ListItem("Caine 3", R.drawable.ic_launcher_background, "Info"));
        items.add(new ListItem("Alice 4", R.drawable.ic_launcher_background, "Chem"));
        items.add(new ListItem("Daryl 5", R.drawable.ic_launcher_background, "Geo"));
        items.add(new ListItem("Syrana 6", R.drawable.ic_launcher_background, "Phys"));
        items.add(new ListItem("Evelyn 7", R.drawable.ic_launcher_background, "Geo"));
        items.add(new ListItem("Triss 8", R.drawable.ic_launcher_background, "Math"));
        items.add(new ListItem("Eve 9", R.drawable.ic_launcher_background, "Lit"));
        items.add(new ListItem("Yennefer 10", R.drawable.ic_launcher_background, "Magic"));
        items.add(new ListItem("Fred 11", R.drawable.ic_launcher_background, "Phys"));
        items.add(new ListItem("Yennefer 12", R.drawable.ic_launcher_background, "Magic"));
        items.add(new ListItem("Eve 13", R.drawable.ic_launcher_background, "Lit"));
        items.add(new ListItem("Shani 14", R.drawable.ic_launcher_background, "Chem"));
        items.add(new ListItem("Zack 15", R.drawable.ic_launcher_background, "Info"));
        items.add(new ListItem("Keira 16", R.drawable.ic_launcher_background, "Info"));
        return  items;
    }
}
