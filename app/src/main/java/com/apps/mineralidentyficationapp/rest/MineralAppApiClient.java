package com.apps.mineralidentyficationapp.rest;

import static com.apps.mineralidentyficationapp.utils.FileUtils.convertBitmapToBase64;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.apps.mineralidentyficationapp.SessionManager;
import com.apps.mineralidentyficationapp.collection.FoundMineralFilter;
import com.apps.mineralidentyficationapp.collection.MineralMessage;
import com.apps.mineralidentyficationapp.collection.Minerals;
import com.apps.mineralidentyficationapp.config.MineralsIdentificationConfig;
import com.apps.mineralidentyficationapp.rest.messages.AuthRequest;
import com.apps.mineralidentyficationapp.rest.messages.AuthenticationResponse;
import com.apps.mineralidentyficationapp.rest.messages.ClassificationMessage;
import com.apps.mineralidentyficationapp.rest.messages.RegisterRequest;
import com.apps.mineralidentyficationapp.utils.AccountType;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MineralAppApiClient {
    private final MineralAppApi myApi;
    private Disposable disposable;
    private final String authToken;

    public MineralAppApiClient(Context context) {
        String url = MineralsIdentificationConfig.getConfigProperties(context, "BASE_URL");
        SessionManager sessionManager = new SessionManager(context);
        authToken = "Bearer " + sessionManager.getToken();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        myApi = retrofit.create(MineralAppApi.class);
    }

    public void classification(final RxCallback<Map<String, Double>> callback, Bitmap image) {
        disposable = myApi.getClassification(authToken,convertBitmapToBase64(image))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );
    }

    public void getMineralsNames(final RxCallback<List<String>> callback) {
        disposable = myApi.getMineralsNames(authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );
    }

    public void getTags(final RxCallback<List<String>> callback, String user) {
        disposable = myApi.getTags(authToken,user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );
    }

    public void getCollection(final RxCallback<List<MineralMessage>> callback, int page, int pageSize, String filter) {
        disposable = myApi.getCollections(authToken,page, pageSize, filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );
    }

    public void getMineral(final RxCallback<Minerals> callback, String mineralName) {
        disposable = myApi.getMineral(authToken,mineralName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );
    }

    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void addNewMineral(final RxCallback<MineralMessage> callback, MineralMessage mineralName) {
        disposable = myApi.addMineralToCollection(authToken, mineralName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );
    }

    public void deleteMineral(final RxCallback<Object> callback, Long id) {
        disposable = myApi.deleteMineralFromCollection(authToken,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );
    }

    public void editMineral(final RxCallback<MineralMessage> callback, MineralMessage mineralMessage) {
        disposable = myApi.editMineralInCollection(authToken, mineralMessage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );
    }

    public void login(final RxCallback<AuthenticationResponse> callback, AuthRequest authRequest) {
        disposable = myApi.login(authRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );
    }

    public void register(final RxCallback<AuthenticationResponse> callback, RegisterRequest registerRequest) {
        disposable = myApi.register(registerRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );
    }

    public void unsharedTheCollection(RxCallback<String> callback, String username) {
        disposable = myApi.unsharedTheCollection(authToken, username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );
    }

    public void shareCollection(RxCallback<String> callback, String username) {
        disposable = myApi.shareCollection(authToken, username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );
    }

    public void getUsers(RxCallback<List<String>> callback) {
        disposable = myApi.getUsers(authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );
    }

    public void getUsersShare(RxCallback<List<String>> callback) {
        disposable = myApi.getUsersShare(authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );
    }

    public void changeTypeAccount(RxCallback<String> callback, AccountType accountType) {
        disposable = myApi.changeAccountType(authToken, accountType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );
    }

    public void getAccountType(RxCallback<AccountType> callback) {
        disposable = myApi.getAccountType(authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onError(error.getMessage())
                );
    }

}