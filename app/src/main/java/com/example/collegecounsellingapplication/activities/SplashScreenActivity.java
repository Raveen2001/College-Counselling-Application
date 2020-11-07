package com.example.collegecounsellingapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegecounsellingapplication.R;
import com.example.collegecounsellingapplication.modals.User;
import com.example.collegecounsellingapplication.utils.Tools;
import com.google.gson.Gson;

import static com.example.collegecounsellingapplication.activities.LoginActivity.metaData;
import static com.example.collegecounsellingapplication.activities.LoginActivity.metaDataLoggedIn;
import static com.example.collegecounsellingapplication.activities.LoginActivity.metaDataUser;

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView logo;
    private TextView collageName, collageSlogan;
    private SharedPreferences mPrefs;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
        iniVariables();
        iniComponents();
    }

    private void iniVariables() {
        logo = findViewById(R.id.collageLogo);
        collageName= findViewById(R.id.collageName);
        collageSlogan = findViewById(R.id.collageSlogan);
        mPrefs = getSharedPreferences(metaData,MODE_PRIVATE);
        String json = mPrefs.getString(metaDataUser,"");
        Gson gson = new Gson();
        user = gson.fromJson(json, User.class);
    }

    private void iniComponents() {
        logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in));
        collageName.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in));
        collageSlogan.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in));
        moveToNextActivity();
    }


    public void moveToNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(isLoggedIn()){
                    if(user.isAdmin() == 0){
                        if(user.getCutoff() == 0 || user.getCategoryId() == 0){
                            intent = new Intent(SplashScreenActivity.this, GetDetailsActivity.class);
                        }else{
                            intent = new Intent(SplashScreenActivity.this, DashboardStudentActivity.class);
                        }

                    }else{
                        Toast.makeText(SplashScreenActivity.this, "Admin", Toast.LENGTH_SHORT).show();
                        intent = new Intent(SplashScreenActivity.this, DashboardAdminActivity.class);
                    }

                }else{
                    intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }

            private boolean isLoggedIn() {
                String isLoggedIn = mPrefs.getString(metaDataLoggedIn, "");
                Log.i("info2", "isLoggedIn: " + isLoggedIn);
                if(isLoggedIn.contains("1")){
                    return true;
                }
                return false;
            }
        }, 2000);
    }
}