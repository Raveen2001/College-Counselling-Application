package com.example.collegecounsellingapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.collegecounsellingapplication.R;
import com.example.collegecounsellingapplication.adapters.AdapterResults;
import com.example.collegecounsellingapplication.modals.Result;
import com.example.collegecounsellingapplication.retrofit.CollegeCounsellingApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.collegecounsellingapplication.activities.LoginActivity.BASE_URL;

public class ShowResultsActivity extends AppCompatActivity {
    private ImageButton btn_close;
    private RecyclerView recyclerView;
    private AdapterResults adapter;
    private CollegeCounsellingApi collegeCounsellingApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);
        iniVariables();
        iniComponents();
    }

    private void iniVariables() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        collegeCounsellingApi = retrofit.create(CollegeCounsellingApi.class);

        btn_close = findViewById(R.id.btn_close);
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void iniComponents() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        Call<List<Result>> call = collegeCounsellingApi.getResults();
        call.enqueue(new Callback<List<Result>>() {
            @Override
            public void onResponse(Call<List<Result>> call, Response<List<Result>> response) {
                if(!(response.isSuccessful())){
                    Toast.makeText(ShowResultsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                adapter = new AdapterResults(ShowResultsActivity.this, response.body());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Result>> call, Throwable t) {
                Toast.makeText(ShowResultsActivity.this, "Can't make request to the server", Toast.LENGTH_SHORT).show();
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}