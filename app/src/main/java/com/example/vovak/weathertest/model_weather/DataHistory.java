package com.example.vovak.weathertest.model_weather;

import io.realm.RealmObject;

/**
 * Created by vovak on 30.04.2018.
 */

public class DataHistory extends RealmObject {

    String cityName;
    long idCity;
    String currentDate;
    int numberstyle;

    public int getNumberstyle() {
        return numberstyle;
    }

    public void setNumberstyle(int numberstyle) {
        this.numberstyle = numberstyle;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public long getIdCity() {
        return idCity;
    }

    public void setIdCity(long idCity) {
        this.idCity = idCity;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

}
