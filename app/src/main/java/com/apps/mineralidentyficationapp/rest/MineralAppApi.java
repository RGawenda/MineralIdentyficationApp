package com.apps.mineralidentyficationapp.rest;

import com.apps.mineralidentyficationapp.collection.MineralMessage;
import com.apps.mineralidentyficationapp.collection.Minerals;
import com.apps.mineralidentyficationapp.rest.messages.ClassificationResultMessage;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface MineralAppApi {
    // Classification
    @FormUrlEncoded
    @POST("classification-rest/mineral-classification")
    Single<Map<String, Double>> getClassification(@Field("image") String image);

    @GET("classification-rest/get-minerals-names")
    Single<List<String>> getMineralsNames();

    @GET("classification-rest/get-mineral")
    Single<Minerals> getMineral(@Query("mineralName") String mineralName);

    @GET(value = "collection-rest/all-collection")
    Single<ClassificationResultMessage> getCollections(@Query("param") String parameter);

    @GET(value = "collection-rest/collected-mineral")
    Single<ClassificationResultMessage> getMineralCollections(@Query("param") String parameter);

    //CRUD Minerals in collection
    @POST(value = "collection-rest/add-image-to-collection")
    Single<MineralMessage> addMineralToCollection(@Field("newMineral") MineralMessage newMineral);

    @GET(value = "collection-rest/show-mineral")
    Single<ClassificationResultMessage> getMineralFromCollection(@Query("mineralId") String mineralId);

    @PUT(value = "collection-rest/edit-mineral-to-collection")
    Single<ClassificationResultMessage> editMineralInCollection(@Query("mineralId") String mineralId, @Query("editedMineral") String editedMineral);

    @DELETE(value = "collection-rest/delete-mineral-to-collection")
    Single<ClassificationResultMessage> deleteMineralFromCollection(@Query("mineralId") String mineralId);

    //CRUD image
    @POST(value = "add-image")
    Single<ClassificationResultMessage> addImage(@Query("image") String image);

    @GET(value = "show-image")
    Single<ClassificationResultMessage> getImage(@Query("imageId") String imageId);

    @PUT(value = "edit-image")
    Single<ClassificationResultMessage> editImage(@Query("imageId") String imageId, @Query("mineral") String mineral);

    @DELETE(value = "delete-image")
    Single<ClassificationResultMessage> deleteImage(@Query("imageId") String imageId);

    //profile
    @GET(value = "show-profile")
    Single<ClassificationResultMessage> showProfile(@Query("userId") String userId);
}