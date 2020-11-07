package com.example.collegecounsellingapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegecounsellingapplication.R;
import com.example.collegecounsellingapplication.helper.DragItemTouchHelper;
import com.example.collegecounsellingapplication.modals.CollegeListSaved;

import java.util.ArrayList;
import java.util.List;

public class AdapterCollegeListDrag extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DragItemTouchHelper.MoveHelperAdapter {

    private List<CollegeListSaved> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnStartDragListener mDragStartListener = null;

    public interface OnItemClickListener {
        void onItemClick(View view, CollegeListSaved obj, int position);
    }

    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterCollegeListDrag(Context context) {
        ctx = context;
    }

    public void setDragListener(OnStartDragListener dragStartListener) {
        this.mDragStartListener = dragStartListener;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder implements DragItemTouchHelper.TouchViewHolder {
        public TextView priority;
        public TextView collageName, code, department;
        public ImageButton bt_move;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            priority = v.findViewById(R.id.priority);
            collageName = (TextView) v.findViewById(R.id.collage_name);
            department = v.findViewById(R.id.department);
            code = v.findViewById(R.id.code);
            bt_move = (ImageButton) v.findViewById(R.id.bt_move);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(ctx.getResources().getColor(R.color.grey_5));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(ctx.getResources().getColor(R.color.white));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.college_list_card_swapable, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final CollegeListSaved data = items.get(position);
            view.priority.setText(String.valueOf(position + 1));
            view.collageName.setText(data.getName());
            view.code.setText("#" + data.getCode());
            view.department.setText(getDepartment(data.getDepartment()));

            // Start a drag whenever the handle view it touched
            view.bt_move.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN && mDragStartListener != null) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        CollegeListSaved item = items.remove(fromPosition);
        items.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public List<CollegeListSaved> getItems() {
        return items;
    }

    public void setItems(List<CollegeListSaved> items) {
        this.items = items;
        notifyDataSetChanged();
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