package com.example.collegecounsellingapplication.modals;

import com.google.gson.annotations.SerializedName;

public class CollegeListFinalized {

    @SerializedName("roll_no")
    private String rollNo;

    private String name;

    private int category;

    @SerializedName("college_name")
    private String collegeName;

    private String code;

    private int department;

    private int priority;

    public CollegeListFinalized(String rollNo, String name, int category, String collegeName, String code, int department, int priority) {
        this.rollNo = rollNo;
        this.name = name;
        this.category = category;
        this.collegeName = collegeName;
        this.code = code;
        this.department = department;
        this.priority = priority;
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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
