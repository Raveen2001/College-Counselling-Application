package com.example.collegecounsellingapplication.retrofit;

import com.example.collegecounsellingapplication.modals.College;
import com.example.collegecounsellingapplication.modals.CollegeListFinalized;
import com.example.collegecounsellingapplication.modals.CollegeListSaved;
import com.example.collegecounsellingapplication.modals.Mark;
import com.example.collegecounsellingapplication.modals.Result;
import com.example.collegecounsellingapplication.modals.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CollegeCounsellingApi {

    @GET("api/user/{rollNo}")
    Call<List<User>> getUser(@Path("rollNo") String rollNo);

    @POST("api/user/update/details/{rollNo}/{cutoff}/{category}")
    Call<List<User>> updateDetails(@Path("rollNo") String rollNo, @Path("cutoff") float cutoff, @Path("category") int category);

    @POST("api/users")
    Call<List<User>> createUser(@Body User user);

    @POST("api/user/{rollNo}/confirmCollegeList")
    Call<List<User>> submitChoiceList(@Body List<CollegeListFinalized> data, @Path("rollNo") String rollNo);

    @GET("api/user/{rollNo}/confirmedCollegeList")
    Call<List<CollegeListFinalized>> getConfirmedCollegeList(@Path("rollNo") String rollNo);

    @POST("api/user/{rollNo}/saveCollegeList")
    Call<Void> saveChoiceList(@Body List<CollegeListSaved> data, @Path("rollNo") String rollNo);

    @GET("api/user/{rollNo}/savedCollegeList")
    Call<List<CollegeListSaved>> getSavedCollegeList(@Path("rollNo") String rollNo);

    @POST("api/user/marks/{rollNo}")
    Call<List<Mark>> submitMark(@Body Mark mark, @Path("rollNo") String rollNo);

    @GET("api/user/marks/{rollNo}")
    Call<List<Mark>> getMark(@Path("rollNo") String rollNo);

    @GET("api/admin/colleges")
    Call<List<College>> getColleges();

    @POST("api/admin/colleges")
    Call<Void> insertCollege(@Body College college);

    @GET("api/admin/colleges/delete/{code}")
    Call<Void> deleteCollege(@Path("code") String code);

    @POST("api/admin/colleges/update")
    Call<Void> updateCollege(@Body College college);

    @GET("api/admin/publishRank/{id}")
    Call<List<User>> publishRank(@Path("id") String id);

    @GET("/api/admin/getStudents")
    Call<Void> getStudentsInBackend();

    @GET("/api/admin/publishResult")
    Call<Void> publishResult();

    @GET("/api/admin/updateResultPublished")
    Call<Void> updateResultPublished();

    @GET("/api/admin/results")
    Call<List<Result>> getResults();

    @GET("/api/admin/results/{rollNo}")
    Call<List<Result>> getResult(@Path("rollNo") String rollNo);
}
