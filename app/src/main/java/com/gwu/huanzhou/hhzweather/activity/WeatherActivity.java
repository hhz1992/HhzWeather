package com.gwu.huanzhou.hhzweather.activity;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gwu.huanzhou.hhzweather.R;
import com.gwu.huanzhou.hhzweather.asynctask.BingImageSearchAsyncTask;
import com.gwu.huanzhou.hhzweather.asynctask.ConditionSearchAsyncTask;
import com.gwu.huanzhou.hhzweather.asynctask.LocationSearchAsyncTask;
import com.gwu.huanzhou.hhzweather.model.Condition;
import com.gwu.huanzhou.hhzweather.sensor.LocationFinder;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.net.URL;

public class WeatherActivity extends AppCompatActivity implements LocationFinder.LocationDetector, LocationSearchAsyncTask.LocationSearchCompletionListener, ConditionSearchAsyncTask.ConditionSearchCompletionListener,BingImageSearchAsyncTask.ImageSearchCompletionListener {

    private final String TAG = "WeatherActivity";

    private ImageView mImageView;
    private ImageView mBackgroundView;

    private TextView mTextViewTempF;
    private TextView mTextViewWeather;
    private TextView mTextViewRelativeHumidity;
    private LinearLayout mLinearLayoutRound;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        mImageView = (ImageView) findViewById(R.id.image);
        mTextViewTempF = (TextView) findViewById(R.id.temp_f);
        mTextViewWeather = (TextView) findViewById(R.id.weather);
        mTextViewRelativeHumidity = (TextView) findViewById(R.id.relative_humidity);
        mLinearLayoutRound = (LinearLayout) findViewById(R.id.round);
        mBackgroundView = (ImageView) findViewById(R.id.background);



        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE);

        mLinearLayoutRound.startAnimation(animation);




        mLinearLayoutRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = getApplicationContext();
                CharSequence text = "Hello toast!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                v.clearAnimation();

            }
        });



        mLinearLayoutRound.setOnTouchListener(new View.OnTouchListener() {
            float dX, dY;

            boolean flag = false;
            float  roundY ;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getActionMasked()) {

                    case MotionEvent.ACTION_DOWN:

                        if(flag == false){
                            flag = true;
                            roundY = v.getY();

                        }
                        dY = v.getY() - event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:

                        v.animate()
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;
                    case MotionEvent.ACTION_UP:

                        v.animate()
                                .y(roundY)
                                .setDuration(1000)
                                .start();


//                        System.out.println(roundY);
//                        System.out.println(v.getY());
//
//                        float translateY = roundY- v.getY();
//
//
//                        System.out.println(translateY);
//
//                        TranslateAnimation moveLefttoRight = new TranslateAnimation(0, 0, 0, 594);
//                        moveLefttoRight.setDuration(1000);
//                        moveLefttoRight.setFillAfter(true);
//
//                        v.startAnimation(moveLefttoRight);
//                        break;
                        //moveViewToScreenCenter(v);
                        break;


                    default:

                        return false;
                }
                return true;


            }
        });




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


        LocationFinder locationFinder = new LocationFinder(this, this);
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

        ConditionSearchAsyncTask conditionTask = new ConditionSearchAsyncTask(this, this);
       // LocationSearchAsyncTask locationTask = new LocationSearchAsyncTask(this, this);

        conditionTask.execute(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        //locationTask.execute(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

        Log.d(TAG, "location found");
    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason reason) {
        //TODO: handle location failure
        Log.d(TAG, "location not found");
    }

    @Override
    public void WundergroundLocationFound(String zip) {
        Log.d(TAG, "Wunderground Found " + zip);

        //ConditionSearchAsyncTask task = new ConditionSearchAsyncTask(this, this);
        //task.execute(zip);


    }

    @Override
    public void WundergroundLocationNotFound() {

        Log.d(TAG, "Wunderground NOT Found");
    }

    @Override
    public void WundergroundConditionFound(Condition condition) {

        System.out.println(condition.getmWeather());

        mTextViewTempF.setText(condition.getmTemperatureF());
        mTextViewWeather.setText(condition.getmWeather());
        mTextViewRelativeHumidity.setText(condition.getmRelativeHumidity());

        BingImageSearchAsyncTask task = new BingImageSearchAsyncTask(this, this);

        System.out.println(condition.getmDisplaylocation().getmCity());
        task.execute(condition.getmDisplaylocation().getmCity());


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


    @Override
    public void imageUrlFound(URL url) {
        Ion.with(mBackgroundView).load(url.toString()).setCallback(new FutureCallback<ImageView>() {
            @Override
            public void onCompleted(Exception e, ImageView result) {
                if(e == null){
                    //yay

                }
                else{
                    Log.d(TAG, "image failed to load");

                }
            }
        });
    }

    @Override
    public void imageUrlNotFound() {
        Log.d(TAG, "image url not found");

    }

}
