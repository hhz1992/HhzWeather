package com.gwu.huanzhou.hhzweather.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.gwu.huanzhou.hhzweather.utils.Wunderground;

/**
 * Created by Huanzhou on 2015/10/19.
 */
public class WundergroundSearchAsyncTask  extends AsyncTask<String,Integer,String> {


    private static final String TAG = "WundergroundSearchAsyncTask";
    private Context mContext;
    private WundergroundCompletionListener mCompletionListener;


    public interface WundergroundCompletionListener{
        public void WundergroundFound(String zip);
        public void WundergroundNotFound();
    }

    public WundergroundSearchAsyncTask(Context context, WundergroundCompletionListener completionListener){
        mContext = context;
        mCompletionListener = completionListener;
    }


    @Override
    protected String doInBackground(String... query) {

        try{
            JsonObject jsonResult = Wunderground.queryWundergroundForLocation(query[0], query[1], mContext);
            String zip = Wunderground.parseLocationFromWundergroundJSON(jsonResult);

            //System.out.println("zip: "+zip);

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

        System.out.println("test!!!!!!!");
        System.out.println(zip);

        if(mCompletionListener != null){
            if(zip != null){
                mCompletionListener.WundergroundFound(zip);
            }
            else{
                mCompletionListener.WundergroundNotFound();
            }
        }

    }


}
