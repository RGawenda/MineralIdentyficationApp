package com.apps.mineralidentyficationapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.mineralidentyficationapp.rest.MineralAppApiClient;
import com.apps.mineralidentyficationapp.rest.RxCallback;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button camera, gallery, collection;
    private ImageView imageView;
    private TextView result;
    private Context context;
    private ProgressDialog progressDialog;
    private int imageSize = 32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        camera = findViewById(R.id.button);
        gallery = findViewById(R.id.button2);
        collection = findViewById(R.id.button3);

        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context = view.getContext();
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context = view.getContext();
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });

        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), CollectionActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Landing...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showClassificationResult(Map<String, Double> resultMap, List<String> names) {
        Intent intent = new Intent(context, ClassificationResultActivity.class);
        intent.putExtra("mineralList", (Serializable) resultMap);
        intent.putExtra("names", (Serializable) names);

        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);

                sendToClassification(image);

            }else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);

                sendToClassification(image);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendToClassification(Bitmap image){
        MineralAppApiClient myApiClient = new MineralAppApiClient(context);

        showLoading();
        myApiClient.classification(new RxCallback<>() {
            @Override
            public void onSuccess(Map<String, Double> result) {
                hideLoading();
                List<String> names2 = Arrays.asList(new String[]{"fff", "lll", "agate", "amethyst", "azurite", "calcite", "chrysocolla", "citrine", "emerald", "gypsum",
                        "labradorite", "malachite", "opal", "pyrite", "quartz", "ruby", "smoky quartz", "topaz", "tourmaline",
                        "turquoise", "wulfenite", "ooo", "qqq"});
                Log.i("success", "navigate");
                showClassificationResult(result, names2);
            }

            @Override
            public void onError(String errorMessage) {
                Log.i("error rx", "error rx");
                hideLoading();
            }
        }, image);
    }


}