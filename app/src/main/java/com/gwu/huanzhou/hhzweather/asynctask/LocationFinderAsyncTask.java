package com.gwu.huanzhou.hhzweather.asynctask;

import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;

import java.net.URL;


/**
 * Created by Huanzhou on 2015/10/17.
 */

public class LocationFinderAsyncTask extends AsyncTask<String,Integer,URL> {


    private static final String TAG = "LocationFinderAsyncTask";
    private Context mContext;
    private ImageSearchCompletionListener mCompletionListener;


    public interface ImageSearchCompletionListener{
        public void locationFound();
        public void locationNotFound();
    }

    public LocationFinderAsyncTask(Context context, ImageSearchCompletionListener completionListener){
        mContext = context;
        mCompletionListener = completionListener;
    }

    LocationManager locationmanager;

    @Override
    protected URL doInBackground(String... query) {




        return null;
    }


    @Override
    protected void onPostExecute(URL url){
        super.onPostExecute(url);

        if(mCompletionListener != null){
            if(url != null){
                mCompletionListener.locationFound();
            }
            else{
                mCompletionListener.locationNotFound();
            }
        }

    }


}
