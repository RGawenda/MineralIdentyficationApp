package com.apps.mineralidentyficationapp.rest;

import com.apps.mineralidentyficationapp.collection.MineralMessage;
import com.apps.mineralidentyficationapp.collection.Minerals;
import com.apps.mineralidentyficationapp.rest.messages.AuthRequest;
import com.apps.mineralidentyficationapp.rest.messages.AuthenticationResponse;
import com.apps.mineralidentyficationapp.rest.messages.ClassificationResultMessage;
import com.apps.mineralidentyficationapp.rest.messages.RegisterRequest;
import com.apps.mineralidentyficationapp.utils.AccountType;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface MineralAppApi {
    // Classification
    @FormUrlEncoded
    @POST("classification-rest/mineral-classification")
    Single<Map<String, Double>> getClassification(@Header("Authorization") String token,
                                                  @Field("image") String image);

    @GET("classification-rest/get-minerals-names")
    Single<List<String>> getMineralsNames(@Header("Authorization") String token);


    @GET("classification-rest/get-mineral")
    Single<Minerals> getMineral(@Header("Authorization") String token,
                                @Query("mineralName") String mineralName);

    @GET(value = "collection-rest/all-collection")
    Single<List<MineralMessage>> getCollections(@Header("Authorization") String token,
                                                @Query("page") int page,
                                                @Query("pageSize") int pageSize,
                                                @Query("filter") String filter);

    @GET("collection-rest/get-tags")
    Single<List<String>> getTags(@Header("Authorization") String token,
                                 @Query("user") String parameter);

    //CRUD Minerals
    @POST(value = "collection-rest/add-mineral-to-collection")
    Single<MineralMessage> addMineralToCollection(@Header("Authorization") String token,
                                                  @Body MineralMessage mineral);

    @PUT(value = "collection-rest/edit-mineral-in-collection")
    Single<MineralMessage> editMineralInCollection(@Header("Authorization") String token,
                                                   @Body MineralMessage editedMineralMessage);

    @DELETE(value = "collection-rest/delete-mineral-from-collection")
    Single<Object> deleteMineralFromCollection(@Header("Authorization") String token,
                                               @Query("id") Long id);

    //auth
    @POST(value = "api/auth/login")
    Single<AuthenticationResponse> login(@Body AuthRequest authRequest);

    @POST(value = "api/auth/register")
    Single<AuthenticationResponse> register(@Body RegisterRequest registerRequest);

    //collection
    @PUT(value = "collection-rest/change-account-type")
    Single<String> changeAccountType(@Header("Authorization") String token, @Query("accountType") AccountType accountType);
    @GET(value = "collection-rest/get-account-type")
    Single<AccountType> getAccountType(@Header("Authorization") String token);

    @GET(value = "collection-rest/get-users")
    Single<List<String>> getUsers(@Header("Authorization") String token);
    @GET(value = "collection-rest/get-share-users")
    Single<List<String>> getUsersShare(@Header("Authorization") String token);
    @POST(value = "collection-rest/share-collection")
    Single<String> shareCollection(@Header("Authorization") String token, @Query("username") String username);
    @DELETE(value = "collection-rest/unshared-collection")
    Single<String> unsharedTheCollection(@Header("Authorization") String token, @Query("username") String username);
}