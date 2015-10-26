package com.gwu.huanzhou.hhzweather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gwu.huanzhou.hhzweather.Constants;
import com.gwu.huanzhou.hhzweather.PersistanceManager;
import com.gwu.huanzhou.hhzweather.R;
import com.gwu.huanzhou.hhzweather.adapter.ListViewAdapters;
import com.gwu.huanzhou.hhzweather.asynctask.ForeCastSearchAsyncTask;
import com.gwu.huanzhou.hhzweather.model.Condition;
import com.gwu.huanzhou.hhzweather.model.Displaylocation;
import com.gwu.huanzhou.hhzweather.model.Forecast;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Huanzhou on 2015/10/21.
 *
 * DetailedActivity is to present detailed weather information for a specific location and for current time. It contains forecast weather for at most next
 * four days as well.
 */
public class DetailedActivity extends AppCompatActivity implements ForeCastSearchAsyncTask.ForecastSearchCompletionListener {

    private final String TAG = "DetailedActivity";

    private TextView mDetailedTemp;
    private ImageView mDetailedImage;
    private TextView mDetailedHumidity;
    private TextView mDetailedDewpoint;
    private TextView mDetailedVisibility;
    private TextView mDetailedPressure;
    private TextView mDetailedWindmph;
    private TextView mDetailedWinddir;
    private ListView mdDetailedForcast;

    private int mDayDisplay = 4;                //set default number of day to display in forecast, will be change in setting activity.

    private PersistanceManager mPersistanceManager;
    Condition condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        mPersistanceManager = new PersistanceManager(this);
        condition = new Condition();
        condition.setmDisplaylocation(new Displaylocation());

        mDetailedHumidity = (TextView) findViewById(R.id.detailed_humidity);
        mDetailedDewpoint = (TextView) findViewById(R.id.detailed_dewpoint);
        mDetailedVisibility = (TextView) findViewById(R.id.detailed_visibility);
        mDetailedTemp = (TextView) findViewById(R.id.detailed_temp);
        mDetailedImage = (ImageView) findViewById(R.id.detailed_image);
        mDetailedPressure = (TextView) findViewById(R.id.detailed_pressure);
        mDetailedWindmph = (TextView) findViewById(R.id.detailed_windmph);
        mDetailedWinddir = (TextView) findViewById(R.id.detailed_winddir);


        //use PersistanceManager to get current condition
        mPersistanceManager.getCurrentCondition(condition);

        mDetailedTemp.setText(condition.getTemperature());
        mDetailedHumidity.setText(condition.getmRelativeHumidity());
        mDetailedDewpoint.setText(condition.getDewpoint());
        mDetailedVisibility.setText(condition.getmVisibilityMi());
        mDetailedPressure.setText(condition.getmPressureIn());
        mDetailedWindmph.setText(condition.getmWindMph());
        mDetailedWinddir.setText(condition.getmWindDir());

        //load weather icon in condition
        Ion.with(mDetailedImage).load(condition.getmIconUrl()).setCallback(new FutureCallback<ImageView>() {
            @Override
            public void onCompleted(Exception e, ImageView result) {
                if (e == null) {

                } else {
                    //log the error information including the URL, helping to check. Then keep running app without weather icon
                    Log.d(TAG, "image failed to load, URL: "+ condition.getmIconUrl());
                }
            }
        });

        //get forecast using current state and city
        ForeCastSearchAsyncTask forecastTask = new ForeCastSearchAsyncTask(this, this);
        forecastTask.execute(condition.getmDisplaylocation().getmState(), condition.getmDisplaylocation().getmCity());


    }


    @Override
    public void WundergroundForecastFound(List<Forecast> forecasts) {

        //here using HashMap<String, String> and ListViewAdapters to show the forecast array got from weather api
        ArrayList list = new ArrayList<HashMap<String, String>>();
        setmDayDisplay();

        int displayNum = forecasts.size();
        if (forecasts.size() >= mDayDisplay) {
            displayNum = mDayDisplay;
        }

        for (int i = 0; i < displayNum; i++) {
            HashMap<String, String> temp = new HashMap<String, String>();

            //to check the the style of temperature display in PersistanceManager (be set in setting activity)
            if (mPersistanceManager.getCurrentTempDisplay() != null && mPersistanceManager.getCurrentTempDisplay() != "") {
                forecasts.get(i).setTEMPDISPLAY(mPersistanceManager.getCurrentTempDisplay());
            }

            temp.put(Constants.FORECAST_DAY, forecasts.get(i).getmWeekday());
            temp.put(Constants.FORECAST_IMAGE, forecasts.get(i).getmIconUrl());
            temp.put(Constants.FORECAST_HIGHTEMP, forecasts.get(i).getHighTemp());
            temp.put(Constants.FORECAST_LOWTEMP, forecasts.get(i).getLowTemp());
            temp.put(Constants.FORECAST_HUMIDITY, forecasts.get(i).getMmAveHumidity());
            list.add(temp);
        }

        mdDetailedForcast = (ListView) findViewById(R.id.forecast_list);
        ListViewAdapters adapter = new ListViewAdapters(this, list);
        mdDetailedForcast.setAdapter(adapter);

    }

    @Override
    public void WundergroundForecastNotFound() {

        //show a toast to notify user
        Log.d(TAG, "forecast failed to get");
        Toast.makeText(this,R.string.NOTIFICATION_FAILTOGETWEATHER,Toast.LENGTH_LONG).show();
    }

    //set the style of temperature display in PersistanceManager
    public void setmDayDisplay() {

        String dayDisplay = mPersistanceManager.getCurrentDayDisplay();

        if (dayDisplay.equals(Constants.One)) {
            mDayDisplay = 1;
        } else if (dayDisplay.equals(Constants.Two)) {
            mDayDisplay = 2;
        } else if (dayDisplay.equals(Constants.Three)) {
            mDayDisplay = 3;
        } else if (dayDisplay.equals(Constants.Four)) {
            mDayDisplay = 4;
        }
    }


}
