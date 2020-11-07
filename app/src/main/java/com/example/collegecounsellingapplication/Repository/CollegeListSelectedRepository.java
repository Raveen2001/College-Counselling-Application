package com.example.collegecounsellingapplication.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.collegecounsellingapplication.Dao.CollegeListSelectedDao;
import com.example.collegecounsellingapplication.RoomDatabase.CollegeListSelectedDatabase;
import com.example.collegecounsellingapplication.modals.CollegeListSaved;

import java.util.List;

public class CollegeListSelectedRepository {
    private CollegeListSelectedDao collegeListSelectedDao;
    private LiveData<List<CollegeListSaved>> allData;

    public CollegeListSelectedRepository(Application application) {
        CollegeListSelectedDatabase database = CollegeListSelectedDatabase.getInstance(application);
        collegeListSelectedDao = database.collageListDao();
        allData = collegeListSelectedDao.getAllData();
    }


    public void insert(CollegeListSaved data) {
        new InsertAsyncTask(collegeListSelectedDao).execute(data);
    }

    public void delete(CollegeListSaved data){
        new DeleteAsyncTask(collegeListSelectedDao).execute(data);
    }
    public void deleteAll() {
        new DeleteAllAsyncTask(collegeListSelectedDao).execute();
    }

    public void insertAll(List<CollegeListSaved> data){
        new InsertAllAsyncTask(collegeListSelectedDao).execute(data);
    }

    public LiveData<List<CollegeListSaved>> getAllData() {
        return allData;
    }


//    public static class GetAsyncTask extends AsyncTask<Void, Void, List<CollageListSelectionData>>{
//        private CollageListDao collageListDao;
//        private List<CollageListSelectionData> allData;
//
//        public GetAsyncTask(CollageListDao collageListDao) {
//            this.collageListDao = collageListDao;
//        }
//
//        @Override
//        protected List<CollageListSelectionData> doInBackground(Void... voids) {
//            allData = collageListDao.getAllData_non_live();
//            return allData;
//        }
//
//    }

    public static class InsertAsyncTask extends AsyncTask<CollegeListSaved, Void, Void> {
        private CollegeListSelectedDao collegeListSelectedDao;

        public InsertAsyncTask(CollegeListSelectedDao collegeListSelectedDao) {
            this.collegeListSelectedDao = collegeListSelectedDao;
        }

        @Override
        protected Void doInBackground(CollegeListSaved... data) {
            collegeListSelectedDao.insert(data[0]);
            return null;
        }
    }

    public static class DeleteAsyncTask extends AsyncTask<CollegeListSaved, Void, Void> {
        private CollegeListSelectedDao collegeListSelectedDao;

        public DeleteAsyncTask(CollegeListSelectedDao collegeListSelectedDao) {
            this.collegeListSelectedDao = collegeListSelectedDao;
        }

        @Override
        protected Void doInBackground(CollegeListSaved... data) {
            collegeListSelectedDao.delete(data[0]);
            return null;
        }
    }

    public static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private CollegeListSelectedDao collegeListSelectedDao;

        public DeleteAllAsyncTask(CollegeListSelectedDao collegeListSelectedDao) {
            this.collegeListSelectedDao = collegeListSelectedDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            collegeListSelectedDao.deleteAllData();
            return null;
        }
    }


    public static class InsertAllAsyncTask extends AsyncTask<List<CollegeListSaved>, Void, Void> {
        private CollegeListSelectedDao collegeListSelectedDao;

        public InsertAllAsyncTask(CollegeListSelectedDao collegeListSelectedDao) {
            this.collegeListSelectedDao = collegeListSelectedDao;
        }

        @Override
        protected Void doInBackground(List<CollegeListSaved>... lists) {
            collegeListSelectedDao.insertAllData(lists[0]);
            return null;
        }
    }
}

