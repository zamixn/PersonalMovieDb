package com.zkadisa.personalmoviedb.UserList;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
public class UserList implements Serializable {

    @NonNull
    @PrimaryKey
    public String title;

    @ColumnInfo(name = "movie_ids")
    public List<String> idList;

    @ColumnInfo(name = "date")
    public Date date;

    public UserList(String title, List<String> idList, Date date) {
        this.title = title;
        this.idList = idList;
        this.date = date;
    }

    @Ignore
    public UserList(String title, Date date) {
        this.title = title;
        idList = new ArrayList<>();
        this.date = date;
    }

    public String getDisplayTitle(){
        return title + " (" + idList.size() + ")";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public void addId(String id){
        this.idList.add(id);
    }

    public boolean containsId(String id){
        return this.idList.contains(id);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
