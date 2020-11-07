package com.example.collegecounsellingapplication.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.collegecounsellingapplication.R;
import com.example.collegecounsellingapplication.modals.College;


import java.util.List;

public class AdapterCollegeSearch extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<College> items;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, College obj, int position);
    }


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterCollegeSearch(Context context, List<College> items) {
        this.items = items;
        ctx = context;
    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name, code, bc, mbc, oc, st, others;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.collage_name);
            code = v.findViewById(R.id.code);
            bc = v.findViewById(R.id.bc);
            mbc = v.findViewById(R.id.mbc);
            oc = v.findViewById(R.id.oc);
            st = v.findViewById(R.id.st);
            others = v.findViewById(R.id.others);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.college_search_activity_card, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final College data = items.get(position);
            view.name.setText(data.getName());
            view.code.setText("#" + data.getCode());
            view.bc.setText(String.valueOf(data.getBc()));
            view.mbc.setText(String.valueOf(data.getMbc()));
            view.oc.setText(String.valueOf(data.getOc()));
            view.st.setText(String.valueOf(data.getSt()));
            view.others.setText(String.valueOf(data.getOthers()));

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setItems(List<College> items){
        this.items = items;
        notifyDataSetChanged();
    }

}