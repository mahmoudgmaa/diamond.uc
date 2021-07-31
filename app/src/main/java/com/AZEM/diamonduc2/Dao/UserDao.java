package com.AZEM.diamonduc2.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.AZEM.diamonduc2.models.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user1);

    @Update
    void update(User user1);

    @Delete
    void delete(User user1);

    @Query("DELETE FROM user_table")
    void deleteAllUsers();

    @Query("SELECT * FROM user_table")
    LiveData<List<User>> getAllUser();

    @Query("SELECT * FROM user_table WHERE userID=:id")
    LiveData<User> getUser(String id);


}
