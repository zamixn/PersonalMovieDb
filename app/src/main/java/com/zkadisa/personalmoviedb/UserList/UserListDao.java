package com.zkadisa.personalmoviedb.UserList;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserListDao {

    @Insert
    void insert(UserList userList);

    @Update
    int update(UserList tour);

    @Query("DELETE from UserList")
    void deleteAll();

    @Delete
    void delete(UserList list);

    @Query("SELECT * FROM UserList WHERE title = :id ")
    UserList get(String id);

    @Query("SELECT * from UserList")
    List<UserList> getAllUserLists();


}
