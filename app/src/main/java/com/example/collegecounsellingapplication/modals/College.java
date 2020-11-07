package com.example.collegecounsellingapplication.modals;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "collage_list")
public class College {
    private String name;
    @NonNull
    @PrimaryKey
    private String code;

    private int bc;

    private int mbc;

    private int oc;

    private int st;

    private int others;

    @Ignore
    public boolean swiped = false;

    public College(String name, String code, int bc, int mbc, int oc, int st, int others) {
        this.name = name;
        this.code = code;
        this.bc = bc;
        this.mbc = mbc;
        this.st = st;
        this.oc = oc;
        this.others = others;
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

    public int getBc() {
        return bc;
    }

    public void setBc(int bc) {
        this.bc = bc;
    }

    public int getMbc() {
        return mbc;
    }

    public void setMbc(int mbc) {
        this.mbc = mbc;
    }

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }

    public int getOc() {
        return oc;
    }

    public void setOc(int oc) {
        this.oc = oc;
    }

    public int getOthers() {
        return others;
    }

    public void setOthers(int others) {
        this.others = others;
    }
}
