package com.gwu.huanzhou.hhzweather.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.gwu.huanzhou.hhzweather.model.Condition;
import com.gwu.huanzhou.hhzweather.utils.Wunderground;


/**
 * Created by Huanzhou on 2015/10/23.
 */
public class ConditionSearchAsyncTask extends AsyncTask<String,Integer,Condition> {


    private static final String TAG = "ConditionSearchAsyncTask";
    private Context mContext;
    private ConditionSearchCompletionListener mCompletionListener;

    public interface ConditionSearchCompletionListener{
        public void WundergroundConditionFound(Condition condition);
        public void WundergroundConditionNotFound();
    }

    public ConditionSearchAsyncTask(Context context, ConditionSearchCompletionListener completionListener){
        mContext = context;
        mCompletionListener = completionListener;
    }


    @Override
    protected Condition doInBackground(String... query) {

        Condition condition = new Condition();

        try{
            JsonObject jsonResult = Wunderground.queryWundergroundForCondition(query[0], mContext);

            Wunderground.parseConditionFromWundergroundJSON(jsonResult,condition);

            return condition;

        }
        catch(Exception e){
            Log.e(TAG, "error:" + e.getMessage());
            return null;
        }

    }


    @Override
    protected void onPostExecute(Condition condition){
        super.onPostExecute(condition);

        if(mCompletionListener != null){
            if(condition != null){
                mCompletionListener.WundergroundConditionFound(condition);
            }
            else{
                mCompletionListener.WundergroundConditionNotFound();
            }
        }

    }
}
