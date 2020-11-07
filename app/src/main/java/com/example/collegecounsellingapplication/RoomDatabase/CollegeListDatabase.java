    package com.example.collegecounsellingapplication.RoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.collegecounsellingapplication.Dao.CollegeListDao;
import com.example.collegecounsellingapplication.modals.College;

    @Database(entities = {College.class}, version = 2)
    public abstract class CollegeListDatabase extends RoomDatabase {
        private static CollegeListDatabase instance;

        public abstract CollegeListDao collageWithSeatDao();

        public static synchronized CollegeListDatabase getInstance(Context context){
            if(instance == null) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        CollegeListDatabase.class, "database_table")
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return instance;
        }
    }
