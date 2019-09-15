package com.zkadisa.personalmoviedb;

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
        items.add(new ListItem("Alice", R.drawable.ic_launcher_background, "Math"));
        items.add(new ListItem("Chris", R.drawable.ic_launcher_background, "Info"));
        items.add(new ListItem("Bob", R.drawable.ic_launcher_background, "Chem"));
        items.add(new ListItem("Caine", R.drawable.ic_launcher_background, "Info"));
        items.add(new ListItem("Alice", R.drawable.ic_launcher_background, "Chem"));
        items.add(new ListItem("Daryl", R.drawable.ic_launcher_background, "Geo"));
        items.add(new ListItem("Syrana", R.drawable.ic_launcher_background, "Phys"));
        items.add(new ListItem("Evelyn", R.drawable.ic_launcher_background, "Geo"));
        items.add(new ListItem("Triss", R.drawable.ic_launcher_background, "Math"));
        items.add(new ListItem("Eve", R.drawable.ic_launcher_background, "Lit"));
        items.add(new ListItem("Yennefer", R.drawable.ic_launcher_background, "Magic"));
        items.add(new ListItem("Fred", R.drawable.ic_launcher_background, "Phys"));
        items.add(new ListItem("Yennefer", R.drawable.ic_launcher_background, "Magic"));
        items.add(new ListItem("Eve", R.drawable.ic_launcher_background, "Lit"));
        items.add(new ListItem("Shani", R.drawable.ic_launcher_background, "Chem"));
        items.add(new ListItem("Zack", R.drawable.ic_launcher_background, "Info"));
        items.add(new ListItem("Keira", R.drawable.ic_launcher_background, "Info"));
        return  items;
    }
}
