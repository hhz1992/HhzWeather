package com.gwu.huanzhou.hhzweather.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.gwu.huanzhou.hhzweather.utils.Wunderground;

import java.net.URL;

/**
 * Created by Huanzhou on 2015/10/19.
 */
public class WundergroundSearchAsyncTask  extends AsyncTask<String,Integer,URL> {


    private static final String TAG = "WundergroundSearchAsyncTask";
    private Context mContext;
    private WundergroundCompletionListener mCompletionListener;


    public interface WundergroundCompletionListener{
        public void WundergroundFound(URL url);
        public void WundergroundNotFound();
    }

    public WundergroundSearchAsyncTask(Context context, WundergroundCompletionListener completionListener){
        mContext = context;
        mCompletionListener = completionListener;
    }


    @Override
    protected URL doInBackground(String... query) {

        try{
            JsonObject jsonResult = Wunderground.queryWundergroundForLocation("38.846846846846844", "-77.03909252879366", mContext);

            System.out.println("json: "+jsonResult);

            return null;

        }
        catch(Exception e){
            Log.e(TAG, "error:" + e.getMessage());
            return null;
        }

    }

    @Override
    protected void onPostExecute(URL url){
        super.onPostExecute(url);

        System.out.println("test!!!!!!!");
        System.out.println(url);

        if(mCompletionListener != null){
            if(url != null){
                mCompletionListener.WundergroundFound(url);
            }
            else{
                mCompletionListener.WundergroundNotFound();
            }
        }

    }


}
