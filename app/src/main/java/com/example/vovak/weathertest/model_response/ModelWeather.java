package com.example.vovak.weathertest.model_response;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.List;

/**
 * Created by vovak on 27.04.2018.
 */

public class ModelWeather {
    public class WeatherTemp {
        Double temp;
        Double temp_min;
        Double temp_max;
    }

    public class WeatherDescription {
        String icon;
        String main;
    }

    @SerializedName("main")
    private WeatherTemp temp;

    @SerializedName("weather")
    private List<WeatherDescription> desctiption;

    @SerializedName("name")
    private String city;

    @SerializedName("dt")
    private long timestamp;

    public ModelWeather(WeatherTemp temp, List<WeatherDescription> desctiption) {
        this.temp = temp;
        this.desctiption = desctiption;
    }

    public Calendar getDate() {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(timestamp * 1000);
        return date;
    }

    public String getTemp() { return String.valueOf(temp.temp); }

    public String getTempMin() { return String.valueOf(temp.temp_min); }

    public String getTempMax() { return String.valueOf(temp.temp_max); }

    public String getTempInteger() { return String.valueOf(temp.temp.intValue()); }

    public String getTempWithDegree() { return String.valueOf(temp.temp.intValue()) + "\u00B0"; }

    public String getCity() { return city; }

    public String getIcon() { return desctiption.get(0).icon; }

    public String getIconUrl() {
        return "http://openweathermap.org/img/w/" + desctiption.get(0).icon + ".png";
    }
    public String getDesctiption(){
        return desctiption.get(0).main;
    }
}
