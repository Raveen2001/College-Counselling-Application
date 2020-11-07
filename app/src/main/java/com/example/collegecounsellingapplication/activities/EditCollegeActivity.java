package com.example.collegecounsellingapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.collegecounsellingapplication.R;
import com.example.collegecounsellingapplication.Repository.CollegeListRepository;
import com.example.collegecounsellingapplication.modals.College;
import com.example.collegecounsellingapplication.retrofit.CollegeCounsellingApi;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.collegecounsellingapplication.activities.DashboardAdminActivity.editIntentExtras;
import static com.example.collegecounsellingapplication.activities.LoginActivity.BASE_URL;

public class EditCollegeActivity extends AppCompatActivity {

    private EditText name, code, bc, mbc, oc, st, others;
    private String nameText, codeText;
    private int bcCount, mbcCount, ocCount, stCount, othersCount;
    private ImageButton close_btn;
    private Button save_btn;
    private CollegeCounsellingApi collegeCounsellingApi;
    private CollegeListRepository collegeListRepository;
    private College data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_college);
        iniVariables();
        iniComponents();
    }
    private void iniVariables() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        collegeCounsellingApi = retrofit.create(CollegeCounsellingApi.class);

        collegeListRepository = new CollegeListRepository(getApplication());

        Intent intent = getIntent();
        String json = intent.getStringExtra(editIntentExtras);
        Gson gson = new Gson();
        data = gson.fromJson(json, College.class);

        name = findViewById(R.id.name);
        code = findViewById(R.id.code);
        bc = findViewById(R.id.bc);
        mbc = findViewById(R.id.mbc);
        oc = findViewById(R.id.oc);
        st = findViewById(R.id.st);
        others = findViewById(R.id.others);
        close_btn = findViewById(R.id.btn_close);
        save_btn = findViewById(R.id.btn_save);


        //primary key
        code.setEnabled(false);
    }

    private void iniComponents() {

        name.setText(data.getName());
        code.setText(data.getCode());
        bc.setText(String.valueOf(data.getBc()));
        mbc.setText(String.valueOf(data.getMbc()));
        oc.setText(String.valueOf(data.getOc()));
        st.setText(String.valueOf(data.getSt()));
        others.setText(String.valueOf(data.getOthers()));


        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameText = name.getText().toString().trim();
                codeText = code.getText().toString().trim();
                bcCount = Integer.parseInt(bc.getText().toString().trim().equals("") ? "0" : bc.getText().toString());
                mbcCount = Integer.parseInt(mbc.getText().toString().trim().equals("") ? "0" : mbc.getText().toString());
                ocCount = Integer.parseInt(oc.getText().toString().trim().equals("") ? "0" : oc.getText().toString());
                stCount = Integer.parseInt(st.getText().toString().trim().equals("") ? "0" : st.getText().toString());
                othersCount = Integer.parseInt(others.getText().toString().trim().equals("") ? "0" : others.getText().toString());

                if(nameText.isEmpty()){
                    Toast.makeText(EditCollegeActivity.this, "Enter the college name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(codeText.isEmpty()){
                    Toast.makeText(EditCollegeActivity.this, "Enter the college code", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!(codeText.length() == 4)){
                    Toast.makeText(EditCollegeActivity.this, "College code should be of 4 numbers", Toast.LENGTH_SHORT).show();
                    return;
                }

                College data = new College(nameText, codeText, bcCount, mbcCount, ocCount, stCount, othersCount);

                Call<Void> call = collegeCounsellingApi.updateCollege(data);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(!(response.isSuccessful())){
                            Toast.makeText(EditCollegeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        collegeListRepository.update(data);
                        Toast.makeText(EditCollegeActivity.this, "College updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(EditCollegeActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }
}