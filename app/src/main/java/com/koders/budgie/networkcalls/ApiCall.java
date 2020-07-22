package com.koders.budgie.networkcalls;

import com.koders.budgie.model.BirdModel;
import com.koders.budgie.model.BirdResponse;
import com.koders.budgie.model.Data;
import com.koders.budgie.model.Profile;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiCall {

    @Multipart
    @POST("/auth/register/")
    Call<Data> registerUser(@Part("username") RequestBody username,
                            @Part("email") RequestBody email,
                            @Part("password") RequestBody password,
                            @Part("password2") RequestBody password2,
                            @Part("first_name") RequestBody firstName,
                            @Part("last_name") RequestBody lastName,
                            @Part MultipartBody.Part image,
                            @Part("country") RequestBody country,
                            @Part("tag_line") RequestBody tagLine);

    @FormUrlEncoded
    @POST("/auth/verify/")
    Call<Data> verifyCode(@Header("Authorization") String authorization, @Field("code") String code);

    @GET("/auth/Send-code-again/")
    Call<Data> resendCode(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("/auth/login/")
    Call<Data> loginUser(@Field("email") String email, @Field("password") String password);

    @POST("/auth/logout/")
    Call<Data> logoutUser(@Header("Authorization") String authorization);

    @Multipart
    @POST("/birds/birds-info/")
    Call<Data> addBirdInfo(@Header("Authorization") String authorization,
                           @Part MultipartBody.Part image,
                           @Part("Ring_no") RequestBody ringNum,
                           @Part("Sex") RequestBody sex,
                           @Part("Hatch_date") RequestBody hatchDate,
                           @Part("Arrival_date") RequestBody arrivalDate,
                           @Part("Approx_age") RequestBody approxAge,
                           @Part("Size") RequestBody size,
                           @Part("Color") RequestBody color,
                           @Part("Crested") RequestBody crested,
                           @Part("Father") RequestBody father,
                           @Part("Mother") RequestBody mother,
                           @Part("Status") RequestBody status,
                           @Part("Cage_number") RequestBody cageNumber,
                           @Part("Ring_owner_name") RequestBody ringOwnerName,
                           @Part("Purchased_price") RequestBody purchasedPrice,
                           @Part("Taken_from") RequestBody takenFrom,
                           @Part("Taken_date") RequestBody takenDate,
                           @Part("Seller_number") RequestBody sellerNumber,
                           @Part("Seller_Location") RequestBody sellerLocation,
                           @Part("Selling_price") RequestBody sellingPrice,
                           @Part("Given_to") RequestBody givenTo,
                           @Part("Given_date") RequestBody givenDate,
                           @Part("Buyer_number") RequestBody buyerNumber,
                           @Part("Buyer_Location") RequestBody buyerLocation,
                           @Part("With_partnership") RequestBody withPartnership,
                           @Part("Mutation") RequestBody mutation);

    @GET("/birds/pagination/{start}/{end}")
    Call<BirdResponse> getAllBirds(@Header("Authorization") String authorization, @Path("start") int start, @Path("end") int end);

    @GET("/birds/filter")
    Call<BirdModel> getBird(@Query("ring_no") String ringNum);

    @DELETE("/birds/birds-info/")
    Call<Data> deleteBird(@Header("Authorization") String authorization, @Query("ring_no") String ringNum);

    @Multipart
    @PUT("/birds/birds-info/")
    Call<BirdModel> updateBirdInfo(@Header("Authorization") String authorization,
                                   @Query("ring_no") String ring_no,
                                   @Part MultipartBody.Part image,
                                   @Part("Ring_no") RequestBody ringNum,
                                   @Part("Sex") RequestBody sex,
                                   @Part("Hatch_date") RequestBody hatchDate,
                                   @Part("Arrival_date") RequestBody arrivalDate,
                                   @Part("Approx_age") RequestBody approxAge,
                                   @Part("Size") RequestBody size,
                                   @Part("Color") RequestBody color,
                                   @Part("Crested") RequestBody crested,
                                   @Part("Father") RequestBody father,
                                   @Part("Mother") RequestBody mother,
                                   @Part("Status") RequestBody status,
                                   @Part("Cage_number") RequestBody cageNumber,
                                   @Part("Ring_owner_name") RequestBody ringOwnerName,
                                   @Part("Purchased_price") RequestBody purchasedPrice,
                                   @Part("Taken_from") RequestBody takenFrom,
                                   @Part("Taken_date") RequestBody takenDate,
                                   @Part("Seller_number") RequestBody sellerNumber,
                                   @Part("Seller_Location") RequestBody sellerLocation,
                                   @Part("Selling_price") RequestBody sellingPrice,
                                   @Part("Given_to") RequestBody givenTo,
                                   @Part("Given_date") RequestBody givenDate,
                                   @Part("Buyer_number") RequestBody buyerNumber,
                                   @Part("Buyer_Location") RequestBody buyerLocation,
                                   @Part("With_partnership") RequestBody withPartnership,
                                   @Part("Mutation") RequestBody mutation);

    @Multipart
    @PUT("/auth/user-info/")
    Call<Profile> updateProfile(@Header("Authorization") String authorization,
                                @Part("username") RequestBody username,
                                @Part("email") RequestBody email,
                                @Part("first_name") RequestBody firstName,
                                @Part("last_name") RequestBody lastName,
                                @Part MultipartBody.Part image,
                                @Part("country") RequestBody country,
                                @Part("tag_line") RequestBody tagLine);
}
