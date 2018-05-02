package com.example.vovak.weathertest.model_weather;

import io.realm.RealmObject;

/**
 * Created by vovak on 27.04.2018.
 */

public class NightWeather extends RealmObject{
    private String date;
    private String describe;
    private String minTemp;
    private String maxTemp;
    private String urlIcon;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdIcon() {
        return urlIcon;
    }

    public String getDescribe() {
        return describe;
    }

    public String getUrlIcon() {
        return urlIcon;
    }

    public void setUrlIcon(String urlIcon) {
        this.urlIcon = urlIcon;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }
}
