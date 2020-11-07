package com.example.collegecounsellingapplication.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegecounsellingapplication.R;
import com.example.collegecounsellingapplication.ViewModal.StudentDashboardViewModel;
import com.example.collegecounsellingapplication.adapters.AdapterCollegeListDrag;
import com.example.collegecounsellingapplication.adapters.AdapterConfirmCollegeList;
import com.example.collegecounsellingapplication.helper.DragItemTouchHelper;
import com.example.collegecounsellingapplication.modals.CollegeListFinalized;
import com.example.collegecounsellingapplication.modals.CollegeListSaved;
import com.example.collegecounsellingapplication.modals.Result;
import com.example.collegecounsellingapplication.modals.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.collegecounsellingapplication.activities.LoginActivity.metaData;
import static com.example.collegecounsellingapplication.activities.LoginActivity.metaDataLoggedIn;
import static com.example.collegecounsellingapplication.activities.LoginActivity.metaDataUser;

public class DashboardStudentActivity extends AppCompatActivity {
    private ImageButton logout;
    private LinearLayout view_mark_sheet, my_list, view_result;
    private RecyclerView recyclerView;
    private AdapterCollegeListDrag mAdapter;
    private Toolbar toolbar;
    private ItemTouchHelper mItemTouchHelper;
    private StudentDashboardViewModel studentDashboardViewModel;
    private List<CollegeListSaved> collageList = new ArrayList<>();
    private SharedPreferences mPrefs;
    private User user;
    public static ImageButton saveBtn, addBtn;
    private TextView rank, rollNo, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_student);
        iniVariables();
        iniComponents();
    }

    private void iniVariables() {

        mPrefs = getSharedPreferences(metaData, MODE_PRIVATE);
        String json = mPrefs.getString(metaDataUser, "");
        Gson gson = new Gson();
        user = gson.fromJson(json, User.class);
        logout = findViewById(R.id.log_out);
        view_mark_sheet = findViewById(R.id.mark_sheet);
        my_list = findViewById(R.id.my_list);
        view_result = findViewById(R.id.view_result);
        recyclerView = findViewById(R.id.recycler_view);
        studentDashboardViewModel = new ViewModelProvider(this).get(StudentDashboardViewModel.class);
        mPrefs = getSharedPreferences(metaData, MODE_PRIVATE);

        Call<List<User>> userCall = studentDashboardViewModel.collegeCounsellingApi.getUser(user.getRollNo());
        userCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(!(response.isSuccessful())){
                    Toast.makeText(DashboardStudentActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!(response.body().isEmpty())) {

                    user = response.body().get(0);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(user);
                    prefsEditor.putString(metaDataUser, json);
                    prefsEditor.commit();
                    Log.i("info2", "user fetched: " + json);
                    rank = findViewById(R.id.rank);
                    if (user.getIsRankPublished() == 0) {
                        rank.setText("-");
                    } else {
                        rank.setText(String.valueOf(user.getRank()));
                    }
                    if (user.getIsListSubmitted() == 1 || user.getIsRankPublished() == 1 || user.getIsResultPublished() == 1) {
                        findViewById(R.id.save).setVisibility(View.GONE);
                        findViewById(R.id.add).setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(DashboardStudentActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.name);
        name.setText(user.getName());
        saveBtn = findViewById(R.id.save);
        addBtn = findViewById(R.id.add);

        rank = findViewById(R.id.rank);
        if (user.getIsRankPublished() == 0) {
            rank.setText("-");
        } else {
            rank.setText(String.valueOf(user.getRank()));
        }

        rollNo = findViewById(R.id.roll_no);
        rollNo.setText("#" + user.getRollNo());
        if (user.getIsListSubmitted() == 1 || user.getIsRankPublished() == 1 || user.getIsResultPublished() == 1) {
            findViewById(R.id.save).setVisibility(View.GONE);
            findViewById(R.id.add).setVisibility(View.GONE);
        }
    }

    private void iniComponents() {
//        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Call<List<User>> userCall = studentDashboardViewModel.collegeCounsellingApi.getUser(user.getRollNo());
//                userCall.enqueue(new Callback<List<User>>() {
//                    @Override
//                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
//                        if (!(response.isSuccessful())) {
//                            Toast.makeText(DashboardStudentActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        if (!(response.body().isEmpty())) {
//                            user = response.body().get(0);
//                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
//                            Gson gson = new Gson();
//                            String json = gson.toJson(user);
//                            prefsEditor.putString(metaDataUser, json);
//                            prefsEditor.commit();
//                            Log.i("info2", "user fetched: " + json);
//                            rank = findViewById(R.id.rank);
//                            if (user.getIsRankPublished() == 0) {
//                                rank.setText("-");
//                            } else {
//                                rank.setText(String.valueOf(user.getRank()));
//                            }
//                            if (user.getIsListSubmitted() == 1 || user.getIsRankPublished() == 1 || user.getIsResultPublished() == 1) {
//                                findViewById(R.id.save).setVisibility(View.GONE);
//                                findViewById(R.id.add).setVisibility(View.GONE);
//                            }
//
//                            recyclerView.setLayoutManager(new LinearLayoutManager(DashboardStudentActivity.this));
//                            recyclerView.setHasFixedSize(true);
//                            mAdapter = new AdapterCollegeListDrag(DashboardStudentActivity.this);
//                            if (user.getIsListSubmitted() == 1 || user.getIsRankPublished() == 1 || user.getIsResultPublished() == 1) {
//                                Call<List<CollegeListFinalized>> call1 = studentDashboardViewModel.collegeCounsellingApi.getConfirmedCollegeList(user.getRollNo());
//                                call1.enqueue(new Callback<List<CollegeListFinalized>>() {
//                                    @Override
//                                    public void onResponse(Call<List<CollegeListFinalized>> call, Response<List<CollegeListFinalized>> response) {
//                                        if (!(response.isSuccessful())) {
//                                            Toast.makeText(DashboardStudentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                                        }
//                                        AdapterConfirmCollegeList adapter = new AdapterConfirmCollegeList(DashboardStudentActivity.this);
//                                        adapter.setItems(response.body());
//                                        recyclerView.setAdapter(adapter);
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<List<CollegeListFinalized>> call, Throwable t) {
//                                        Toast.makeText(DashboardStudentActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            } else {
//                                Call<List<CollegeListSaved>> call1 = studentDashboardViewModel.collegeCounsellingApi.getSavedCollegeList(user.getRollNo());
//                                call1.enqueue(new Callback<List<CollegeListSaved>>() {
//                                    @Override
//                                    public void onResponse(Call<List<CollegeListSaved>> call, Response<List<CollegeListSaved>> response) {
//                                        if(!(response.isSuccessful())){
//                                            Toast.makeText(DashboardStudentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                                            return;
//                                        }
//                                        studentDashboardViewModel.deleteAllData();
//                                        studentDashboardViewModel.insertAllData(response.body());
//                                        Toast.makeText(DashboardStudentActivity.this, "Data Fetched", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<List<CollegeListSaved>> call, Throwable t) {
//                                        Toast.makeText(DashboardStudentActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<User>> call, Throwable t) {
//                        Toast.makeText(DashboardStudentActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//        });



//        findViewById(R.id.developer_info).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialogDeveloper();
//            }
//        });



//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor prefsEditor = mPrefs.edit();
//                prefsEditor.putString(metaDataLoggedIn, "0");
//                prefsEditor.putString(metaData, "");
//                prefsEditor.putString(metaDataUser, "");
//                prefsEditor.commit();
//                Intent intent = new Intent(DashboardStudentActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        view_mark_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardStudentActivity.this, ViewMark.class);
                startActivity(intent);
            }
        });

        my_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardStudentActivity.this, ConfirmListSubmissionActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        view_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getIsResultPublished() == 1) {

                    Call<List<Result>> resultCall = studentDashboardViewModel.collegeCounsellingApi.getResult(user.getRollNo());
                    resultCall.enqueue(new Callback<List<Result>>() {
                        @Override
                        public void onResponse(Call<List<Result>> call, Response<List<Result>> response) {
                            if(!(response.isSuccessful())){
                                Toast.makeText(DashboardStudentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(response.body().isEmpty()){
                                showResultFailDialog();
                            }else{
                                Result result = response.body().get(0);
                                showResultSuccessDialog(result);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Result>> call, Throwable t) {

                        }
                    });
                } else {
                    showResultNotPublished();
                }
            }
        });


        //request server for data

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new AdapterCollegeListDrag(this);
        if (user.getIsListSubmitted() == 1 || user.getIsRankPublished() == 1 || user.getIsResultPublished() == 1) {
            Call<List<CollegeListFinalized>> call1 = studentDashboardViewModel.collegeCounsellingApi.getConfirmedCollegeList(user.getRollNo());
            call1.enqueue(new Callback<List<CollegeListFinalized>>() {
                @Override
                public void onResponse(Call<List<CollegeListFinalized>> call, Response<List<CollegeListFinalized>> response) {
                    if (!(response.isSuccessful())) {
                        Toast.makeText(DashboardStudentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                    AdapterConfirmCollegeList adapter = new AdapterConfirmCollegeList(DashboardStudentActivity.this);
                    adapter.setItems(response.body());
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<CollegeListFinalized>> call, Throwable t) {
                    Toast.makeText(DashboardStudentActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Call<List<CollegeListSaved>> call = studentDashboardViewModel.collegeCounsellingApi.getSavedCollegeList(user.getRollNo());
            call.enqueue(new Callback<List<CollegeListSaved>>() {
                @Override
                public void onResponse(Call<List<CollegeListSaved>> call, Response<List<CollegeListSaved>> response) {
                    if(!(response.isSuccessful())){
                        Toast.makeText(DashboardStudentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    studentDashboardViewModel.deleteAllData();
                    studentDashboardViewModel.insertAllData(response.body());
                    Toast.makeText(DashboardStudentActivity.this, "Data Fetched", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<List<CollegeListSaved>> call, Throwable t) {
                    Toast.makeText(DashboardStudentActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                }
            });
        }

        studentDashboardViewModel.getLiveData().observe(this, new Observer<List<CollegeListSaved>>() {
            @Override
            public void onChanged(List<CollegeListSaved> collageListSelectionData) {
                Log.i("info 2", "onChanged: is list submitted" + user.getIsListSubmitted());
                if (user.getIsListSubmitted() == 1 || user.getIsRankPublished() == 1 || user.getIsResultPublished() == 1) {
                    Call<List<CollegeListFinalized>> call = studentDashboardViewModel.collegeCounsellingApi.getConfirmedCollegeList(user.getRollNo());
                    call.enqueue(new Callback<List<CollegeListFinalized>>() {
                        @Override
                        public void onResponse(Call<List<CollegeListFinalized>> call, Response<List<CollegeListFinalized>> response) {
                            if (!(response.isSuccessful())) {
                                Toast.makeText(DashboardStudentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                            AdapterConfirmCollegeList adapter = new AdapterConfirmCollegeList(DashboardStudentActivity.this);
                            adapter.setItems(response.body());
                            Log.i("info2", "onResponse: you are wrong it is executed");
                            recyclerView.setAdapter(adapter);
                            saveBtn.setVisibility(View.GONE);
                            addBtn.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<List<CollegeListFinalized>> call, Throwable t) {
                            Toast.makeText(DashboardStudentActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    mAdapter.setItems(collageListSelectionData);
                    recyclerView.setAdapter(mAdapter);
                }
            }
        });

        ItemTouchHelper.Callback callback = new DragItemTouchHelper(mAdapter, mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        mAdapter.setDragListener(new AdapterCollegeListDrag.OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }

        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CollegeListSaved> updatedData = mAdapter.getItems();
                int priority = 1;
                for (CollegeListSaved data : updatedData) {
                    data.setPriority(priority++);
                }
                studentDashboardViewModel.deleteAllData();
                studentDashboardViewModel.insertAllData(updatedData);
                List<CollegeListSaved> data = studentDashboardViewModel.getAllData();
                Call<Void> call = studentDashboardViewModel.collegeCounsellingApi.saveChoiceList(data, user.getRollNo());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!(response.isSuccessful())) {
                            Toast.makeText(DashboardStudentActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(DashboardStudentActivity.this, "saved", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(DashboardStudentActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardStudentActivity.this, AddCollegeStudent.class);
                startActivity(intent);
            }
        });

    }

    private void showResultSuccessDialog(Result data) {
        TextView name, rollNo, rank, choice_no, collage_name, code, department;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_result_success);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        name = dialog.findViewById(R.id.name);
        rollNo = dialog.findViewById(R.id.roll_no);
        rank = dialog.findViewById(R.id.rank);
        choice_no = dialog.findViewById(R.id.choice_no);
        collage_name = dialog.findViewById(R.id.allotted_collage);
        code = dialog.findViewById(R.id.code);
        department = dialog.findViewById(R.id.department);

        name.setText(data.getName());
        rollNo.setText("#" + data.getRollNo());
        rank.setText(String.valueOf(user.getRank()));
        choice_no.setText(String.valueOf((data.getChoice_no())));
        collage_name.setText(data.getCollegeName());
        code.setText("#" + data.getCode());
        department.setText(getDepartment(data.getDepartment()));

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_okay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void showResultFailDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_results_fail);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showResultNotPublished() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_results_not_available);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void showDialogDeveloper() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_contact_developer);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    private String getDepartment(int id) {
        String departmentName;
        switch (id) {
            case 1:
                departmentName = "Computer Science and Engineering";
                break;
            case 2:
                departmentName = "Mechanical Engineering";
                break;
            case 3:
                departmentName = "Information Technology";
                break;
            case 4:
                departmentName = "Electrical and Electronics Engineering";
                break;
            case 5:
                departmentName = "Chemical Engineering";
                break;
            case 6:
                departmentName = "Electronics and Communications Engineering";
                break;
            case 7:
                departmentName = "Civil Engineering";
                break;
            default:
                departmentName = "error";
                break;
        }
        return departmentName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                Log.i("info2", "onActivityResult" );
                Call<List<CollegeListFinalized>> call = studentDashboardViewModel.collegeCounsellingApi.getConfirmedCollegeList(user.getRollNo());
                call.enqueue(new Callback<List<CollegeListFinalized>>() {
                    @Override
                    public void onResponse(Call<List<CollegeListFinalized>> call, Response<List<CollegeListFinalized>> response) {
                        if (!(response.isSuccessful())) {
                            Toast.makeText(DashboardStudentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                        AdapterConfirmCollegeList adapter = new AdapterConfirmCollegeList(DashboardStudentActivity.this);
                        adapter.setItems(response.body());
                        Log.i("info2", "onResponse: you are wrong it is executed");
                        recyclerView.setAdapter(adapter);
                        saveBtn.setVisibility(View.GONE);
                        addBtn.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<CollegeListFinalized>> call, Throwable t) {
                        Toast.makeText(DashboardStudentActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.refresh){
            Call<List<User>> userCall = studentDashboardViewModel.collegeCounsellingApi.getUser(user.getRollNo());
            userCall.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (!(response.isSuccessful())) {
                        Toast.makeText(DashboardStudentActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!(response.body().isEmpty())) {
                        user = response.body().get(0);
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(user);
                        prefsEditor.putString(metaDataUser, json);
                        prefsEditor.commit();
                        Log.i("info2", "user fetched: " + json);
                        rank = findViewById(R.id.rank);
                        if (user.getIsRankPublished() == 0) {
                            rank.setText("-");
                        } else {
                            rank.setText(String.valueOf(user.getRank()));
                        }
                        if (user.getIsListSubmitted() == 1 || user.getIsRankPublished() == 1 || user.getIsResultPublished() == 1) {
                            findViewById(R.id.save).setVisibility(View.GONE);
                            findViewById(R.id.add).setVisibility(View.GONE);
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardStudentActivity.this));
                        recyclerView.setHasFixedSize(true);
                        mAdapter = new AdapterCollegeListDrag(DashboardStudentActivity.this);
                        if (user.getIsListSubmitted() == 1 || user.getIsRankPublished() == 1 || user.getIsResultPublished() == 1) {
                            Call<List<CollegeListFinalized>> call1 = studentDashboardViewModel.collegeCounsellingApi.getConfirmedCollegeList(user.getRollNo());
                            call1.enqueue(new Callback<List<CollegeListFinalized>>() {
                                @Override
                                public void onResponse(Call<List<CollegeListFinalized>> call, Response<List<CollegeListFinalized>> response) {
                                    if (!(response.isSuccessful())) {
                                        Toast.makeText(DashboardStudentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                    AdapterConfirmCollegeList adapter = new AdapterConfirmCollegeList(DashboardStudentActivity.this);
                                    adapter.setItems(response.body());
                                    recyclerView.setAdapter(adapter);
                                }

                                @Override
                                public void onFailure(Call<List<CollegeListFinalized>> call, Throwable t) {
                                    Toast.makeText(DashboardStudentActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Call<List<CollegeListSaved>> call1 = studentDashboardViewModel.collegeCounsellingApi.getSavedCollegeList(user.getRollNo());
                            call1.enqueue(new Callback<List<CollegeListSaved>>() {
                                @Override
                                public void onResponse(Call<List<CollegeListSaved>> call, Response<List<CollegeListSaved>> response) {
                                    if(!(response.isSuccessful())){
                                        Toast.makeText(DashboardStudentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    studentDashboardViewModel.deleteAllData();
                                    studentDashboardViewModel.insertAllData(response.body());
                                    Log.i("info2", "onResponse: college list fetched");
                                }

                                @Override
                                public void onFailure(Call<List<CollegeListSaved>> call, Throwable t) {
                                    Toast.makeText(DashboardStudentActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Toast.makeText(DashboardStudentActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                }
            });
        }else if(item.getItemId()== R.id.about_developer){
            showDialogDeveloper();
        }else if(item.getItemId() == R.id.log_out){
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.putString(metaDataLoggedIn, "0");
            prefsEditor.putString(metaData, "");
            prefsEditor.putString(metaDataUser, "");
            prefsEditor.commit();
            Intent intent = new Intent(DashboardStudentActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}