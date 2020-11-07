package com.example.collegecounsellingapplication.modals;

import com.google.gson.annotations.SerializedName;

public class Mark {
    @SerializedName("roll_no")
    private String rollNo;

    private float english, tamil, maths, physics, chemistry, bio, cs, total;

    private float cutoff;

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public Mark(String rollNo, float english, float tamil, float maths, float physics, float chemistry, float cutoff) {
        this.rollNo = rollNo;
        this.english = english;
        this.tamil = tamil;
        this.maths = maths;
        this.physics = physics;
        this.chemistry = chemistry;
        this.cutoff = cutoff;
    }

    public float getEnglish() {
        return english;
    }

    public void setEnglish(float english) {
        this.english = english;
    }

    public float getTamil() {
        return tamil;
    }

    public void setTamil(float tamil) {
        this.tamil = tamil;
    }

    public float getMaths() {
        return maths;
    }

    public void setMaths(float maths) {
        this.maths = maths;
    }

    public float getPhysics() {
        return physics;
    }

    public void setPhysics(float physics) {
        this.physics = physics;
    }

    public float getChemistry() {
        return chemistry;
    }

    public void setChemistry(float chemistry) {
        this.chemistry = chemistry;
    }

    public float getBio() {
        return bio;
    }

    public void setBio(float bio) {
        this.bio = bio;
    }

    public float getCs() {
        return cs;
    }

    public void setCs(float cs) {
        this.cs = cs;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getCutoff() {
        return cutoff;
    }

    public void setCutoff(float cutoff) {
        this.cutoff = cutoff;
    }
}
