package com.app.veka.Network;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServices {

    @FormUrlEncoded
    @POST("user")
    Call<JsonObject> userLogin(
            @Field("mobile") String mobile,
            @Field("password") String password
    );


    @GET("billing")
    Call<JsonObject> Billing(
            @Query("cus_id") String cid
    );

    @FormUrlEncoded
    @POST("change_password")
    Call<JsonObject> changePasword(
            @Field("cus_id") String customerid,
            @Field("npass") String newpassword,
            @Field("cpass") String cpass
    );


    @FormUrlEncoded
    @POST("extra")
    Call<JsonObject> extaDemond(
            @Field("cus_id") String custid,
            @Field("extra") String extra,
            @Field("date") String date
    );


    @GET("user?cus_id=")
    Call<JsonObject> getUserDetails(
            @Query("cus_id") String cid
    );


    @GET("supply?cus_id=")
    Call<JsonObject> getSupplies(
            @Query("cus_id") String cid
    );

    @FormUrlEncoded
    @POST("extra")
    Call<JsonObject> todayCancelOrder(
            @Field("cus_id") String cusid,
            @Field("extra") String status,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST("permanent_close")
    Call<JsonObject> parementColse(
            @Field("cus_id") String customerid,
            @Field("msg") String message
    );


    @FormUrlEncoded
    @POST("user_profile")
    Call<JsonObject> updateprofile(
            @Field("cus_id") String customerid,
            @Field("name") String name,
            @Field("email") String email,
            @Field("d_ltr") String d_ltr,
            @Field("type") String type,
            @Field("gap") String gap,
            @Field("area") String area,
            @Field("shift") String shift
    );


    @FormUrlEncoded
    @POST("regular_demand")
    Call<JsonObject> updateDilyleater(
            @Field("cus_id") String customerid,
            @Field("d_ltr") String d_ltr
    );


    @GET("notification?cus_id=")
    Call<JsonObject> getNotification(
            @Query("cus_id") String cid
    );


    @FormUrlEncoded
    @POST("payment")
    Call<JsonObject> uploadPaymentImage(
            @Field("cus_id") String customerid,
            @Field("image") String image
    );


    @FormUrlEncoded
    @POST("payment")
    Call<JSONObject> image(
            @Field("cus_id") String id,
            @Field("image") String image
    );


    @FormUrlEncoded
    @POST("payment")
    Call<JsonObject> sendImage(
            @Field("cus_id") String id,
            @Field("image") String imagestring);

    @FormUrlEncoded
    @POST("cash")
    Call<JsonObject> sendCash(
            @Field("cus_id") String id,
            @Field("cash") String cash);


    @GET("dairy_status")
    Call<JsonObject> getCloseStatus();


    @FormUrlEncoded
    @POST("add_notification")
    Call<JsonObject> addNotificatin(
            @Field("cus_id") String id,
            @Field("msg") String message,
            @Field("status") String status

    );


    @FormUrlEncoded
    @POST("NotificationByDate")
    Call<JsonObject> mutipulDate(
            @Field("cus_id") String custid,
            @Field("dates") String dates,
            @Field("msg") String message

    );

}
