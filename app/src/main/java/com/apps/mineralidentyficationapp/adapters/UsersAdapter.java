package com.apps.mineralidentyficationapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.mineralidentyficationapp.R;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private List<String> records;


    public interface OnRecordDeleteClickListener {
        void onRecordDeleteClick(int position);
    }
    private OnRecordDeleteClickListener onRecordDeleteClickListener;

    public UsersAdapter(List<String> records) {
        this.records = records;
    }

    public void setOnRecordDeleteClickListener(OnRecordDeleteClickListener listener) {
        this.onRecordDeleteClickListener = listener;
        Log.d("UsersAdapter", "onRecordDeleteClickListener set: " + listener);

    }

    public void updateList(List<String> newRecords) {
        this.records = newRecords;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.itemTagsTextView);
            deleteButton = itemView.findViewById(R.id.itemTagsDeleteButton);

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onRecordDeleteClickListener != null) {
                    onRecordDeleteClickListener.onRecordDeleteClick(position);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tags, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String record = records.get(position);
        holder.textView.setText(record);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public String getItem(int pos) {
        return records.get(pos);
    }

    public void removeRecord(int position) {
        Log.i("deleteTag", "delete:" + position);
        if (position >= 0 && position < records.size()) {
            records.remove(position);
            notifyDataSetChanged();
        }
    }


}

