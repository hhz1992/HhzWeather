package com.gwu.huanzhou.hhzweather.utils;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gwu.huanzhou.hhzweather.Constants;
import com.gwu.huanzhou.hhzweather.model.Condition;
import com.gwu.huanzhou.hhzweather.model.Displaylocation;
import com.gwu.huanzhou.hhzweather.model.Forecast;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by Huanzhou on 2015/10/19.
 */
public class Wunderground {


    public static JsonObject queryWundergroundForLocation(String longitude, String latitude, Context context) {
        try {
            return
                    Ion.with(context).load(Constants.Wunderground_SEARCH_LOCATION_URL + longitude + "," + latitude + ".json")
                            .asJsonObject().get();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static String parseLocationFromWundergroundJSON(JsonObject jsonObject) throws Exception {

        JsonObject locationResult = jsonObject.getAsJsonObject("location");

        if (locationResult != null) {

            String state = locationResult.get("state").getAsString();
            String zip = locationResult.get("zip").getAsString();

            System.out.println("state:" + state);
            System.out.println("zip:" + zip);

            return zip;
        }

        return null;
    }


    public static JsonObject queryWundergroundForCondition(String zip, Context context) {
        try {
            return
                    Ion.with(context).load(Constants.Wunderground_SEARCH_CONDITION_ZIP_URL + zip + ".json")
                            .asJsonObject().get();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static JsonObject queryWundergroundForCondition(String longitude, String latitude, Context context) {
        try {
            return
                    Ion.with(context).load(Constants.Wunderground_SEARCH_CONDITION_GPS_URL + longitude + "," + latitude + ".json")
                            .asJsonObject().get();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static boolean parseConditionFromWundergroundJSON(JsonObject jsonObject, Condition condition) throws Exception {

        JsonObject conditionResult = jsonObject.getAsJsonObject("current_observation");

        if (conditionResult != null) {

            condition.setmWeather(conditionResult.get("weather").getAsString());
            condition.setmTemperatureString(conditionResult.get("temperature_string").getAsString());
            condition.setmTemperatureF(conditionResult.get("temp_f").getAsString() + " F");
            condition.setmTemperatureC(conditionResult.get("temp_c").getAsString() + " C");

            condition.setmWind(conditionResult.get("wind_string").getAsString());
            condition.setmRelativeHumidity(conditionResult.get("relative_humidity").getAsString());
            condition.setmIconUrl(conditionResult.get("icon_url").getAsString());

            condition.setmDewpointC(conditionResult.get("dewpoint_c").getAsString() + " C");
            condition.setmDewpointF(conditionResult.get("dewpoint_f").getAsString() + " F");
            condition.setmPressureIn(conditionResult.get("pressure_in").getAsString());
            condition.setmVisibilityMi(conditionResult.get("visibility_mi").getAsString() + " Mi");
            condition.setmWindMph(conditionResult.get("wind_mph").getAsDouble() + "Mph");
            condition.setmWindDir(conditionResult.get("wind_dir").getAsString());

            JsonObject displayLocationJson = conditionResult.getAsJsonObject("display_location");
            Displaylocation displaylocation = new Displaylocation();

            displaylocation.setmCity(displayLocationJson.get("city").getAsString());
            displaylocation.setmCountry(displayLocationJson.get("country").getAsString());
            displaylocation.setmState(displayLocationJson.get("state").getAsString());

            condition.setmDisplaylocation(displaylocation);

            System.out.println("displaylocation:" + displaylocation.getmCountry());

            System.out.println("weather:" + condition.getmWeather());

            return true;
        }

        return false;
    }


    public static JsonObject queryWundergroundForForecast(String state, String city, Context context) {
        try {
            return
                    Ion.with(context).load(Constants.Wunderground_SEARCH_FORECAST_URL + state + "/" + city + ".json")
                            .asJsonObject().get();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }


    public static boolean parseForecastFromWundergroundJSON(JsonObject jsonObject, List<Forecast> forecasts) throws Exception {

        JsonArray forecastResults = jsonObject.getAsJsonObject("forecast").getAsJsonObject("simpleforecast").getAsJsonArray("forecastday");

        if (forecastResults != null && forecastResults.size() > 0) {

            for (int i = 0; i < forecastResults.size(); i++) {

                Forecast forecast = new Forecast();

                JsonObject forecastResult = forecastResults.get(i).getAsJsonObject();
                forecast.setmWeekday(forecastResult.getAsJsonObject("date").get("weekday_short").getAsString());
                forecast.setmHighTempF(forecastResult.getAsJsonObject("high").get("fahrenheit").getAsString()+ " F");
                forecast.setmHighTempC(forecastResult.getAsJsonObject("high").get("celsius").getAsString()+ " C");
                forecast.setmLowTempF(forecastResult.getAsJsonObject("low").get("fahrenheit").getAsString()+ " F");
                forecast.setmLowTempC(forecastResult.getAsJsonObject("low").get("celsius").getAsString()+ " C");
                forecast.setmIconUrl(forecastResult.get("icon_url").getAsString());
                forecast.setMmAveHumidity(forecastResult.get("avehumidity").getAsString() + "%");

                forecasts.add(forecast);

                System.out.println("weekday: " + forecast.getmWeekday());
            }
            return true;
        }
        return false;
    }



}
