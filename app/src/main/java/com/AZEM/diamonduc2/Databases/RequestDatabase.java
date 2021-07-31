package com.AZEM.diamonduc2.Databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.AZEM.diamonduc2.Dao.RequestsDao;
import com.AZEM.diamonduc2.models.Requests;

@Database(entities = {Requests.class}, version = 1, exportSchema = false)
public abstract class RequestDatabase extends RoomDatabase {
    private static RequestDatabase instance;

    public static synchronized RequestDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext()
                    , RequestDatabase.class, "requests_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract RequestsDao requestsDao();
}
