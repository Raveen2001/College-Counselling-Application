package com.example.collegecounsellingapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegecounsellingapplication.R;
import com.example.collegecounsellingapplication.ViewModal.AdminDashboardViewModal;
import com.example.collegecounsellingapplication.adapters.AdapterCollegeListAdmin;
import com.example.collegecounsellingapplication.adapters.AdapterCollegeListAdminFinalized;
import com.example.collegecounsellingapplication.modals.College;
import com.example.collegecounsellingapplication.modals.CollegeListFinalized;
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

public class DashboardAdminActivity extends AppCompatActivity {

    private ImageButton logout;
    private LinearLayout publish_rank, publish_result, view_result;
    private EditText search;
    private RecyclerView recyclerView;
    private AdapterCollegeListAdmin mAdapter;
    private Toolbar toolbar;
    private AdminDashboardViewModal adminDashboardViewModal;
    private SharedPreferences mPrefs;
    private User user;
    private TextView name;
    public static final String editIntentExtras = "COLLAGE_DATA";
    private List<College> items;


    List<User> students = new ArrayList<>();
    List<CollegeListFinalized> studentCollegeList = new ArrayList();
    College college;
    Result result;
    int i, j;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);
        iniVariables();
        iniComponents();
    }

    private void iniVariables() {

        mPrefs = getSharedPreferences(metaData, MODE_PRIVATE);
        String json = mPrefs.getString(metaDataUser, "");
        Gson gson = new Gson();
        user = gson.fromJson(json, User.class);
        adminDashboardViewModal = new ViewModelProvider(this).get(AdminDashboardViewModal.class);
        Call<List<User>> userCall = adminDashboardViewModal.collegeCounsellingApi.getUser(user.getRollNo());
        userCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!(response.isSuccessful())) {
                    Toast.makeText(DashboardAdminActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(DashboardAdminActivity.this, "user Fetched", Toast.LENGTH_SHORT).show();
                user = response.body().get(0);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(user);
                prefsEditor.putString(metaDataUser, json);
                prefsEditor.commit();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
            }
        });

//        logout = findViewById(R.id.log_out);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        publish_rank = findViewById(R.id.publish_rank);
        publish_result = findViewById(R.id.publish_result);
        view_result = findViewById(R.id.view_result);
        recyclerView = findViewById(R.id.recycler_view);
        name = findViewById(R.id.name);
        name.setText(user.getName());
        search = findViewById(R.id.et_search);
    }


    private void iniComponents() {
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
//                Intent intent = new Intent(DashboardAdminActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        publish_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getIsRankPublished() == 1) {
                    showSuccessWarning("Rank Already Published!");
                    return;
                } else {
                    //make server request and update user
                    Call<List<User>> call = adminDashboardViewModal.collegeCounsellingApi.publishRank(user.getRollNo());
                    call.enqueue(new Callback<List<User>>() {
                        @Override
                        public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                            if (!(response.isSuccessful())) {
                                Toast.makeText(DashboardAdminActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            user = response.body().get(0);
                            user.setIsRankPublished(1);
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            prefsEditor.putString(metaDataUser, json);
                            prefsEditor.commit();
                            showDialogSuccess("Rank Published!");
                            AdapterCollegeListAdminFinalized adapter = new AdapterCollegeListAdminFinalized(DashboardAdminActivity.this);
                            adapter.setItems(adminDashboardViewModal.getAllData());
                            recyclerView.setAdapter(adapter);
                            findViewById(R.id.create).setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<List<User>> call, Throwable t) {
                            Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        publish_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getIsResultPublished() == 1) {
                    showSuccessWarning("Results Already Published!");
                } else {
                    if (user.getIsRankPublished() == 1) {
                        publishResult();
                        showDialogSuccess("Results Published!");
                    } else {
                        showDialogWarning("Publish Rank to Publish Results!");
                    }
                }
            }
        });

        view_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getIsResultPublished() == 1) {
                    Intent intent = new Intent(DashboardAdminActivity.this, ShowResultsActivity.class);
                    startActivity(intent);
                } else {
                    showDialogWarning("Results not yet published!");
                }
            }
        });


        //request server for data


        Call<List<College>> call = adminDashboardViewModal.collegeCounsellingApi.getColleges();
        call.enqueue(new Callback<List<College>>() {
            @Override
            public void onResponse(Call<List<College>> call, Response<List<College>> response) {
                if (!(response.isSuccessful())) {
                    Toast.makeText(DashboardAdminActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                items = response.body();
                adminDashboardViewModal.deleteAllData();
                adminDashboardViewModal.insertAllData(items);
                Toast.makeText(DashboardAdminActivity.this, "Data fetched", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<College>> call, Throwable t) {
                Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new AdapterCollegeListAdmin(this);


        mAdapter.setOnItemClickListener(new AdapterCollegeListAdmin.OnItemClickListener() {
            @Override
            public void onItemClick(View view, College obj, int position) {
                Intent intent = new Intent(DashboardAdminActivity.this, EditCollegeActivity.class);
                Gson gson = new Gson();
                String json = gson.toJson(obj);
                intent.putExtra(editIntentExtras, json);
                startActivity(intent);
            }
        });
        mAdapter.setOnItemDeleteListener(new AdapterCollegeListAdmin.OnItemDeleteListener() {
            @Override
            public void onItemClick(View view, College obj, int position) {
                Call<Void> call = adminDashboardViewModal.collegeCounsellingApi.deleteCollege(obj.getCode());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!(response.isSuccessful())) {
                            Toast.makeText(DashboardAdminActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        adminDashboardViewModal.delete(obj);
                        Toast.makeText(DashboardAdminActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        recyclerView.setAdapter(mAdapter);


        adminDashboardViewModal.getLiveData().observe(this, new Observer<List<College>>() {
            @Override
            public void onChanged(List<College> colleges) {

                if (user.getIsRankPublished() == 1) {
                    AdapterCollegeListAdminFinalized adapter = new AdapterCollegeListAdminFinalized(DashboardAdminActivity.this);
                    adapter.setItems(items);
                    recyclerView.setAdapter(adapter);
                } else {
                    mAdapter.setItems(colleges);
                    items = colleges;
                    search.setText("");
                }
            }
        });


        if (user.getIsRankPublished() == 1) {
            findViewById(R.id.create).setVisibility(View.GONE);
        }
        findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardAdminActivity.this, CreateCollegeActivity.class);
                startActivity(intent);
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
                for (College college : items) {
                    if (college.getName().toLowerCase().contains(filterPattern) || college.getCode().startsWith(filterPattern)) {
                        tempColleges.add(college);
                    }
                }

                if (user.getIsRankPublished() == 1) {
                    AdapterCollegeListAdminFinalized adapter = new AdapterCollegeListAdminFinalized(DashboardAdminActivity.this);
                    adapter.setItems(tempColleges);
                    recyclerView.setAdapter(adapter);
                } else {
                    mAdapter = new AdapterCollegeListAdmin(DashboardAdminActivity.this);

                    mAdapter.setOnItemClickListener(new AdapterCollegeListAdmin.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, College obj, int position) {
                            Intent intent = new Intent(DashboardAdminActivity.this, EditCollegeActivity.class);
                            Gson gson = new Gson();
                            String json = gson.toJson(obj);
                            intent.putExtra(editIntentExtras, json);
                            startActivity(intent);
                        }
                    });
                    mAdapter.setOnItemDeleteListener(new AdapterCollegeListAdmin.OnItemDeleteListener() {
                        @Override
                        public void onItemClick(View view, College obj, int position) {
                            Call<Void> call = adminDashboardViewModal.collegeCounsellingApi.deleteCollege(obj.getCode());
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (!(response.isSuccessful())) {
                                        Toast.makeText(DashboardAdminActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    adminDashboardViewModal.delete(obj);
                                    Toast.makeText(DashboardAdminActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    mAdapter.setItems(tempColleges);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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


    private void publishResult() {
        Call<Void> getStudentsInBackendCall = adminDashboardViewModal.collegeCounsellingApi.getStudentsInBackend();
        getStudentsInBackendCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!(response.isSuccessful())) {
                    Toast.makeText(DashboardAdminActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                Call<Void> publishResultCall = adminDashboardViewModal.collegeCounsellingApi.publishResult();
                publishResultCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!(response.isSuccessful())) {
                            Toast.makeText(DashboardAdminActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Call<Void> updateResultPublishedCall = adminDashboardViewModal.collegeCounsellingApi.updateResultPublished();
                        updateResultPublishedCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (!(response.isSuccessful())) {
                                    Toast.makeText(DashboardAdminActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                user.setIsResultPublished(1);
                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(user);
                                Log.i("info2", "onResponse: json" + json);
                                prefsEditor.putString(metaDataUser, json);
                                prefsEditor.commit();

                                Toast.makeText(DashboardAdminActivity.this, "Results Published", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialogSuccess(String text) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_success);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((TextView)dialog.findViewById(R.id.text)).setText(text);
        dialog.show();
    }

    private void showSuccessWarning(String text) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_success_warning);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((TextView)dialog.findViewById(R.id.text)).setText(text);
        dialog.show();
    }

    private void showDialogWarning(String text) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_warning);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((TextView)dialog.findViewById(R.id.text)).setText(text);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.about_developer){
            showDialogDeveloper();
        }

        if(item.getItemId() == R.id.log_out){
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.putString(metaDataLoggedIn, "0");
            prefsEditor.putString(metaData, "");
            prefsEditor.putString(metaDataUser, "");
            prefsEditor.commit();
            Intent intent = new Intent(DashboardAdminActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return true;
    }


}

//    public void allocateCollege() {
//        Log.i("info2", "onResponse: inside function size " + j);
//        if (studentCollegeList.get(j).getCategory() == 1) {
//            if (college.getBc() >= 1) {
//                college.setBc(college.getBc() - 1);
//                int k = j;
//                Toast.makeText(DashboardAdminActivity.this, "allotted", Toast.LENGTH_SHORT).show();
//                Call<Void> updateCollegeCall = adminDashboardViewModal.collegeCounsellingApi.updateCollege(college);
//                updateCollegeCall.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        if (!(response.isSuccessful())) {
//                            Toast.makeText(DashboardAdminActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        result = new Result(studentCollegeList.get(k).getName(), studentCollegeList.get(j).getRollNo(), studentCollegeList.get(j).getCollegeName(), studentCollegeList.get(j).getCode(), studentCollegeList.get(j).getDepartment(), studentCollegeList.get(j).getPriority());
//                        Call<Void> insertResultCall = adminDashboardViewModal.collegeCounsellingApi.postResult(result);
//                        insertResultCall.enqueue(new Callback<Void>() {
//                            @Override
//                            public void onResponse(Call<Void> call, Response<Void> response) {
//                                if (!(response.isSuccessful())) {
//                                    Toast.makeText(DashboardAdminActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                Toast.makeText(DashboardAdminActivity.this, "College allotted", Toast.LENGTH_SHORT).show();
//                                studentCollegeList.clear();
//                            }
//
//                            @Override
//                            public void onFailure(Call<Void> call, Throwable t) {
//                                Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        } else if (studentCollegeList.get(j).getCategory() == 2) {
//            if (college.getMbc() >= 1) {
//                college.setMbc(college.getMbc() - 1);
//                int k = j;
//                Toast.makeText(DashboardAdminActivity.this, "allotted", Toast.LENGTH_SHORT).show();
//                Call<Void> updateCollegeCall = adminDashboardViewModal.collegeCounsellingApi.updateCollege(college);
//                updateCollegeCall.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        if (!(response.isSuccessful())) {
//                            Toast.makeText(DashboardAdminActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        result = new Result(studentCollegeList.get(k).getName(), studentCollegeList.get(j).getRollNo(), studentCollegeList.get(j).getCollegeName(), studentCollegeList.get(j).getCode(), studentCollegeList.get(j).getDepartment(), studentCollegeList.get(j).getPriority());
//                        Call<Void> insertResultCall = adminDashboardViewModal.collegeCounsellingApi.postResult(result);
//                        insertResultCall.enqueue(new Callback<Void>() {
//                            @Override
//                            public void onResponse(Call<Void> call, Response<Void> response) {
//                                if (!(response.isSuccessful())) {
//                                    Toast.makeText(DashboardAdminActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                Toast.makeText(DashboardAdminActivity.this, "College allotted", Toast.LENGTH_SHORT).show();
//                                studentCollegeList.clear();
//                            }
//
//                            @Override
//                            public void onFailure(Call<Void> call, Throwable t) {
//                                Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//        } else if (studentCollegeList.get(j).getCategory() == 3) {
//
//            if (college.getOc() >= 1) {
//                college.setOc(college.getOc() - 1);
//                int k = j;
//                Toast.makeText(DashboardAdminActivity.this, "allotted", Toast.LENGTH_SHORT).show();
//                Call<Void> updateCollegeCall = adminDashboardViewModal.collegeCounsellingApi.updateCollege(college);
//                updateCollegeCall.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        if (!(response.isSuccessful())) {
//                            Toast.makeText(DashboardAdminActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        result = new Result(studentCollegeList.get(k).getName(), studentCollegeList.get(j).getRollNo(), studentCollegeList.get(j).getCollegeName(), studentCollegeList.get(j).getCode(), studentCollegeList.get(j).getDepartment(), studentCollegeList.get(j).getPriority());
//                        Call<Void> insertResultCall = adminDashboardViewModal.collegeCounsellingApi.postResult(result);
//                        insertResultCall.enqueue(new Callback<Void>() {
//                            @Override
//                            public void onResponse(Call<Void> call, Response<Void> response) {
//                                if (!(response.isSuccessful())) {
//                                    Toast.makeText(DashboardAdminActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                Toast.makeText(DashboardAdminActivity.this, "College allotted", Toast.LENGTH_SHORT).show();
//                                studentCollegeList.clear();
//                            }
//
//                            @Override
//                            public void onFailure(Call<Void> call, Throwable t) {
//                                Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//        } else if (studentCollegeList.get(j).getCategory() == 4) {
//
//            if (college.getSt() >= 1) {
//                college.setSt(college.getSt() - 1);
//                int k = j;
//                Toast.makeText(DashboardAdminActivity.this, "allotted", Toast.LENGTH_SHORT).show();
//                Call<Void> updateCollegeCall = adminDashboardViewModal.collegeCounsellingApi.updateCollege(college);
//                updateCollegeCall.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        if (!(response.isSuccessful())) {
//                            Toast.makeText(DashboardAdminActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        result = new Result(studentCollegeList.get(k).getName(), studentCollegeList.get(j).getRollNo(), studentCollegeList.get(j).getCollegeName(), studentCollegeList.get(j).getCode(), studentCollegeList.get(j).getDepartment(), studentCollegeList.get(j).getPriority());
//                        Call<Void> insertResultCall = adminDashboardViewModal.collegeCounsellingApi.postResult(result);
//                        insertResultCall.enqueue(new Callback<Void>() {
//                            @Override
//                            public void onResponse(Call<Void> call, Response<Void> response) {
//                                if (!(response.isSuccessful())) {
//                                    Toast.makeText(DashboardAdminActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                Toast.makeText(DashboardAdminActivity.this, "College allotted", Toast.LENGTH_SHORT).show();
//                                studentCollegeList.clear();
//                            }
//
//                            @Override
//                            public void onFailure(Call<Void> call, Throwable t) {
//                                Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//        } else if (studentCollegeList.get(j).getCategory() == 5) {
//            if (college.getOthers() >= 1) {
//                college.setOthers(college.getOthers() - 1);
//                int k = j;
//                Log.i("info2", "allocateCollege: k" + k);
//                Toast.makeText(DashboardAdminActivity.this, "allotted", Toast.LENGTH_SHORT).show();
//                Call<Void> updateCollegeCall = adminDashboardViewModal.collegeCounsellingApi.updateCollege(college);
//                updateCollegeCall.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        if (!(response.isSuccessful())) {
//                            Toast.makeText(DashboardAdminActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        result = new Result(studentCollegeList.get(k).getName(), studentCollegeList.get(j).getRollNo(), studentCollegeList.get(j).getCollegeName(), studentCollegeList.get(j).getCode(), studentCollegeList.get(j).getDepartment(), studentCollegeList.get(j).getPriority());
//                        Call<Void> insertResultCall = adminDashboardViewModal.collegeCounsellingApi.postResult(result);
//                        insertResultCall.enqueue(new Callback<Void>() {
//                            @Override
//                            public void onResponse(Call<Void> call, Response<Void> response) {
//                                if (!(response.isSuccessful())) {
//                                    Toast.makeText(DashboardAdminActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                Toast.makeText(DashboardAdminActivity.this, "College allotted", Toast.LENGTH_SHORT).show();
//                                studentCollegeList.clear();
//                            }
//
//                            @Override
//                            public void onFailure(Call<Void> call, Throwable t) {
//                                Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Toast.makeText(DashboardAdminActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//        }
//    }
