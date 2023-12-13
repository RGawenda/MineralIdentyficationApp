package com.apps.mineralidentyficationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.apps.mineralidentyficationapp.databinding.ActivityCollectionBinding;

import java.sql.Array;

public class CollectionActivity extends AppCompatActivity {

    ActivityCollectionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("test","create collection activity");
        binding = ActivityCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setContentView(R.layout.activity_collection);
        String[] mineralName = {"","amentyst", "kwarc"};

        int[] images = {R.drawable.image_0058362, R.drawable.image_0059505, R.drawable.kwarc7};

        MineralInCollectionAdapter mineralInCollectionAdapter = new MineralInCollectionAdapter(CollectionActivity.this, mineralName, images);
        binding.gridView.setAdapter(mineralInCollectionAdapter);
        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("test click","click on item");
                Toast.makeText(CollectionActivity.this, "click on"+mineralName[i], Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(view.getContext(), MineralActivity.class);
                Bundle b = new Bundle();
                b.putInt("key", i);
                myIntent.putExtras(b);
                startActivity(myIntent);
                finish();
            }
        });
    }
}