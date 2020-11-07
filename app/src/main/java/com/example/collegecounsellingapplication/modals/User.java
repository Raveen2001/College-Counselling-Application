package com.example.collegecounsellingapplication.modals;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("roll_no")
    private String rollNo;

    @SerializedName("name")
    private String name;

    @SerializedName("dob")
    private String dob;

    @SerializedName("password")
    private String password;

    @SerializedName("isAdmin")
    private int isAdmin;

    @SerializedName("cutoff")
    private float cutoff;

    @SerializedName("student_rank")
    private int rank;

    @SerializedName("is_rank_published")
    private int isRankPublished;



    @SerializedName("category")
    private int categoryId;


    @SerializedName("is_list_submitted")
    private int isListSubmitted;

    @SerializedName("is_result_published")
    private int isResultPublished;





    public User(String rollNo, String name, String dob, String password) {
        this.rollNo = rollNo;
        this.name = name;
        this.dob = dob;
        this.password = password;
    }

    public float getCutoff() {
        return cutoff;
    }

    public void setCutoff(float cutoff) {
        this.cutoff = cutoff;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }


    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int isAdmin() {
        return isAdmin;
    }

    public void setAdmin(int admin) {
        isAdmin = admin;
    }


    public int getIsRankPublished() {
        return isRankPublished;
    }

    public void setIsRankPublished(int isRankPublished) {
        this.isRankPublished = isRankPublished;
    }


    public int getIsListSubmitted() {
        return isListSubmitted;
    }

    public void setIsListSubmitted(int isListSubmitted) {
        this.isListSubmitted = isListSubmitted;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getIsResultPublished() {
        return isResultPublished;
    }

    public void setIsResultPublished(int isResultPublished) {
        this.isResultPublished = isResultPublished;
    }

}
