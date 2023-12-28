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

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {
    private final List<String> records;

    public TagsAdapter(List<String> records) {
        this.records = records;
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
        holder.deleteButton.setOnClickListener(v -> removeRecord(position));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.itemTagsTextView);
            deleteButton = itemView.findViewById(R.id.itemTagsDeleteButton);
        }
    }

    private void removeRecord(int position) {
        Log.i("deleteTag", "delete:" + position);
        if (position >= 0 && position < records.size()) {
            records.remove(position);
            notifyDataSetChanged();
        }
    }
}
