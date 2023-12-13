package com.apps.mineralidentyficationapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MineralAdapter extends RecyclerView.Adapter<MineralAdapter.ViewHolder> implements Filterable {

    private final Map<String, Double> classificationResults;
    private List<String> mineralsToShow;
    private List<String> mineralsToShowCopy;
    private OnItemClickListener onItemClickListener;

    public MineralAdapter(List<String> names, Map<String, Double> classificationResults) {
        this.classificationResults = classificationResults;
        this.mineralsToShow = processMineralList(names, classificationResults);
        this.mineralsToShowCopy = mineralsToShow;
    }

    public interface OnItemClickListener {
        void onItemClick(String mineralName);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mineral, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(mineralsToShow.get(position));
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String mineralName = mineralsToShow.get(position);
        Double mineralPercentage = classificationResults.getOrDefault(mineralName, 0.0);
        holder.bind(mineralName, mineralPercentage, position);
    }

    @Override
    public int getItemCount() {
        return mineralsToShow.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                String query = charSequence.toString().toLowerCase();
                if (query.isEmpty()) {
                    filterResults.values = mineralsToShowCopy;
                } else {
                    List<String> filtered = mineralsToShowCopy.stream()
                            .filter(name -> name.toLowerCase().contains(query))
                            .collect(Collectors.toList());
                    filterResults.values = filtered;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mineralsToShow = (List<String>) filterResults.values;
                    notifyDataSetChanged();
            }
        };
    }

    private static List<String> processMineralList(List<String> minerals, Map<String, Double> classificationMinerals){
        List<String> highestValues = classificationMinerals.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<String> remainingItems = new ArrayList<>(minerals);
        remainingItems.removeAll(highestValues);

        Collections.shuffle(remainingItems);

        List<String> result = new ArrayList<>();
        result.addAll(highestValues);
        result.addAll(remainingItems);

        return result;
    }

    public class ViewHolder extends RecyclerView.ViewHolder //implements View.OnClickListener
     {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(String mineralName, Double mineralPercentage, int position) {
            TextView nameTextView = itemView.findViewById(R.id.mineralNameTextView);
            TextView percentageTextView = itemView.findViewById(R.id.mineralPercentageTextView);

            nameTextView.setText(mineralName);
            if(mineralPercentage > 0.00){
                percentageTextView.setText(String.format(" %.2f%%", mineralPercentage));

            }else{
                percentageTextView.setText("");
            }
            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(mineralName);
                }
            });
        }
    }
}
