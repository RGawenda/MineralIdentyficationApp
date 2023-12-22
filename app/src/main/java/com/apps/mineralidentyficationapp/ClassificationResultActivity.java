package com.apps.mineralidentyficationapp;

import static com.apps.mineralidentyficationapp.utils.FileUtils.convertListBitmapToBase64;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.mineralidentyficationapp.adapters.MineralAdapter;
import com.apps.mineralidentyficationapp.collection.MineralMessage;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ClassificationResultActivity extends AppCompatActivity {
    Map<String, Double> mineralList;
    List<String> names;
    List<Bitmap> mineralBitmapList;
    private Button addMineralButton, addImageButton;
    String selectedMineralName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification_result);

        Intent intent = getIntent();
        if (intent != null) {
            mineralList = (Map<String, Double>) intent.getSerializableExtra("mineralList");
            names = (List<String>) intent.getSerializableExtra("names");
            mineralBitmapList = (List<Bitmap>) intent.getSerializableExtra("images");
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        EditText searchEditText = findViewById(R.id.searchEditText);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        TextView selectedText = findViewById(R.id.selectedMineral);

        addMineralButton = findViewById(R.id.addToCollectionNewMineral);
        addImageButton = findViewById(R.id.addToCollectionAsImage);
        selectedMineralName = names.get(0);
        MineralAdapter adapter = new MineralAdapter(names, mineralList);

        adapter.setOnItemClickListener(mineralName -> {
            selectedText.setText(mineralName);
            selectedMineralName = mineralName;
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        addMineralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MineralMessage mineralMessage = new MineralMessage();
                mineralMessage.setMineralName(selectedMineralName);
                mineralMessage.setImages(convertListBitmapToBase64(mineralBitmapList));
                Context context = view.getContext();
                Intent intent = new Intent(context, MainMineralActivity.class);
                intent.putExtra("mineralMessage", mineralMessage);
                context.startActivity(intent);
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, MineralActivity.class);
                intent.putExtra("selectedMineralName", (Serializable) selectedMineralName);

                context.startActivity(intent);
            }
        });
    }


}

