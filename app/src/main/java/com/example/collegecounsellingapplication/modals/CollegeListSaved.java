package com.example.collegecounsellingapplication.modals;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.android.material.datepicker.MaterialDatePicker;

@Entity(tableName = "college_list_saved")
public class CollegeListSaved {

    private String name;

    private String code;

    private int department;
    @PrimaryKey(autoGenerate = true)
    private int priority;

    private int category;

    public CollegeListSaved(String name, String code, int department, int category) {
        this.name = name;
        this.code = code;
        this.department = department;
        this.category = category;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
