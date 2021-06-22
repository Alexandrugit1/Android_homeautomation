package com.example.homeautomation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface JsonPlaceHolderAPI
{
    @GET("api/?ctrl=login")
    Call<UserDetails> authenticateUser(@Query("email") String userEmail,
                                       @Query("pass") String userPassword);

    @FormUrlEncoded
    @POST("api/?ctrl=user")
    Call<String> registerUser(@Field("nume") String userName,
                              @Field("email") String userEmail,
                              @Field("parola") String userPassword,
                              @Field("cnp") String userPIN);

    @GET("/api/?ctrl=is_logged")
    Call<Object> checkIfUserIsAuthenticated();

    @GET("api/?ctrl=logout")
    Call<String> signOutUser();

    @GET("/api/?ctrl=user")
    Call<UserDetails> getUserByPIN(@Query("cnp") String userPIN);

    @GET("/api/?ctrl=user")
    Call<List<UserDetails>> getAllUsers();

    @GET("/api/?ctrl=set_imei")
    Call<String> setUserIMEI(@Query("cnp") String userPIN,
                             @Query("imei") String userIMEI);

    @GET("/api/?ctrl=device")
    Call<ArrayList<Device>> getAllDevices();

    @GET("/api/?ctrl=device")
    Call<Device> getDeviceByID(@Query("device_id") String id);

    @PUT("/api/?ctrl=device")
    Call<String> setDeviceValue(@Query("device_id") String id,
                                @Query("value") String value);

    @GET("/api/?ctrl=temperature")
    Call<ArrayList<Temperature>> getTemperature(@Query("temp_id") String id);

    @FormUrlEncoded
    @POST("/api/?ctrl=program")
    Call<String> insertProgram(@Field("name") String programName,
                               @Field("start_time") String programStartTime,
                               @Field("finish_time") String programFinishTime,
                               @Field("temperature") String programTemperature);

    @GET("/api/?ctrl=program")
    Call<ArrayList<Program>> getAllPrograms();

    @GET("/api/?ctrl=access&enter")
    Call<String> enterHouse(@Query("imei") String userIMEI);

    @GET("/api/?ctrl=access&leave")
    Call<String> leaveHouse(@Query("imei") String userIMEI);

    @GET("/api/?ctrl=access")
    Call<ArrayList<Entry>> getAccessEntries();

    @GET("/api/?ctrl=time")
    Call<CurrentTime> getCurrentTime();
}