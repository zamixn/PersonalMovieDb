package com.zkadisa.personalmoviedb.UserList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserList implements Serializable {

    public String title;
    public List<String> idList;

    public UserList(String title, List<String> idList) {
        this.title = title;
        this.idList = idList;
    }

    public UserList(String title) {
        this.title = title;
        idList = new ArrayList<>();
    }
}
