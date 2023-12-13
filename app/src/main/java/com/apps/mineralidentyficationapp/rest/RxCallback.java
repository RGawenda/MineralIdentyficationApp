package com.apps.mineralidentyficationapp.rest;

public interface RxCallback<T> {
    void onSuccess(T result);
    void onError(String errorMessage);
}
