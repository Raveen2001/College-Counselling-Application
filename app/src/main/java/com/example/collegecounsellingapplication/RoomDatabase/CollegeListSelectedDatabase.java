    package com.example.collegecounsellingapplication.RoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.collegecounsellingapplication.Dao.CollegeListSelectedDao;
import com.example.collegecounsellingapplication.modals.CollegeListSaved;

    @Database(entities = {CollegeListSaved.class}, version = 7)
public abstract class CollegeListSelectedDatabase extends RoomDatabase {
    private static CollegeListSelectedDatabase instance;

    public abstract CollegeListSelectedDao collageListDao();

    public static synchronized CollegeListSelectedDatabase getInstance(Context context){
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CollegeListSelectedDatabase.class, "database_table")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

}
