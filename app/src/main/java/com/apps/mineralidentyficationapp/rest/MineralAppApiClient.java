package com.apps.mineralidentyficationapp.rest;

import static com.apps.mineralidentyficationapp.utils.FileUtils.convertBitmapToBase64;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.apps.mineralidentyficationapp.collection.Mineral;
import com.apps.mineralidentyficationapp.config.MineralsIdentificationConfig;
import com.apps.mineralidentyficationapp.rest.messages.ClassificationMessage;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MineralAppApiClient {
    private Retrofit retrofit;
    private MineralAppApi myApi;

    private Disposable disposable;


    public MineralAppApiClient(Context context)
    {
        String url = MineralsIdentificationConfig.getConfigProperties(context, "BASE_URL");
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        myApi = retrofit.create(MineralAppApi.class);
    }

    public void classification(final RxCallback<Map<String, Double>> callback, Bitmap image) {
        Log.i("start class", "start class");

        disposable = myApi.getClassification(convertBitmapToBase64(image))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );

        Log.i("end class", "end class");
    }

    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void addNewMineral(Bitmap image, Mineral mineral) {

        ClassificationMessage classificationMessage = new ClassificationMessage();
        classificationMessage.setAuthToken("f");
        classificationMessage.setStringImage(convertBitmapToBase64(image));

        Gson gson = new Gson();
        String json = gson.toJson(classificationMessage);

    }
}