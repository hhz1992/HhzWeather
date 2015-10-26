package com.gwu.huanzhou.hhzweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gwu.huanzhou.hhzweather.model.Condition;

/**
 * Created by Huanzhou on 2015/10/25.
 */
public class PersistanceManager {
    private Context mContext;

    public PersistanceManager(Context context) {
        mContext = context;
    }


    public boolean saveConditionLocally(Condition condition) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Constants.TEMPERATURE_F, condition.getmTemperatureF());
        editor.putString(Constants.TEMPERATURE_C, condition.getmTemperatureC());
        editor.putString(Constants.ICON_URL, condition.getmIconUrl());
        editor.putString(Constants.WIND, condition.getmWind());
        editor.putString(Constants.RELATIVE_HUMIDITY, condition.getmRelativeHumidity());
        editor.putString(Constants.WEATHER, condition.getmWeather());
        editor.putString(Constants.CITY, condition.getmDisplaylocation().getmCity());
        editor.putString(Constants.STATE, condition.getmDisplaylocation().getmState());
        editor.putString(Constants.COUNTRY, condition.getmDisplaylocation().getmCountry());
        editor.putString(Constants.DEWPOINT_F, condition.getmDewpointF());
        editor.putString(Constants.DEWPOINT_C, condition.getmDewpointC());
        editor.putString(Constants.VISIBILITY_MI, condition.getmVisibilityMi());
        editor.putString(Constants.PRESSURE_IN, condition.getmPressureIn());
        editor.putString(Constants.WIND_MPH,condition.getmWindMph());
        editor.putString(Constants.WIND_DIR,condition.getmWindDir());

        editor.apply();

        return true;

    }

    public boolean getCurrentCondition(Condition condition) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        condition.setmIconUrl(sharedPreferences.getString(Constants.ICON_URL, ""));
        condition.setmTemperatureF(sharedPreferences.getString(Constants.TEMPERATURE_F, ""));
        condition.setmTemperatureC(sharedPreferences.getString(Constants.TEMPERATURE_C, ""));
        condition.setmWind(sharedPreferences.getString(Constants.WIND, ""));
        condition.setmRelativeHumidity(sharedPreferences.getString(Constants.RELATIVE_HUMIDITY, ""));
        condition.setmWeather(sharedPreferences.getString(Constants.WEATHER, ""));
        condition.getmDisplaylocation().setmCity(sharedPreferences.getString(Constants.CITY, ""));
        condition.getmDisplaylocation().setmCountry(sharedPreferences.getString(Constants.COUNTRY, ""));
        condition.getmDisplaylocation().setmState(sharedPreferences.getString(Constants.STATE, ""));
        condition.setmDewpointF(sharedPreferences.getString(Constants.DEWPOINT_F, ""));
        condition.setmDewpointC(sharedPreferences.getString(Constants.DEWPOINT_C, ""));
        condition.setmVisibilityMi(sharedPreferences.getString(Constants.VISIBILITY_MI, ""));
        condition.setmPressureIn(sharedPreferences.getString(Constants.PRESSURE_IN, ""));
        condition.setmWindMph(sharedPreferences.getString(Constants.WIND_MPH, ""));
        condition.setmWindDir(sharedPreferences.getString(Constants.WIND_DIR,""));

        return true;
    }

    public String getCurrentTempDisplay() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString(Constants.TEMPDISPLAY, "");
    }


    public void saveTempDisplay(String tempDisplay) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Constants.TEMPDISPLAY, tempDisplay);
        editor.apply();
    }

    public String getCurrentDayDisplay() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString(Constants.DAYDISPLAY, "");
    }


    public void saveDayDisplay(String dayDisplay) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Constants.DAYDISPLAY, dayDisplay);
        editor.apply();
    }


}
