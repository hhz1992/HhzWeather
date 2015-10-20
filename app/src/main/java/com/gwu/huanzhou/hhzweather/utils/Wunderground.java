package com.gwu.huanzhou.hhzweather.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.Base64;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gwu.huanzhou.hhzweather.Constants;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * Created by Huanzhou on 2015/10/19.
 */
public class Wunderground {


    public static JsonObject queryWundergroundForLocation(String longitude, String latitude, Context context) {
        try {
            return
                    Ion.with(context).load(Constants.Wunderground_SEARCH_LOCATION_URL+longitude+","+latitude+".json")
                            .asJsonObject().get();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }


//    public static void main(String[] args) {
//
//        JsonObject json = queryWundergroundForLocation("38.846846846846844", "-77.03909252879366", null);
//
//        System.out.println(json);
//
//    }





    public static String parseLocationFromWundergroundJSON(JsonObject jsonObject) throws Exception{

        JsonObject locationResult = jsonObject.getAsJsonObject("location");


                        //getAsJsonArray("results").get(0).getAsJsonObject().getAsJsonArray("Image");


        if (locationResult != null) {

            String state = locationResult.get("state").getAsString();
            String zip = locationResult.get("zip").getAsString();

            System.out.println("state:"+ state);
            System.out.println("zip:"+ zip);

            return zip;
        }

        return null;
    }








    public static URL parseURLFromBingJSON(JsonObject jsonObject, int desiredOrientation) throws Exception{
        JsonArray imageResults = jsonObject
                .getAsJsonObject("d").getAsJsonArray("results").get(0).getAsJsonObject().getAsJsonArray("Image");
        if (imageResults != null && imageResults.size() > 0) {
            for (int i = 0; i < imageResults.size(); i++) {
                JsonObject imageResult = imageResults.get(i).getAsJsonObject();
                boolean tooBig = imageResult.get("FileSize").getAsInt() > Constants.MAX_IMAGE_FILE_SIZE_IN_BYTES;

                if (tooBig == false) {
                    int width = imageResult.get("Width").getAsInt();
                    int height = imageResult.get("Height").getAsInt();

                    if (desiredOrientation == Configuration.ORIENTATION_PORTRAIT) {
                        if (height > width) {
                            return new URL(imageResult.get("MediaUrl").getAsString());
                        }
                    } else if (desiredOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                        if (width > height) {
                            return new URL(imageResult.get("MediaUrl").getAsString());
                        }
                    }
                }
            }
        }

        return null;
    }

    public static JsonObject queryBingForImage(String query, Context context) {
        try {
            return Ion.with(context).load(Constants.BING_SEARCH_URL).addQuery("Sources","'image'")
                    .addHeader("Authorization", "Basic " + Base64.encodeToString((Constants.BING_SEARCH_API_TOKEN + ":" + Constants.BING_SEARCH_API_TOKEN).getBytes("UTF-8"), Base64.NO_WRAP))
                    .addQuery("Adult","'Strict'")
                    .addQuery("Query","'" + query + "'")
                    .addQuery("$format","JSON")
                    .asJsonObject().get();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean saveReactionImage(Bitmap bitmap, File directory){
        File image = new File(directory,Constants.REACTION_IMAGE_FILE_NAME);

        FileOutputStream outStream;
        try {

            outStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outStream);

            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
