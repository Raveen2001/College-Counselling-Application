package com.example.collegecounsellingapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.collegecounsellingapplication.R;
import com.example.collegecounsellingapplication.modals.College;
import com.example.collegecounsellingapplication.modals.Result;

import java.util.List;

public class AdapterResults extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Result> items;
    private Context ctx;

    public AdapterResults(Context context, List<Result> items) {
        ctx = context;
        this.items = items;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name, rollNo, collegeName, code, department, choiceNo;

        public OriginalViewHolder(View v) {
            super(v);
            name =  v.findViewById(R.id.name);
            code = v.findViewById(R.id.code);
            rollNo = v.findViewById(R.id.roll_no);
            collegeName = v.findViewById(R.id.college_name);
            department = v.findViewById(R.id.department);
            choiceNo = v.findViewById(R.id.choice_no);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_for_results, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;
            final Result data = items.get(position);
            view.name.setText(data.getName());
            view.rollNo.setText("#" + data.getRollNo());
            view.collegeName.setText(data.getCollegeName());
            view.code.setText("#" + data.getCode());
            view.department.setText(getDepartment(data.getDepartment()));
            view.choiceNo.setText(String.valueOf(data.getChoice_no()));
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

}