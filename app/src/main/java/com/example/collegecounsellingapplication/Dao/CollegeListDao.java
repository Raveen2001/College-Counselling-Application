package com.example.collegecounsellingapplication.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.collegecounsellingapplication.modals.College;

import java.util.List;

@Dao
public interface CollegeListDao {
    @Insert
    void insert(College data);

    @Update
    void update(College data);

    @Delete
    void delete(College data);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllData(List<College> data);

    @Query("DELETE FROM collage_list")
    void deleteAllData();

    @Query("SELECT * FROM collage_list")
    LiveData<List<College>> getAllData();

}
