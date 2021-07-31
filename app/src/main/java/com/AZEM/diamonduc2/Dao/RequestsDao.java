package com.AZEM.diamonduc2.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.AZEM.diamonduc2.models.Requests;

import java.util.List;

@Dao
public interface RequestsDao {
    @Insert
    void insert(Requests request);

    @Query("SELECT * FROM requests_table")
    LiveData<List<Requests>> getAllRequests();

    @Query("DELETE FROM requests_table")
    void deleteAllRequests();
}
