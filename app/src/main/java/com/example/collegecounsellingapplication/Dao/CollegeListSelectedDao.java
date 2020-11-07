package com.example.collegecounsellingapplication.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.collegecounsellingapplication.modals.CollegeListSaved;

import java.util.List;

@Dao
public interface CollegeListSelectedDao {
    @Insert
    void insert(CollegeListSaved data);

    @Update
    void update(CollegeListSaved data);

    @Delete
    void delete(CollegeListSaved data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllData(List<CollegeListSaved> data);

    @Query("DELETE FROM college_list_saved")
    void deleteAllData();

    @Query("SELECT * FROM college_list_saved ORDER BY priority asc")
    LiveData<List<CollegeListSaved>> getAllData();

//    @Query("SELECT * FROM collage_list_table ORDER BY priority asc")
//    List<CollageListSelectionData> getAllData_non_live();

}
