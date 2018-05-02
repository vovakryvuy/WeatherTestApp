package com.example.vovak.weathertest.model_weather;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by vovak on 27.04.2018.
 */

public class Data extends RealmObject{

    @PrimaryKey
    private String uuid;
    private String nameCity;

    public RealmList<DayWeather> dayWeatherList;
    public RealmList<NightWeather> nightWeatherList;

    public String getNameCity() {
        return nameCity;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }

    public List<DayWeather> getDayWeatherList() {
        return dayWeatherList;
    }

    public void setDayWeatherList(RealmList<DayWeather> dayWeatherList) {
        this.dayWeatherList = dayWeatherList;
    }

    public List<NightWeather> getNightWeatherList() {
        return nightWeatherList;
    }

    public void setNightWeatherList(RealmList<NightWeather> nightWeatherList) {
        this.nightWeatherList = nightWeatherList;
    }
}
