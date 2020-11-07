package com.example.collegecounsellingapplication.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.collegecounsellingapplication.R;
import com.example.collegecounsellingapplication.Repository.CollegeListSelectedRepository;
import com.example.collegecounsellingapplication.adapters.AdapterCollegeSearch;
import com.example.collegecounsellingapplication.modals.College;
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

import static com.example.collegecounsellingapplication.activities.LoginActivity.BASE_URL;
import static com.example.collegecounsellingapplication.activities.LoginActivity.metaData;
import static com.example.collegecounsellingapplication.activities.LoginActivity.metaDataUser;

public class AddCollegeStudent extends AppCompatActivity {
    private List<College> collegeList = new ArrayList<>();
    private ImageButton closeBtn;
    private EditText search;
    private RecyclerView recyclerView;
    private AdapterCollegeSearch adapterCollageSearch;
    int selected_department_id = 1;
    private CollegeListSelectedRepository collegeListSelectedRepository;
    private SharedPreferences mPrefs;
    private User user;
    private CollegeCounsellingApi collegeCounsellingApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_college_student);
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

        collegeListSelectedRepository = new CollegeListSelectedRepository(getApplication());
//        collageList.add(new Collage("kongu eng collage", "5689"));
//        collageList.add(new Collage("ras eng collage", "7866"));
//        collageList.add(new Collage("afdsdfgu eng collage", "5644"));
//        collageList.add(new Collage("ewrt eng collage", "2364"));
//        collageList.add(new Collage("hfgjfd eng collage", "2356"));
        closeBtn = findViewById(R.id.bt_back);
        search = findViewById(R.id.et_search);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapterCollageSearch = new AdapterCollegeSearch(this, collegeList);
        adapterCollageSearch.setOnItemClickListener(new AdapterCollegeSearch.OnItemClickListener() {
            @Override
            public void onItemClick(View view, College obj, int position) {
                showDepartmentDialog(obj);
            }
        });


        Call<List<College>> call = collegeCounsellingApi.getColleges();
        call.enqueue(new Callback<List<College>>() {
            @Override
            public void onResponse(Call<List<College>> call, Response<List<College>> response) {
                if(!(response.isSuccessful())){
                    Toast.makeText(AddCollegeStudent.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(AddCollegeStudent.this, "Data Fetched", Toast.LENGTH_SHORT).show();
                collegeList = response.body();
                adapterCollageSearch.setItems(collegeList);
            }

            @Override
            public void onFailure(Call<List<College>> call, Throwable t) {
                Toast.makeText(AddCollegeStudent.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapterCollageSearch);
    }

    private void iniComponents() {
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<College> tempColleges = new ArrayList<>();
                String filterPattern = s.toString().toLowerCase();
                for (College college : collegeList) {
                    if (college.getName().toLowerCase().contains(filterPattern)  || college.getCode().startsWith(filterPattern)) {
                        tempColleges.add(college);
                    }
                }
                adapterCollageSearch = new AdapterCollegeSearch(AddCollegeStudent.this, tempColleges);
                adapterCollageSearch.setOnItemClickListener(new AdapterCollegeSearch.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, College obj, int position) {

                        showDepartmentDialog(obj);
                    }
                });
                recyclerView.setAdapter(adapterCollageSearch);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showDepartmentDialog(College obj) {
        String[] categories = {"Computer Science and Engineering","Mechanical Engineering", "Information Technology", "Electrical and Electronics Engineering", "Chemical Engineering", "Electronics and Communications Engineering", "Civil Engineering"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Departments");
        builder.setSingleChoiceItems(categories,0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selected_department_id = i + 1;
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AddCollegeStudent.this, categories[selected_department_id - 1], Toast.LENGTH_SHORT).show();
                CollegeListSaved data = new CollegeListSaved(obj.getName(),obj.getCode(), selected_department_id,user.getCategoryId());
                collegeListSelectedRepository.insert(data);
                finish();
            }
        });
        builder.setNegativeButton("CANCEL", null);
        builder.show();
    }
}