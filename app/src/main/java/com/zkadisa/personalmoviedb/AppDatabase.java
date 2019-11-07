package com.zkadisa.personalmoviedb;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.zkadisa.personalmoviedb.UserList.UserList;
import com.zkadisa.personalmoviedb.UserList.UserListDao;

@Database(
        entities = {UserList.class},
        version = 1
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserListDao userListDao();
}
