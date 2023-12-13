package com.apps.mineralidentyficationapp.rest;

import com.apps.mineralidentyficationapp.rest.messages.ClassificationResultMessage;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface MineralAppApi {
    // Classification
    @GET("mineral-classification")
    Single<Map<String, Double>> getClassification(@Query("image") String image);

    @GET(value = "all-collection")
    Call<ClassificationResultMessage> getCollections(@Query("param") String parameter);

    @GET(value = "collected-mineral")
    Call<ClassificationResultMessage> getMineralCollections(@Query("param") String parameter);

    //CRUD Minerals in collection
    @POST(value = "add-image-to-collection")
    Call<ClassificationResultMessage> addMineralToCollection(@Query("image") String image, @Query("newMineral") String newMineral);

    @GET(value = "show-mineral")
    Call<ClassificationResultMessage> getMineralFromCollection(@Query("mineralId") String mineralId);

    @PUT(value = "edit-mineral-to-collection")
    Call<ClassificationResultMessage> editMineralInCollection(@Query("mineralId") String mineralId, @Query("editedMineral") String editedMineral);

    @DELETE(value = "delete-mineral-to-collection")
    Call<ClassificationResultMessage> deleteMineralFromCollection(@Query("mineralId") String mineralId);

    //CRUD image
    @POST(value = "add-image")
    Call<ClassificationResultMessage> addImage(@Query("image") String image);

    @GET(value = "show-image")
    Call<ClassificationResultMessage> getImage(@Query("imageId") String imageId);

    @PUT(value = "edit-image")
    Call<ClassificationResultMessage> editImage(@Query("imageId") String imageId, @Query("mineral") String mineral);

    @DELETE(value = "delete-image")
    Call<ClassificationResultMessage> deleteImage(@Query("imageId") String imageId);

    //profile
    @GET(value = "show-profile")
    Call<ClassificationResultMessage> showProfile(@Query("userId") String userId);
}