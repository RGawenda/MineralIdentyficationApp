package com.gymapps.mineralidentyficationapp.rest;

import com.gymapps.mineralidentyficationapp.rest.messages.ClassificationMessage;
import com.gymapps.mineralidentyficationapp.rest.messages.ClassificationResultMessageClass;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MineralAppApi {
    // Classification
    @GET("mineral-classification")
    Call<ClassificationResultMessageClass> getClassification(@Query("param") String param);

    @GET(value = "all-collection")
    Call<ClassificationResultMessageClass> getCollections(@Query("param") String parameter);

    @GET(value = "collected-mineral")
    Call<ClassificationResultMessageClass> getMineralCollections(@Query("param") String parameter);

    //CRUD Minerals in collection
    @POST(value = "add-image-to-collection")
    Call<ClassificationResultMessageClass> addMineralToCollection(@Query("param") String parameter);

    @GET(value = "show-mineral")
    Call<ClassificationResultMessageClass> getMineralFromCollection(@Query("param") String parameter);

    @PUT(value = "edit-mineral-to-collection")
    Call<ClassificationResultMessageClass> editMineralInCollection(@Query("param") String parameter);

    @DELETE(value = "delete-mineral-to-collection")
    Call<ClassificationResultMessageClass> deleteMineralFromCollection(@Query("param") String parameter);


    //CRUD image
    @POST(value = "add-image")
    Call<ClassificationResultMessageClass> addImage(@Query("param") String parameter);

    @GET(value = "show-image")
    Call<ClassificationResultMessageClass> getImage(@Query("param") String parameter);

    @PUT(value = "edit-image")
    Call<ClassificationResultMessageClass> editImage(@Query("param") String parameter);

    @DELETE(value = "delete-image")
    Call<ClassificationResultMessageClass> deleteImage(@Query("param") String parameter);

    //profile
    @GET(value = "show-profile")
    Call<ClassificationResultMessageClass> showProfile(@Query("param") String parameter);
}