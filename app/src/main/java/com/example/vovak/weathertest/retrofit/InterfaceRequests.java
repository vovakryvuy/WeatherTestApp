package com.example.vovak.weathertest.retrofit;

import com.example.vovak.weathertest.model_response.ModelDayWeatherForecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by vovak on 26.04.2018.
 */

public interface InterfaceRequests {

    @GET("forecast")
    Call<ModelDayWeatherForecast> getCityByName(@Query("id")Integer id_city,
                                                @Query("units")String units,
                                                @Query("appid") String appid);
}
