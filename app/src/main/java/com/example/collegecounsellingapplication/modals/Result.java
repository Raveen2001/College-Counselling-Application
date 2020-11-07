package com.example.collegecounsellingapplication.modals;

import com.google.gson.annotations.SerializedName;

public class Result {
    private String name;

    @SerializedName("roll_no")
    private String rollNo;

    @SerializedName("college_name")
    private String collegeName;

    private String code;

    private int department;

    private int choice_no;

    public Result(String name, String rollNo, String collegeName, String code, int department, int choice_no) {
        this.name = name;
        this.rollNo = rollNo;
        this.collegeName = collegeName;
        this.code = code;
        this.department = department;
        this.choice_no = choice_no;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
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

    public int getChoice_no() {
        return choice_no;
    }

    public void setChoice_no(int choice_no) {
        this.choice_no = choice_no;
    }
}
