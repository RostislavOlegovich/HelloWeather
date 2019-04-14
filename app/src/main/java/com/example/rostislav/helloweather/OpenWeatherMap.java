package com.example.rostislav.helloweatherupd;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMap {

    @GET("/data/2.5/weather")
    Call<WeatherResponcePOJO> getWeatherInfo(@Query("q") String querry,
                                             @Query("units") String units,
                                             @Query("appid") String appId);

    @GET("/data/2.5/weather")
    Call<WeatherResponcePOJO> getWeatherInfo(@Query("lat") String lat,
                                             @Query("lon") String lon,
                                             @Query("units") String units,
                                             @Query("appid") String appId);

    @GET("/data/2.5/forecast")
    Call<WeatherResponcePOJO> getWeatherDailyInfo(@Query("q") String querry,
                                             @Query("units") String units,
                                             @Query("appid") String appId);

    @GET("/data/2.5/forecast")
    Call<WeatherResponcePOJO> getWeatherDailyInfo(@Query("lat") String lat,
                                                       @Query("lon") String lon,
                                                       @Query("units") String units,
                                                       @Query("appid") String appId);



}
