package com.gwu.huanzhou.hhzweather.model;

/**
 * Created by Huanzhou on 2015/10/25.
 */
public class Forecast {

    private String mWeekday;
    private String mIconUrl;
    private String mHighTempF;
    private String mLowTempF;
    private String mHighTempC;
    private String mLowTempC;
    private String mmAveHumidity;

    public String getmWeekday() {
        return mWeekday;
    }

    public void setmWeekday(String mWeekday) {
        this.mWeekday = mWeekday;
    }

    public String getmIconUrl() {
        return mIconUrl;
    }

    public void setmIconUrl(String mIconUrl) {
        this.mIconUrl = mIconUrl;
    }

    public String getmHighTempF() {
        return mHighTempF;
    }

    public void setmHighTempF(String mHighTempF) {
        this.mHighTempF = mHighTempF;
    }

    public String getmLowTempF() {
        return mLowTempF;
    }

    public void setmLowTempF(String mLowTempF) {
        this.mLowTempF = mLowTempF;
    }

    public String getmHighTempC() {
        return mHighTempC;
    }

    public void setmHighTempC(String mHighTempC) {
        this.mHighTempC = mHighTempC;
    }

    public String getmLowTempC() {
        return mLowTempC;
    }

    public void setmLowTempC(String mLowTempC) {
        this.mLowTempC = mLowTempC;
    }

    public String getMmAveHumidity() {
        return mmAveHumidity;
    }

    public void setMmAveHumidity(String mmAveHumidity) {
        this.mmAveHumidity = mmAveHumidity;
    }
}
