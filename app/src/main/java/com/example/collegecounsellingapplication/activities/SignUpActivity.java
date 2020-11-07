package com.example.collegecounsellingapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.collegecounsellingapplication.R;
import com.example.collegecounsellingapplication.modals.User;
import com.example.collegecounsellingapplication.retrofit.CollegeCounsellingApi;
import com.example.collegecounsellingapplication.utils.Tools;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.collegecounsellingapplication.activities.LoginActivity.BASE_URL;

public class SignUpActivity extends AppCompatActivity {
    private MaterialDatePicker materialDatePicker;
    private EditText rollNo, name, dob, password, confirmPassword;
    private ImageButton rollNoError, passwordError;
    private String rollNoText, nameText, dobText, passwordText, confirmPasswordText;
    private View parentView;
    private CollegeCounsellingApi collegeCounsellingApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
        iniVariables();
        iniComponents();
    }

    private void iniVariables() {
        parentView = findViewById(android.R.id.content);
        rollNo = findViewById(R.id.roll_no);
        name = findViewById(R.id.name);
        dob = findViewById(R.id.dob);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        rollNoError = findViewById(R.id.roll_no_error);
        passwordError = findViewById(R.id.password_error);
        MaterialDatePicker.Builder dateBuilder = MaterialDatePicker.Builder.datePicker();
        dateBuilder.setTitleText("Select your DOB");
//        dateRangeBuilder.setTheme(R.style.);
        dateBuilder.setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar);
        materialDatePicker = dateBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        collegeCounsellingApi = retrofit.create(CollegeCounsellingApi.class);
    }

    private void iniComponents() {

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                dob.setText(materialDatePicker.getHeaderText());
                dobText = materialDatePicker.getHeaderText();
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "date_picker");
            }
        });
        rollNoError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(parentView, "Roll Number must be of 10 digits", Snackbar.LENGTH_SHORT).show();
            }
        });

        passwordError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(parentView, "Password and Confirm password didn't match", Snackbar.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.go_to_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollNoText = rollNo.getText().toString().trim();
                nameText = name.getText().toString().trim();
                dobText = dob.getText().toString().trim();
                passwordText = password.getText().toString().trim();
                confirmPasswordText = confirmPassword.getText().toString().trim();

                if (rollNoText.isEmpty() || nameText.isEmpty() || dobText.isEmpty() || passwordText.isEmpty() || confirmPasswordText.isEmpty()) {

                    Snackbar.make(parentView, "Please fill all the fields", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (validateData()) {

                    //make server request
                    Call<List<User>> call = collegeCounsellingApi.createUser(new User(rollNoText, nameText, dobText, passwordText));
                    call.enqueue(new Callback<List<User>>() {
                        @Override
                        public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                            if(!response.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "User already Exist", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }

                        @Override
                        public void onFailure(Call<List<User>> call, Throwable t) {
                            Toast.makeText(SignUpActivity.this, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            private boolean validateData() {
                boolean flag = true;
                rollNoError.setVisibility(View.GONE);
                passwordError.setVisibility(View.GONE);
                if (!(rollNoText.length() == 10)) {
                    rollNoError.setVisibility(View.VISIBLE);
                    flag = false;
                }

                if (!(passwordText.equals(confirmPasswordText))) {
                    passwordError.setVisibility(View.VISIBLE);
                    flag = false;
                }
                return flag;
            }
        });


    }
}