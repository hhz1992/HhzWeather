package com.gwu.huanzhou.hhzweather.model;

/**
 * Created by Huanzhou on 2015/10/23.
 */
public class Condition {


    private String mWeather;
    private String mTemperatureString;
    private String mTemperatureF;
    private String mTemperatureC;
    private String mRelativeHumidity;
    private String mWind;
    private String mWindMph;
    private String mWindDir;
    private String mIconUrl;
    private String mDewpointF;
    private String mDewpointC;
    private String mVisibilityMi;
    private String mPressureIn;

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


    public String getmDewpointF() {
        return mDewpointF;
    }

    public void setmDewpointF(String mDewpointF) {
        this.mDewpointF = mDewpointF;
    }

    public String getmDewpointC() {
        return mDewpointC;
    }

    public void setmDewpointC(String mDewpointC) {
        this.mDewpointC = mDewpointC;
    }

    public String getmVisibilityMi() {
        return mVisibilityMi;
    }

    public void setmVisibilityMi(String mVisibilityMi) {
        this.mVisibilityMi = mVisibilityMi;
    }

    public String getmPressureIn() {
        return mPressureIn;
    }

    public void setmPressureIn(String mPressureIn) {
        this.mPressureIn = mPressureIn;
    }

    public String getmTemperatureString() {
        return mTemperatureString;
    }

    public String getmWindMph() {
        return mWindMph;
    }

    public void setmWindMph(String mWindMph) {
        this.mWindMph = mWindMph;
    }

    public String getmWindDir() {
        return mWindDir;
    }

    public void setmWindDir(String mWindDir) {
        this.mWindDir = mWindDir;
    }

}
