package com.apps.mineralidentyficationapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class ClassificationResultActivity extends AppCompatActivity {
    Map<String, Double> mineralList;
    List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification_result);

        Intent intent = getIntent();
        if (intent != null) {
            mineralList = (Map<String, Double>) intent.getSerializableExtra("mineralList");
            names = (List<String>) intent.getSerializableExtra("names");
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        EditText searchEditText = findViewById(R.id.searchEditText);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        MineralAdapter adapter = new MineralAdapter(names, mineralList);

        adapter.setOnItemClickListener(mineralName  -> {
            // Handle item click
            Log.i("MainActivity", "Item clicked at position: " + mineralName );
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    searchEditText.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            adapter.getFilter().filter(charSequence);
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    });
    }
}

