package com.apps.mineralidentyficationapp.rest;

import com.apps.mineralidentyficationapp.collection.MineralMessage;
import com.apps.mineralidentyficationapp.collection.Minerals;
import com.apps.mineralidentyficationapp.rest.messages.ClassificationResultMessage;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.Body;
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
    Single<List<MineralMessage>> getCollections(@Query("page") int page, @Query("pageSize") int pageSize, @Query("username") String username);

    @GET(value = "collection-rest/collected-mineral")
    Single<ClassificationResultMessage> getMineralCollections(@Query("param") String parameter);

    //CRUD Minerals in collectio
    @POST(value = "collection-rest/add-mineral-to-collection")
    Single<MineralMessage> addMineralToCollection(@Query("id") Long id, @Body MineralMessage mineral);

    @GET(value = "collection-rest/show-mineral")
    Single<Minerals> getMineralFromCollection(@Query("mineralId") String mineralId);

    @PUT(value = "collection-rest/edit-mineral-in-collection")
    Single<MineralMessage> editMineralInCollection(@Query("id") Long id, @Body MineralMessage editedMineralMessage);

    @DELETE(value = "collection-rest/delete-mineral-from-collection")
    Single<Object> deleteMineralFromCollection(@Query("id") Long id);

    //CRUD image
    @GET(value = "show-image")
    Single<ClassificationResultMessage> getImage(@Query("imageId") String imageId);

    //profile
    @GET(value = "show-profile")
    Single<ClassificationResultMessage> showProfile(@Query("userId") String userId);
}