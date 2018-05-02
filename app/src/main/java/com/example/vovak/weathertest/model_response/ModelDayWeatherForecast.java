package com.example.vovak.weathertest.model_response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vovak on 27.04.2018.
 */

public class ModelDayWeatherForecast {
    @SerializedName("list")
    private List<ModelWeather> items;

    @SerializedName("city")
    private City city;

    private class City {
        String name;
    }

    public ModelDayWeatherForecast(List<ModelWeather> items,City city) {
        this.items = items;
        this.city = city;
    }

    public List<ModelWeather> getItems() {
        return items;
    }

    public String getCity() {
        return String.valueOf(city.name);
    }

}
