package com.example.collegecounsellingapplication.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class GetDetailsActivity extends AppCompatActivity {
    private float cutoffMark = 0f;
    private float englishMark = 0f, tamilMark = 0f, mathsMark = 0f, physicsMark = 0f, chemistryMark = 0f, computerScienceMark = 0f, biologyMark = 0f;
    private EditText english, tamil, maths, physics, chemistry, computerScience, biology, category;
    private TextView cutoff;
    private Button submitBtn;
    private Toolbar toolbar;
    private int selected_category_id;
    private User user;
    private CollegeCounsellingApi collegeCounsellingApi;
    private SharedPreferences mPrefs;
    public static String metaDataMark = "META_DATA_MARK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_details);
        iniVariables();
        iniComponents();
    }

    private void iniVariables() {
        toolbar = findViewById(R.id.toolbar);
        english = findViewById(R.id.english);
        tamil = findViewById(R.id.tamil);
        maths = findViewById(R.id.maths);
        physics = findViewById(R.id.physics);
        chemistry = findViewById(R.id.chemistry);
        computerScience = findViewById(R.id.computer_science);
        biology = findViewById(R.id.biology);
        category = findViewById(R.id.category);
        cutoff = findViewById(R.id.cutoff);
        submitBtn = findViewById(R.id.btn_submit);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        collegeCounsellingApi = retrofit.create(CollegeCounsellingApi.class);

         mPrefs = getSharedPreferences(metaData,MODE_PRIVATE);
         String json = mPrefs.getString(metaDataUser,"");
         Gson gson = new Gson();
         user = gson.fromJson(json, User.class);

    }


    private void iniComponents() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("Hi, " + user.getName());
        Log.i("info2", "iniVariables: " + user.getName());
        english.addTextChangedListener(new TextWatcher() {
            private int preInput;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                preInput = Integer.parseInt(s.toString().isEmpty() ? "0" : s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int mark = Integer.parseInt(s.toString().isEmpty() ? "0" : s.toString());
                if (mark > 200) {
                    Toast.makeText(GetDetailsActivity.this, "Mark can't exceed 200", Toast.LENGTH_SHORT).show();
                    english.setText(String.valueOf(preInput));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tamil.addTextChangedListener(new TextWatcher() {
            private int preInput;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                preInput = Integer.parseInt(s.toString().isEmpty() ? "0" : s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int mark = Integer.parseInt(s.toString().isEmpty() ? "0" : s.toString());
                if (mark > 200) {
                    Toast.makeText(GetDetailsActivity.this, "Mark can't exceed 200", Toast.LENGTH_SHORT).show();
                    tamil.setText(String.valueOf(preInput));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        maths.addTextChangedListener(new TextWatcher() {
            private int preInput;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                preInput = Integer.parseInt(s.toString().isEmpty() ? "0" : s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float mark = Integer.parseInt(s.toString().isEmpty() ? "0" : s.toString());
                if (mark <= 200) {
                    cutoffMark -= mathsMark;
                    mathsMark = mark / 2;
                    cutoffMark += mathsMark;
                    cutoff.setText(String.valueOf(cutoffMark));
                } else {
                    Toast.makeText(GetDetailsActivity.this, "Mark can't exceed 200", Toast.LENGTH_SHORT).show();
                    maths.setText(String.valueOf(preInput));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        physics.addTextChangedListener(new TextWatcher() {
            private int preInput;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                preInput = Integer.parseInt(s.toString().isEmpty() ? "0" : s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float mark = Integer.parseInt(s.toString().isEmpty() ? "0" : s.toString());
                if (mark <= 200) {
                    cutoffMark -= physicsMark;
                    physicsMark = mark / 4;
                    cutoffMark += physicsMark;
                    cutoff.setText(String.valueOf(cutoffMark));
                } else {
                    Toast.makeText(GetDetailsActivity.this, "Mark can't exceed 200", Toast.LENGTH_SHORT).show();
                    physics.setText(String.valueOf(preInput));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        chemistry.addTextChangedListener(new TextWatcher() {
            private int preInput;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                preInput = Integer.parseInt(s.toString().isEmpty() ? "0" : s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float mark = Integer.parseInt(s.toString().isEmpty() ? "0" : s.toString());
                if (mark <= 200) {
                    cutoffMark -= chemistryMark;
                    chemistryMark = mark / 4;
                    cutoffMark += chemistryMark;
                    cutoff.setText(String.valueOf(cutoffMark));
                } else {
                    Toast.makeText(GetDetailsActivity.this, "Mark can't exceed 200", Toast.LENGTH_SHORT).show();
                    chemistry.setText(String.valueOf(preInput));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        computerScience.addTextChangedListener(new TextWatcher() {
            private int preInput;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                preInput = Integer.parseInt(s.toString().isEmpty() ? "0" : s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    biology.setEnabled(true);
                } else {
                    biology.setEnabled(false);
                }

                int mark = Integer.parseInt(s.toString().isEmpty() ? "0" : s.toString());
                if (mark > 200) {
                    Toast.makeText(GetDetailsActivity.this, "Mark can't exceed 200", Toast.LENGTH_SHORT).show();
                    computerScience.setText(String.valueOf(preInput));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        biology.addTextChangedListener(new TextWatcher() {
            private int preInput;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                preInput = Integer.parseInt(s.toString().isEmpty() ? "0" : s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    computerScience.setEnabled(true);
                } else {
                    computerScience.setEnabled(false);
                }

                int mark = Integer.parseInt(s.toString().isEmpty() ? "0" : s.toString());
                if (mark > 200) {
                    Toast.makeText(GetDetailsActivity.this, "Mark can't exceed 200", Toast.LENGTH_SHORT).show();
                    biology.setText(String.valueOf(preInput));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoriesDialog();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            String englishM, tamilM, physicsM, mathsM, chemistryM, csM, biologyM;

            @Override
            public void onClick(View v) {
                englishM = english.getText().toString().trim();
                tamilM = tamil.getText().toString().trim();
                mathsM = maths.getText().toString().trim();
                physicsM = physics.getText().toString().trim();
                chemistryM = chemistry.getText().toString().trim();
                csM = computerScience.getText().toString().trim();
                biologyM = biology.getText().toString().trim();



                if (englishM.isEmpty() || tamilM.isEmpty() || mathsM.isEmpty() || physicsM.isEmpty() || chemistryM.isEmpty() || selected_category_id == 0 ) {
                    Toast.makeText(GetDetailsActivity.this, "please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (computerScience.isEnabled() && biology.isEnabled()) {
                    Toast.makeText(GetDetailsActivity.this, "please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                englishMark = Integer.parseInt(englishM);
                tamilMark = Integer.parseInt(tamilM);
                mathsMark = Integer.parseInt(mathsM);
                physicsMark = Integer.parseInt(physicsM);
                chemistryMark = Integer.parseInt(chemistryM);
                if(computerScience.isEnabled()){
                    computerScienceMark = Integer.parseInt(csM);
                }
                else if(biology.isEnabled()){
                    biologyMark = Integer.parseInt(biologyM);
                }


                //cache locally


                if(englishMark >=70 && tamilMark >= 70 && mathsMark >=70 && physicsMark >= 70 && chemistryMark >= 70 && ((!(biologyMark>=70) && computerScienceMark>=70) || ((biologyMark>=70) && !(computerScienceMark>=70)))){
                    Call<List<User>> call = collegeCounsellingApi.updateDetails(user.getRollNo(),cutoffMark, selected_category_id);

                    call.enqueue(new Callback<List<User>>() {
                        @Override
                        public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(GetDetailsActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            List<User> users = response.body();
                            if (!users.isEmpty()) {
                                user = users.get(0);
                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(user);
                                prefsEditor.putString(metaDataUser, json);
                                prefsEditor.commit();
                                Log.i("info2", "onResponse: " + user.getCutoff());
                                Mark mark = new Mark(user.getRollNo(), englishMark, tamilMark, mathsMark, physicsMark, chemistryMark,cutoffMark);
                                mark.setTotal(englishMark + tamilMark + mathsMark + chemistryMark + physicsMark);
                                if(computerScience.isEnabled()){
                                    mark.setCs(computerScienceMark);
                                    mark.setTotal(mark.getTotal() + computerScienceMark);
                                }
                                else if(biology.isEnabled()){
                                    mark.setBio(biologyMark);
                                    mark.setTotal(mark.getTotal() + biologyMark);
                                }
                                gson = new Gson();
                                json = gson.toJson(mark);
                                prefsEditor.putString(metaDataMark, json);
                                prefsEditor.commit();
                                Call<List<Mark>> call2 = collegeCounsellingApi.submitMark(mark, user.getRollNo());
                                call2.enqueue(new Callback<List<Mark>>() {
                                    @Override
                                    public void onResponse(Call<List<Mark>> call, Response<List<Mark>> response) {
                                        if(!(response.isSuccessful())){
                                            Toast.makeText(GetDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                        Intent intent = new Intent(GetDetailsActivity.this, DashboardStudentActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<List<Mark>> call, Throwable t) {
                                        Toast.makeText(GetDetailsActivity.this, "Can't make request to the server please try again later", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                        @Override
                        public void onFailure(Call<List<User>> call, Throwable t) {
                            Toast.makeText(GetDetailsActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                        }
                    });


                }else{
                    Toast.makeText(GetDetailsActivity.this, "You didn't pass the exam", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void showCategoriesDialog() {
        String[] categories = {"OC","BC", "MBC", "ST", "Others"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Categories");
        builder.setSingleChoiceItems(categories, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selected_category_id = i + 1;

            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                category.setText(categories[selected_category_id - 1]);
                Toast.makeText(GetDetailsActivity.this, categories[selected_category_id - 1], Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("CANCEL", null);
        builder.show();

    }
}