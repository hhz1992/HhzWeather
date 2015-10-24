package com.gwu.huanzhou.hhzweather.model;

/**
 * Created by Huanzhou on 2015/10/23.
 */
public class Condition {

    public String getmTemperatureString() {
        return mTemperatureString;
    }

    public void setmTemperatureString(String mTemperatureString) {
        this.mTemperatureString = mTemperatureString;
    }

    public String getmTemperatureF() {
        return mTemperatureF;
    }

    public void setmTemperatureF(String mTemperatureF) {
        this.mTemperatureF = mTemperatureF;
    }

    public String getmTemperatureC() {
        return mTemperatureC;
    }

    public void setmTemperatureC(String mTemperatureC) {
        this.mTemperatureC = mTemperatureC;
    }

    private String mWeather;
    private String mTemperatureString;
    private String mTemperatureF;
    private String mTemperatureC;


    private String mRelativeHumidity;
    private String mWind;
    private String mIconUrl;

    private Displaylocation mDisplaylocation;


    public String getmWeather() {
        return mWeather;
    }

    public void setmWeather(String mWeather) {
        this.mWeather = mWeather;
    }



    public String getmRelativeHumidity() {
        return mRelativeHumidity;
    }

    public void setmRelativeHumidity(String mRelativeHumidity) {
        this.mRelativeHumidity = mRelativeHumidity;
    }

    public String getmWind() {
        return mWind;
    }

    public void setmWind(String mWind) {
        this.mWind = mWind;
    }

    public String getmIconUrl() {
        return mIconUrl;
    }

    public void setmIconUrl(String mIconUrl) {
        this.mIconUrl = mIconUrl;
    }

    public Displaylocation getmDisplaylocation() {
        return mDisplaylocation;
    }

    public void setmDisplaylocation(Displaylocation mDisplaylocation) {
        this.mDisplaylocation = mDisplaylocation;
    }
}
