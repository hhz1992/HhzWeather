package com.gwu.huanzhou.hhzweather.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwu.huanzhou.hhzweather.R;
import com.gwu.huanzhou.hhzweather.asynctask.ConditionSearchAsyncTask;
import com.gwu.huanzhou.hhzweather.asynctask.LocationSearchAsyncTask;
import com.gwu.huanzhou.hhzweather.model.Condition;
import com.gwu.huanzhou.hhzweather.sensor.LocationFinder;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class WeatherActivity extends AppCompatActivity implements LocationFinder.LocationDetector, LocationSearchAsyncTask.LocationSearchCompletionListener, ConditionSearchAsyncTask.ConditionSearchCompletionListener {

    private final String TAG = "WeatherActivity";

    private ImageView mImageView;

    private TextView mTextViewTempF;
    private TextView mTextViewWeather;
    private TextView mTextViewRelativeHumidity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        mImageView = (ImageView) findViewById(R.id.image);
        mTextViewTempF =(TextView) findViewById(R.id.temp_f);
        mTextViewWeather =(TextView) findViewById(R.id.weather);
        mTextViewRelativeHumidity =(TextView) findViewById(R.id.relative_humidity );



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//
//            }
//        });


        LocationFinder locationFinder = new LocationFinder(this,this);
        locationFinder.detectLocation();





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Log.d(TAG,"settings button pressed");
//            Intent intent = new Intent(this, WeatherActivity.class);
//            startActivity(intent);
//
//            return true;
//        }
//        else if(id == R.id.action_share){
//            Log.d(TAG, "share button pressed");
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void locationFound(Location location) {
        //TODO: handle location success

        System.out.println(location.getLatitude());
        System.out.println(location.getLongitude());

        LocationSearchAsyncTask task = new LocationSearchAsyncTask(this,this);
        task.execute(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

        Log.d(TAG, "location found");
    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason reason) {
        //TODO: handle location failure
        Log.d(TAG,"location not found");
    }

    @Override
    public void WundergroundLocationFound(String zip) {
        Log.d(TAG,"Wunderground Found "+zip);

        ConditionSearchAsyncTask task = new ConditionSearchAsyncTask(this,this);
        task.execute(zip);



    }

    @Override
    public void WundergroundLocationNotFound() {

        Log.d(TAG,"Wunderground NOT Found");
    }

    @Override
    public void WundergroundConditionFound(Condition condition) {

        System.out.println(condition.getmWeather());

        mTextViewTempF.setText(condition.getmTemperatureF());
        mTextViewWeather.setText(condition.getmWeather());
        mTextViewRelativeHumidity.setText(condition.getmRelativeHumidity());

        Ion.with(mImageView).load(condition.getmIconUrl()).setCallback(new FutureCallback<ImageView>() {
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


    }

    @Override
    public void WundergroundConditionNotFound() {

    }
}
