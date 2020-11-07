package com.example.collegecounsellingapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.collegecounsellingapplication.R;
import com.example.collegecounsellingapplication.Repository.CollegeListSelectedRepository;
import com.example.collegecounsellingapplication.modals.User;
import com.example.collegecounsellingapplication.retrofit.CollegeCounsellingApi;
import com.example.collegecounsellingapplication.utils.Tools;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://192.168.31 .3:8080/";

    private EditText rollNo, password;
    private View parentView;
    private User user;
    public static String metaData = "META_DATA", metaDataUser ="META_DATA_USER", metaDataLoggedIn = "META_DATA_LOGGED_IN", metaDataIsAdmin = "META_DATA_IS_ADMIN", getMetaDataFilled = "META_DATA_FILLED";
    private CollegeCounsellingApi collegeCounsellingApi;
    private SharedPreferences mPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
        CollegeListSelectedRepository repo = new CollegeListSelectedRepository(getApplication());
        repo.deleteAll();
        iniVariables();
        iniComponents();

    }

    private void iniVariables() {
        parentView = findViewById(android.R.id.content);
        rollNo = findViewById(R.id.roll_no);
        password = findViewById(R.id.password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        collegeCounsellingApi = retrofit.create(CollegeCounsellingApi.class);
    }

    private void iniComponents() {
        ((View) findViewById(R.id.sign_up_for_account)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rollNoText = rollNo.getText().toString().trim();
                if(rollNoText.isEmpty()){
                    Snackbar.make(parentView,"Enter Roll no",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                String passwordText = password.getText().toString().trim();
                if(passwordText.isEmpty()){
                    Snackbar.make(parentView,"Enter password",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                //make server request

                Call<List<User>> call = collegeCounsellingApi.getUser(rollNoText);
                call.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        List<User> users= response.body();
                        if(!users.isEmpty()){
                            user = users.get(0);
                            mPrefs = getSharedPreferences(metaData,MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            Log.i("info2", "onResponse: json" + json);
                            prefsEditor.putString(metaDataUser, json);
                            prefsEditor.commit();
                            if(passwordText.equals(user.getPassword())){
                                Intent intent;
                                prefsEditor.putString(metaDataLoggedIn, "1");
                                prefsEditor.commit();
                                Log.i("info2", "login: " + mPrefs.getString(metaDataLoggedIn, ""));
                                if(user.isAdmin() == 0){
                                    if(user.getCutoff() == 0){
                                        intent = new Intent(LoginActivity.this, GetDetailsActivity.class);
                                    }else{
                                        intent = new Intent(LoginActivity.this, DashboardStudentActivity.class);
                                    }
                                }else{
                                    intent = new Intent(LoginActivity.this, DashboardAdminActivity.class);
                                }
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "Your password is wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(LoginActivity.this, "No user found with that Roll No... Try Sign Up", Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}