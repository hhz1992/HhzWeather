package com.gwu.huanzhou.hhzweather.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.gwu.huanzhou.hhzweather.utils.Wunderground;

/**
 * Created by Huanzhou on 2015/10/19.
 */
public class LocationSearchAsyncTask  extends AsyncTask<String,Integer,String> {


    private static final String TAG = "LocationSearchAsyncTask";
    private Context mContext;
    private LocationSearchCompletionListener mCompletionListener;


    public interface LocationSearchCompletionListener{
        public void WundergroundLocationFound(String zip);
        public void WundergroundLocationNotFound();
    }

    public LocationSearchAsyncTask(Context context, LocationSearchCompletionListener completionListener){
        mContext = context;
        mCompletionListener = completionListener;
    }


    @Override
    protected String doInBackground(String... query) {

        try{
            JsonObject jsonResult = Wunderground.queryWundergroundForLocation(query[0], query[1], mContext);
            String zip = Wunderground.parseLocationFromWundergroundJSON(jsonResult);

            return zip;

        }
        catch(Exception e){
            Log.e(TAG, "error:" + e.getMessage());
            return null;
        }

    }

    @Override
    protected void onPostExecute(String zip){
        super.onPostExecute(zip);

        if(mCompletionListener != null){
            if(zip != null){
                mCompletionListener.WundergroundLocationFound(zip);
            }
            else{
                mCompletionListener.WundergroundLocationNotFound();
            }
        }

    }


}
