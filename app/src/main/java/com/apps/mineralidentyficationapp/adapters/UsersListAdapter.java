package com.apps.mineralidentyficationapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.mineralidentyficationapp.R;

import java.util.List;
import java.util.stream.Collectors;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.ViewHolder> implements Filterable {
    private List<String> filteredList;
    private final List<String> orginalList;
    private UsersListAdapter.OnItemClickListener onItemClickListener;

    public UsersListAdapter(List<String> names) {
        this.filteredList = names;
        this.orginalList = names;
    }

    public interface OnItemClickListener {
        void onItemClick(String mineralName);
    }

    public void setOnItemClickListener(UsersListAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public UsersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mineral, parent, false);
        UsersListAdapter.ViewHolder viewHolder = new UsersListAdapter.ViewHolder(view);

        view.setOnClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(filteredList.get(position));
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersListAdapter.ViewHolder holder, int position) {
        String username = filteredList.get(position);
        holder.bind(username, position);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                String query = charSequence.toString().toLowerCase();
                if (query.isEmpty()) {
                    filterResults.values = orginalList;
                } else {
                    List<String> filtered = orginalList.stream()
                            .filter(name -> name.toLowerCase().contains(query))
                            .collect(Collectors.toList());
                    filterResults.values = filtered;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (List<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView = itemView.findViewById(R.id.mineralItemMineralNameTextView);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(String username, int position) {
            nameTextView.setText(username);

            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(username);
                }
            });
        }
    }
}
