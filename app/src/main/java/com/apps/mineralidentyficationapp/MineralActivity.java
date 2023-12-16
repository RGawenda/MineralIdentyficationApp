package com.apps.mineralidentyficationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MineralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mineral);
        Bundle b = getIntent().getExtras();
        int value = -1;
        if (b != null)
            value = b.getInt("key");

        ImageView imageView = findViewById(R.id.min_image);
        TextView textView = findViewById(R.id.min_text);
        String[] mineralName = {"kwarc_dymny", "ametyst", "kwarc"};

        int[] images = {R.drawable.image_0058362, R.drawable.image_0059505, R.drawable.kwarc7};

        imageView.setImageResource(images[value]);
        textView.setText(mineralName[value]);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}