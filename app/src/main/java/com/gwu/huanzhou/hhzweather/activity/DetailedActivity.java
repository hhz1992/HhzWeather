package com.gwu.huanzhou.hhzweather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class DetailedActivity extends AppCompatActivity  implements ForeCastSearchAsyncTask.ForecastSearchCompletionListener{

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

        mDetailedTemp =  (TextView)findViewById(R.id.detailed_temp);
        mDetailedImage = (ImageView)findViewById(R.id.detailed_image);

        mDetailedPressure = (TextView)findViewById(R.id.detailed_pressure);
        mDetailedWindmph = (TextView)findViewById(R.id.detailed_windmph);
        mDetailedWinddir = (TextView)findViewById(R.id.detailed_winddir);

        mPersistanceManager.getCurrentCondition(condition);
        mDetailedTemp.setText(condition.getmTemperatureF());
        mDetailedHumidity.setText(condition.getmRelativeHumidity());
        mDetailedDewpoint.setText(condition.getmDewpointF());
        mDetailedVisibility.setText(condition.getmVisibilityMi());
        mDetailedPressure.setText(condition.getmPressureIn());
        mDetailedWindmph.setText(condition.getmWindMph());
        mDetailedWinddir.setText(condition.getmWindDir());

        Ion.with(mDetailedImage).load(condition.getmIconUrl()).setCallback(new FutureCallback<ImageView>() {
            @Override
            public void onCompleted(Exception e, ImageView result) {
                if (e == null) {
                    //yay
                    System.out.println("image success");

                } else {
                    //TODO: handle image loading fail
                    Log.d(TAG, "image failed to load");
                    //skipQuestionDueToError();
                }
            }
        });

        ForeCastSearchAsyncTask forecastTask = new ForeCastSearchAsyncTask(this, this);
        forecastTask.execute(condition.getmDisplaylocation().getmState(), condition.getmDisplaylocation().getmCity());



    }

    @Override
    public void WundergroundForecastFound(List<Forecast> forecasts) {


        ArrayList list=new ArrayList<HashMap<String,String>>();
        

        for(int i=0; i<forecasts.size();i++){
            HashMap<String,String> temp=new HashMap<String, String>();
            temp.put(Constants.FORECAST_DAY, forecasts.get(i).getmWeekday());
            temp.put(Constants.FORECAST_IMAGE, forecasts.get(i).getmIconUrl());
            temp.put(Constants.FORECAST_HIGHTEMP, forecasts.get(i).getmHighTempF());
            temp.put(Constants.FORECAST_LOWTEMP, forecasts.get(i).getmLowTempF());
            temp.put(Constants.FORECAST_HUMIDITY,forecasts.get(i).getMmAveHumidity());
            list.add(temp);
        }


        mdDetailedForcast = (ListView)findViewById(R.id.forecast_list);
        ListViewAdapters adapter=new ListViewAdapters(this, list);
        mdDetailedForcast.setAdapter(adapter);


    }

    @Override
    public void WundergroundForecastNotFound() {

    }
}
