package com.example.collegecounsellingapplication.ViewModal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.collegecounsellingapplication.Repository.CollegeListSelectedRepository;
import com.example.collegecounsellingapplication.modals.CollegeListSaved;
import com.example.collegecounsellingapplication.retrofit.CollegeCounsellingApi;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.collegecounsellingapplication.activities.LoginActivity.BASE_URL;

public class StudentDashboardViewModel extends AndroidViewModel {
    private CollegeListSelectedRepository collegeListSelectedRepository;
    private LiveData<List<CollegeListSaved>> liveData;
    public CollegeCounsellingApi collegeCounsellingApi;
    public StudentDashboardViewModel(@NonNull Application application) {
        super(application);
        collegeListSelectedRepository = new CollegeListSelectedRepository(application);
        liveData = collegeListSelectedRepository.getAllData();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        collegeCounsellingApi = retrofit.create(CollegeCounsellingApi.class);
    }

    public void insert(CollegeListSaved data){
        collegeListSelectedRepository.insert(data);
    }

    public void deleteAllData(){
        collegeListSelectedRepository.deleteAll();
    }

    public void delete(CollegeListSaved data){
        collegeListSelectedRepository.delete(data);
    }

    public void insertAllData(List<CollegeListSaved> data){
        collegeListSelectedRepository.insertAll(data);
    }

    public LiveData<List<CollegeListSaved>> getLiveData() {
        return liveData;
    }

    public List<CollegeListSaved> getAllData() {
        return liveData.getValue();
    }
}
