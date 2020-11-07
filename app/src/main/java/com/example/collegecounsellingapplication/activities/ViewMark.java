package com.example.collegecounsellingapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegecounsellingapplication.R;
import com.example.collegecounsellingapplication.modals.Mark;
import com.example.collegecounsellingapplication.modals.User;
import com.example.collegecounsellingapplication.retrofit.CollegeCounsellingApi;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.collegecounsellingapplication.activities.LoginActivity.BASE_URL;
import static com.example.collegecounsellingapplication.activities.LoginActivity.metaData;
import static com.example.collegecounsellingapplication.activities.LoginActivity.metaDataUser;

public class ViewMark extends AppCompatActivity {

    private LinearLayout csLayout, bioLayout;
    private TextView english, tamil, maths, physics, chemistry, cs, bio, cutoff, total;
    private User user;
    private Mark mark;
    private SharedPreferences mPrefs;
    private CollegeCounsellingApi collegeCounsellingApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mark);
        iniVariables();
        iniComponents();
    }

    private void iniVariables() {
        mPrefs = getSharedPreferences(metaData,MODE_PRIVATE);
        String json = mPrefs.getString(metaDataUser,"");
        Gson gson = new Gson();
        user = gson.fromJson(json, User.class);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        collegeCounsellingApi = retrofit.create(CollegeCounsellingApi.class);

        csLayout = findViewById(R.id.csLayout);
        bioLayout = findViewById(R.id.bioLayout);
        english = findViewById(R.id.english);
        tamil = findViewById(R.id.tamil);
        maths = findViewById(R.id.maths);
        physics = findViewById(R.id.physics);
        chemistry = findViewById(R.id.chemistry);
        bio = findViewById(R.id.biology);
        cs = findViewById(R.id.cs);
        total = findViewById(R.id.total);
        cutoff = findViewById(R.id.cutoff);
    }

    private void iniComponents() {
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Call<List<Mark>> call = collegeCounsellingApi.getMark(user.getRollNo());
        call.enqueue(new Callback<List<Mark>>() {
            @Override
            public void onResponse(Call<List<Mark>> call, Response<List<Mark>> response) {
                if(!(response.isSuccessful())){
                    Toast.makeText(ViewMark.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                mark = response.body().get(0);
                english.setText(String.valueOf((int)mark.getEnglish()));
                tamil.setText(String.valueOf((int)mark.getTamil()));
                maths.setText(String.valueOf((int)mark.getMaths()));
                chemistry.setText(String.valueOf((int)mark.getChemistry()));
                physics.setText(String.valueOf((int)mark.getPhysics()));

                if(mark.getBio() == 0){
                    bioLayout.setVisibility(View.GONE);
                    cs.setText(String.valueOf((int)mark.getCs()));
                }else{
                    csLayout.setVisibility(View.GONE);
                    bio.setText(String.valueOf((int)mark.getBio()));
                }

                total.setText(String.valueOf((int)mark.getTotal()));
                cutoff.setText(String.valueOf(mark.getCutoff()));
            }

            @Override
            public void onFailure(Call<List<Mark>> call, Throwable t) {
                Toast.makeText(ViewMark.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}