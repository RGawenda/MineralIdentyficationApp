package com.gymapps.mineralidentyficationapp.rest;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.gymapps.mineralidentyficationapp.rest.messages.ClassificationMessage;
import com.gymapps.mineralidentyficationapp.rest.messages.ClassificationResultMessageClass;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MineralAppApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/classification-rest/";

    private Retrofit retrofit;
    private MineralAppApi myApi;

    public MineralAppApiClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myApi = retrofit.create(MineralAppApi.class);
    }

    public void classification(Bitmap iamge) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        iamge.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        ClassificationMessage classificationMessage = new ClassificationMessage();
        classificationMessage.setAuthToken("f");
        classificationMessage.setStringImage(encoded);

        Gson gson = new Gson();
        String json = gson.toJson(classificationMessage);

        Call<ClassificationResultMessageClass> call = myApi.getClassification(json);
        call.enqueue(new Callback<ClassificationResultMessageClass>() {
            @Override
            public void onResponse(Call<ClassificationResultMessageClass> call, Response<ClassificationResultMessageClass> response) {
                Log.i("resp", "resp");
                if (response.isSuccessful()) {
                    ClassificationResultMessageClass data = response.body();
                    // Zrób coś z danymi
                } else {
                    // Obsłuż błędy
                }
            }

            @Override
            public void onFailure(Call<ClassificationResultMessageClass> call, Throwable t) {
                // Obsłuż błędy komunikacji
                Log.i("fail", "fail");
            }
        });
    }
}