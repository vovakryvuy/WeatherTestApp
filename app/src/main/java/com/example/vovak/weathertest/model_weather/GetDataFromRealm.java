package com.example.vovak.weathertest.model_weather;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by vovak on 29.04.2018.
 */

public class GetDataFromRealm {
    private Realm mRealm;
    private Data mData;
    private List<DayWeather> dayWeatherList;
    private List<NightWeather> nightWeatherList;
    private List<String> discribeDay;
    private List<String> temperatureDay;
    private List<String> urlIconDay;
    private List<String> date;
    private List<String> discribeNight;
    private List<String> temperatureNight;
    private List<String> urlIconNight;

    public GetDataFromRealm() {
        this.mRealm = Realm.getDefaultInstance();
        this.mData = mRealm.where(Data.class).findFirst();
        if(mData != null){
            dayWeatherList = new ArrayList<>();
            nightWeatherList = new ArrayList<>();
            dayWeatherList = mData.getDayWeatherList();
            nightWeatherList = mData.getNightWeatherList();
        }
    }

    public List<String> getDiscribeDay() {
        discribeDay = new ArrayList<>();
        if (mData!= null && !dayWeatherList.isEmpty())
        for (DayWeather dayWeather : dayWeatherList){
            discribeDay.add(dayWeather.getDescribe());
        }
        return discribeDay;
    }

    public List<String> getTemperatureDay() {
        temperatureDay = new ArrayList<>();
        if (mData!= null && !dayWeatherList.isEmpty())
            for (DayWeather dayWeather : dayWeatherList){
                temperatureDay.add(dayWeather.getMaxTemp());
            }
        return temperatureDay;
    }

    public List<String> getUrlIconDay() {
        urlIconDay = new ArrayList<>();
        if (mData!= null && !dayWeatherList.isEmpty())
            for (DayWeather dayWeather : dayWeatherList){
                urlIconDay.add(dayWeather.getUrlIcon());
            }
        return urlIconDay;
    }

    public List<String> getDate() {
        date = new ArrayList<>();
        if (mData!= null && !dayWeatherList.isEmpty())
        for (DayWeather dayWeather : dayWeatherList){
            date.add(dayWeather.getDate());
        }
        return date;
    }

    public List<String> getDiscribeNight() {
        discribeNight = new ArrayList<>();
        if (mData!= null && !nightWeatherList.isEmpty())
            for (NightWeather nightWeather : nightWeatherList){
                discribeNight.add(nightWeather.getDescribe());
            }
        return discribeNight;
    }

    public List<String> getTemperatureNight() {
        temperatureNight = new ArrayList<>();
        if (mData!= null && !nightWeatherList.isEmpty())
            for (NightWeather nightWeather : nightWeatherList){
                temperatureNight.add(nightWeather.getMaxTemp());
            }
        return temperatureNight;
    }

    public List<String> getUrlIconNight() {
        urlIconNight = new ArrayList<>();
        if (mData!= null && !nightWeatherList.isEmpty())
            for (NightWeather nightWeather : nightWeatherList){
                urlIconNight.add(nightWeather.getUrlIcon());
            }
        return urlIconNight;
    }
}
