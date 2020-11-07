package com.example.collegecounsellingapplication.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.collegecounsellingapplication.R;
import com.example.collegecounsellingapplication.Repository.CollegeListSelectedRepository;
import com.example.collegecounsellingapplication.adapters.AdapterConfirmCollegeList;
import com.example.collegecounsellingapplication.modals.CollegeListFinalized;
import com.example.collegecounsellingapplication.modals.CollegeListSaved;
import com.example.collegecounsellingapplication.modals.User;
import com.example.collegecounsellingapplication.retrofit.CollegeCounsellingApi;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.collegecounsellingapplication.activities.DashboardStudentActivity.addBtn;
import static com.example.collegecounsellingapplication.activities.DashboardStudentActivity.saveBtn;
import static com.example.collegecounsellingapplication.activities.LoginActivity.BASE_URL;
import static com.example.collegecounsellingapplication.activities.LoginActivity.metaData;
import static com.example.collegecounsellingapplication.activities.LoginActivity.metaDataUser;

public class ConfirmListSubmissionActivity extends AppCompatActivity {

    private ImageButton closeBtn;
    private Button submitBtn;
    private RecyclerView recyclerView;
    private AdapterConfirmCollegeList adapterConfirmCollegeList;
    private CollegeCounsellingApi collegeCounsellingApi;
    private List<CollegeListFinalized> data = new ArrayList<>();
    private List<CollegeListSaved> saved =new ArrayList<>();
    private SharedPreferences mPrefs;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_list_submision);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        collegeCounsellingApi = retrofit.create(CollegeCounsellingApi.class);
        iniVariables();
        iniComponents();

    }

    private void iniVariables() {
        mPrefs = getSharedPreferences(metaData, MODE_PRIVATE);
        String json = mPrefs.getString(metaDataUser, "");
        Gson gson = new Gson();
        user = gson.fromJson(json, User.class);
        closeBtn = findViewById(R.id.btn_close);
        submitBtn = findViewById(R.id.btn_submit);
        Log.i("info2", "iniVariables: " + user.getIsListSubmitted());
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapterConfirmCollegeList = new AdapterConfirmCollegeList(this);
        if (user.getIsListSubmitted() == 1 || user.getIsRankPublished() == 1 || user.getIsResultPublished() == 1) {
            Log.i("info2", "iniVariables: inside if " + user.getIsListSubmitted());
            submitBtn.setVisibility(View.GONE);
            Call<List<CollegeListFinalized>> call = collegeCounsellingApi.getConfirmedCollegeList(user.getRollNo());
            call.enqueue(new Callback<List<CollegeListFinalized>>() {
                @Override
                public void onResponse(Call<List<CollegeListFinalized>> call, Response<List<CollegeListFinalized>> response) {
                    if(!(response.isSuccessful())){
                        Toast.makeText(ConfirmListSubmissionActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    data = response.body();

                    adapterConfirmCollegeList.setItems(data);
                }

                @Override
                public void onFailure(Call<List<CollegeListFinalized>> call, Throwable t) {
                    Toast.makeText(ConfirmListSubmissionActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Call<List<CollegeListSaved>> call = collegeCounsellingApi.getSavedCollegeList(user.getRollNo());
            call.enqueue(new Callback<List<CollegeListSaved>>() {
                @Override
                public void onResponse(Call<List<CollegeListSaved>> call, Response<List<CollegeListSaved>> response) {
                    if(!(response.isSuccessful())){
                        Toast.makeText(ConfirmListSubmissionActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    saved = response.body();
                    for(CollegeListSaved selected : saved){
                        CollegeListFinalized finalized = new CollegeListFinalized(user.getRollNo(), user.getName(), user.getCategoryId(), selected.getName(), selected.getCode(), selected.getDepartment(), selected.getPriority());
                        data.add(finalized);
                    }
                    adapterConfirmCollegeList.setItems(data);
                }

                @Override
                public void onFailure(Call<List<CollegeListSaved>> call, Throwable t) {
                    Toast.makeText(ConfirmListSubmissionActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                }
            });
        }




        recyclerView.setAdapter(adapterConfirmCollegeList);
    }

    private void iniComponents() {
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmListSubmissionActivity.this);
                builder.setTitle("Save your list to server ?");
                builder.setMessage("Once you save your list to the server you cant make changes there after!!");
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<List<User>> call = collegeCounsellingApi.submitChoiceList(data, user.getRollNo());

                        call.enqueue(new Callback<List<User>>() {
                            @Override
                            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                if (!(response.isSuccessful())) {
                                    Toast.makeText(ConfirmListSubmissionActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Toast.makeText(ConfirmListSubmissionActivity.this, "Saved to server.. you cant change your list here after", Toast.LENGTH_SHORT).show();
                                user = response.body().get(0);
                                Log.i("info", "onResponse: confirem " + user.getIsListSubmitted());
                                submitBtn.setVisibility(View.GONE);
                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(user);
                                prefsEditor.putString(metaDataUser, json);
                                prefsEditor.commit();
                                Log.i("info2", "iniVariables: confirmed " + user.getIsListSubmitted());
                                setResult(Activity.RESULT_OK);
                                finish();
                            }


                            @Override
                            public void onFailure(Call<List<User>> call, Throwable t) {
                                Toast.makeText(ConfirmListSubmissionActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });

    }
}