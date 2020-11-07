package com.example.collegecounsellingapplication.ViewModal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.collegecounsellingapplication.Repository.CollegeListRepository;
import com.example.collegecounsellingapplication.modals.College;
import com.example.collegecounsellingapplication.retrofit.CollegeCounsellingApi;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.collegecounsellingapplication.activities.LoginActivity.BASE_URL;

public class AdminDashboardViewModal extends AndroidViewModel {
    private CollegeListRepository collegeListRepository;
    private LiveData<List<College>> liveData;
    public CollegeCounsellingApi collegeCounsellingApi;
    public AdminDashboardViewModal(@NonNull Application application) {
        super(application);
        collegeListRepository = new CollegeListRepository(application);
        liveData = collegeListRepository.getAllData();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        collegeCounsellingApi = retrofit.create(CollegeCounsellingApi.class);
    }

    public void insert(College data){
        collegeListRepository.insert(data);
    }

    public void deleteAllData(){
        collegeListRepository.deleteAll();
    }

    public void delete(College data){
        collegeListRepository.delete(data);
    }

    public void insertAllData(List<College> data){
        collegeListRepository.insertAll(data);
    }

    public LiveData<List<College>> getLiveData() {
        return liveData;
    }

    public List<College> getAllData() {
        return liveData.getValue();
    }
}
