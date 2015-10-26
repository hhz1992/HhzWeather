package com.gwu.huanzhou.hhzweather.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.gwu.huanzhou.hhzweather.model.Forecast;
import com.gwu.huanzhou.hhzweather.utils.Wunderground;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huanzhou on 2015/10/25.
 */
public class ForeCastSearchAsyncTask extends AsyncTask<String,Integer, List<Forecast>> {


    private static final String TAG = "ForeCastSearchAsyncTask";
    private Context mContext;
    private ForecastSearchCompletionListener mCompletionListener;

    public ForeCastSearchAsyncTask(Context context, ForecastSearchCompletionListener completionListener){
        mContext = context;
        mCompletionListener = completionListener;
    }


    public interface ForecastSearchCompletionListener{
        public void WundergroundForecastFound(List<Forecast> forecasts);
        public void WundergroundForecastNotFound();
    }


    @Override
    protected List<Forecast> doInBackground(String... query) {
        try{

            ArrayList<Forecast> forecasts = new ArrayList<Forecast>();

            JsonObject jsonResult = Wunderground.queryWundergroundForForecast(query[0], query[1], mContext);
            Wunderground.parseForecastFromWundergroundJSON(jsonResult, forecasts);

            return forecasts;

        }
        catch(Exception e){
            Log.e(TAG, "error:" + e.getMessage());
            return null;
        }
    }


    @Override
    protected void onPostExecute( List<Forecast> forecasts){
        super.onPostExecute(forecasts);

        if(mCompletionListener != null){
            if(forecasts != null){
                mCompletionListener.WundergroundForecastFound(forecasts);
            }
            else{
                mCompletionListener.WundergroundForecastNotFound();
            }
        }

    }
}
