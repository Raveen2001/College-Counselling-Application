package com.example.collegecounsellingapplication.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.collegecounsellingapplication.Dao.CollegeListDao;
import com.example.collegecounsellingapplication.RoomDatabase.CollegeListDatabase;
import com.example.collegecounsellingapplication.modals.College;

import java.util.List;

public class CollegeListRepository {
    private CollegeListDao collegeListDao;
    private LiveData<List<College>> allData;

    public CollegeListRepository(Application application) {
        CollegeListDatabase database = CollegeListDatabase.getInstance(application);
        collegeListDao = database.collageWithSeatDao();
        allData = collegeListDao.getAllData();
    }


    public void insert(College data) {
        new InsertAsyncTask(collegeListDao).execute(data);
    }

    public void delete(College data){
        new DeleteAsyncTask(collegeListDao).execute(data);
    }

    public void update(College data){
        new UpdateAsyncTask(collegeListDao).execute(data);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(collegeListDao).execute();
    }

    public void insertAll(List<College> data){
        new InsertAllAsyncTask(collegeListDao).execute(data);
    }


    public LiveData<List<College>> getAllData() {
        return allData;
    }


    public static class InsertAsyncTask extends AsyncTask<College, Void, Void> {
        private CollegeListDao collegeListDao;

        public InsertAsyncTask(CollegeListDao collegeListDao) {
            this.collegeListDao = collegeListDao;
        }

        @Override
        protected Void doInBackground(College... data) {
            collegeListDao.insert(data[0]);
            return null;
        }
    }

    public static class UpdateAsyncTask extends AsyncTask<College, Void, Void> {
        private CollegeListDao collegeListDao;

        public UpdateAsyncTask(CollegeListDao collegeListDao) {
            this.collegeListDao = collegeListDao;
        }

        @Override
        protected Void doInBackground(College... data) {
            collegeListDao.update(data[0]);
            return null;
        }
    }

    public static class DeleteAsyncTask extends AsyncTask<College, Void, Void> {
        private CollegeListDao collegeListDao;

        public DeleteAsyncTask(CollegeListDao collegeListDao) {
            this.collegeListDao = collegeListDao;
        }

        @Override
        protected Void doInBackground(College... data) {
            collegeListDao.delete(data[0]);
            return null;
        }
    }

    public static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private CollegeListDao collegeListDao;

        public DeleteAllAsyncTask(CollegeListDao collegeListSelectedDao) {
            this.collegeListDao = collegeListSelectedDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            collegeListDao.deleteAllData();
            return null;
        }
    }


    public static class InsertAllAsyncTask extends AsyncTask<List<College>, Void, Void> {
        private CollegeListDao collegeListDao;

        public InsertAllAsyncTask(CollegeListDao collegeListSelectedDao) {
            this.collegeListDao = collegeListSelectedDao;
        }

        @Override
        protected Void doInBackground(List<College>... lists) {
            collegeListDao.insertAllData(lists[0]);
            return null;
        }
    }



}

