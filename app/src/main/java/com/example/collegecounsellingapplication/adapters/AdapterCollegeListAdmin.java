package com.example.collegecounsellingapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.collegecounsellingapplication.R;
import com.example.collegecounsellingapplication.Repository.CollegeListRepository;
import com.example.collegecounsellingapplication.helper.SwipeItemTouchHelper;
import com.example.collegecounsellingapplication.modals.College;


import java.util.ArrayList;
import java.util.List;

public class AdapterCollegeListAdmin extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<College> items = new ArrayList<>();
    private List<College> items_swiped = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnItemDeleteListener mOnItemDeleteListener;
    private CollegeListRepository collegeListRepository;

    public interface OnItemClickListener {
        void onItemClick(View view, College obj, int position);
    }
     public interface OnItemDeleteListener {
            void onItemClick(View view, College obj, int position);
        }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }
    public void setOnItemDeleteListener(final OnItemDeleteListener mItemDeleteListener) {
        this.mOnItemDeleteListener = mItemDeleteListener;
    }

    public AdapterCollegeListAdmin(Context context) {
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder{
        public TextView name, code, bc, mbc, oc, st, others;
        public ImageButton btn_delete;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            code = v.findViewById(R.id.code);
            bc = v.findViewById(R.id.bc);
            mbc = v.findViewById(R.id.mbc);
            oc = v.findViewById(R.id.oc);
            st = v.findViewById(R.id.st);
            others = v.findViewById(R.id.others);
            btn_delete = (ImageButton) v.findViewById(R.id.btn_delete);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.college_card, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;
            final College data = items.get(position);
            view.name.setText(data.getName());
            view.bc.setText(String.valueOf(data.getBc()));
            view.mbc.setText(String.valueOf(data.getMbc()));
            view.oc.setText(String.valueOf(data.getOc()));
            view.st.setText(String.valueOf(data.getSt()));
            view.others.setText(String.valueOf(data.getOthers()));
            view.code.setText("#" + data.getCode());
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            view.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    College deletedCollege = items.get(position);
                    if (mOnItemClickListener != null) {
                        mOnItemDeleteListener.onItemClick(v, deletedCollege, position);
                    }
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setItems(List<College> data){
        this.items = data;
        notifyDataSetChanged();
    }
}