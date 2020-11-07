package com.example.collegecounsellingapplication.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.collegecounsellingapplication.R;
import com.example.collegecounsellingapplication.modals.CollegeListFinalized;
import com.example.collegecounsellingapplication.modals.CollegeListSaved;


import java.util.ArrayList;

import java.util.List;

public class AdapterConfirmCollegeList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CollegeListFinalized> items = new ArrayList<>();

    private Context ctx;

    public AdapterConfirmCollegeList(Context context) {
        ctx = context;
    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name, code, department, priority;

        public OriginalViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.collage_name);
            code = v.findViewById(R.id.code);
            department = v.findViewById(R.id.department);
            priority = v.findViewById(R.id.priority);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_college_list_card, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final CollegeListFinalized data = items.get(position);
            view.name.setText(data.getCollegeName());
            view.code.setText("#" + data.getCode());
            view.priority.setText(String.valueOf(data.getPriority()));
            view.department.setText(getDepartment(data.getDepartment()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
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

    public void setItems(List<CollegeListFinalized> items){
        this.items = items;
        notifyDataSetChanged();
    }
}