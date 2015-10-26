package com.gwu.huanzhou.hhzweather.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gwu.huanzhou.hhzweather.PersistanceManager;
import com.gwu.huanzhou.hhzweather.R;
import com.gwu.huanzhou.hhzweather.asynctask.BingImageSearchAsyncTask;
import com.gwu.huanzhou.hhzweather.asynctask.ConditionSearchAsyncTask;
import com.gwu.huanzhou.hhzweather.model.Condition;
import com.gwu.huanzhou.hhzweather.sensor.LocationFinder;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.net.URL;

/**
 * Created by Huanzhou on 2015/09/30.
 * <p/>
 * WeatherActivity is main activity in this app which gets current location of mobile and use location to send weather api calls. Then get json result and show
 * the weather information. It also provides searching weather condition by zipcode. The background image will change according to the city which got by current location
 * or zipcode. You can drag up or drag down the center circle to refresh the weather condition, which make whole process run again.
 */
public class WeatherActivity extends AppCompatActivity implements LocationFinder.LocationDetector, ConditionSearchAsyncTask.ConditionSearchCompletionListener, BingImageSearchAsyncTask.ImageSearchCompletionListener {

    private final String TAG = "WeatherActivity";
    private static final int LOCATION_ACCESS_REQUEST_CODE = 1;

    private ImageView mImageView;
    private ImageView mBackgroundView;
    private TextView mTextViewTemp;
    private TextView mTextViewWeather;
    private TextView mTextViewRelativeHumidity;
    private TextView mTextViewLocation;
    private LinearLayout mLinearLayoutRound;
    private TextView mTextViewNotification;
    private EditText mEditTextZipcode;

    private PersistanceManager mPersistanceManager;

    Activity activity;
    ConditionSearchAsyncTask.ConditionSearchCompletionListener mConditionSearchCompletionListener = this;
    LocationFinder.LocationDetector mLocationDetector = this;
    Condition condition;
    private boolean conditionFoundFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        activity = this;

        mPersistanceManager = new PersistanceManager(this);

        mImageView = (ImageView) findViewById(R.id.image);
        mTextViewTemp = (TextView) findViewById(R.id.temp);
        mTextViewWeather = (TextView) findViewById(R.id.weather);
        mTextViewRelativeHumidity = (TextView) findViewById(R.id.relative_humidity);
        mLinearLayoutRound = (LinearLayout) findViewById(R.id.round);
        mBackgroundView = (ImageView) findViewById(R.id.background);
        mTextViewLocation = (TextView) findViewById(R.id.location);
        mTextViewNotification = (TextView) findViewById(R.id.notification);


        //when get the location and weather, it will make the center circle flash
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE);

        mLinearLayoutRound.startAnimation(animation);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //show dialog
            new AlertDialog.Builder(this)
                    .setTitle(R.string.location_permission_title)
                    .setMessage(R.string.location_permission_message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //prompt user with system dialog for location permission upon user clicking okay dialog button
                            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_ACCESS_REQUEST_CODE);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();
        }

        //to make the center circle drag-able, which will detect the current coordinate and moved coordinate, and make animation
        mLinearLayoutRound.setOnTouchListener(new View.OnTouchListener() {
            float dY, y1, y2;

            boolean flag = false;
            float roundY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getActionMasked()) {

                    case MotionEvent.ACTION_DOWN:
                        if (flag == false) {
                            flag = true;
                            roundY = v.getY();

                        }
                        dY = v.getY() - event.getRawY();
                        y1 = v.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        v.animate()
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;
                    case MotionEvent.ACTION_UP:
                        y2 = v.getY();
                        if (Math.abs(y1 - y2) < 5) {

                            if (conditionFoundFlag) {
                                Intent intent = new Intent(getBaseContext(), DetailedActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            v.animate()
                                    .y(roundY)
                                    .setDuration(1000)
                                    .start();
                            mLinearLayoutRound.startAnimation(animation);
                            Toast.makeText(getApplicationContext(), R.string.NOTIFICATION_GETTINGWEATHER, Toast.LENGTH_LONG).show();
                            //start a new finding location
                            new LocationFinder(getApplicationContext(), mLocationDetector).detectLocation();
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        LocationFinder locationFinder = new LocationFinder(this, this);
        locationFinder.detectLocation();
        mEditTextZipcode = (EditText) findViewById(R.id.zipcode);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                //when enter a five digital zipcode, then start searching weather by this zipcode
                if (mEditTextZipcode.getText().length() == 5) {
                    Toast.makeText(getApplicationContext(), R.string.NOTIFICATION_GETTINGWEATHER, Toast.LENGTH_LONG).show();
                    new ConditionSearchAsyncTask(getApplicationContext(), mConditionSearchCompletionListener).execute(mEditTextZipcode.getText().toString());
                } else {
                }
            }
        };

        mEditTextZipcode.addTextChangedListener(textWatcher);

    }

    // when onResume, load setting from PersistanceManager to ensure the setting make effect
    @Override
    protected void onResume() {
        super.onResume();

        if (condition != null) {
            if (mPersistanceManager.getCurrentTempDisplay() != null && mPersistanceManager.getCurrentTempDisplay() != "") {
                condition.setTEMPDISPLAY(mPersistanceManager.getCurrentTempDisplay());
                mTextViewTemp.setText(condition.getTemperature());
            }
        }
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
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d(TAG, "settings button pressed");
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void locationFound(Location location) {
        //TODO: handle location success

        //after finding a location, use Latitude and Longitude to get weather condition.
        ConditionSearchAsyncTask conditionTask = new ConditionSearchAsyncTask(this, this);
        conditionTask.execute(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

        Log.d(TAG, "location found");
    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason reason) {

        //To infor user to check the GPS or use searching weather by zipcode
        Toast.makeText(getApplicationContext(), R.string.NOTIFICATION_FAILTOGETLOCATION, Toast.LENGTH_LONG).show();
        Log.d(TAG, "location not found");
    }


    @Override
    public void WundergroundConditionFound(Condition condition) {

        //save the condition to PersistanceManager
        this.condition = condition;
        mPersistanceManager.saveConditionLocally(condition);

        //set the display mode
        if (mPersistanceManager.getCurrentTempDisplay() != null && mPersistanceManager.getCurrentTempDisplay() != "") {
            condition.setTEMPDISPLAY(mPersistanceManager.getCurrentTempDisplay());
        }

        //show the main layout of center circle
        mLinearLayoutRound.clearAnimation();
        mTextViewNotification.setVisibility(View.GONE);

        mTextViewTemp.setVisibility(View.VISIBLE);
        mTextViewWeather.setVisibility(View.VISIBLE);
        mTextViewRelativeHumidity.setVisibility(View.VISIBLE);
        mTextViewLocation.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.VISIBLE);

        mTextViewTemp.setText(condition.getTemperature());
        mTextViewWeather.setText(condition.getmWeather());
        mTextViewRelativeHumidity.setText(condition.getmRelativeHumidity());
        mTextViewLocation.setText(condition.getmDisplaylocation().getmCity());

        conditionFoundFlag = true;

        //In order to show location information in inner class
        final Condition conditionFinal = condition;

        //make a popup windows and show the detailed location information of a city, including Country name, State name and City name.
        //Once user click the city title, the windows will pop up
        mTextViewLocation.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Rect locationOFLocationView = locateView(mTextViewLocation);

                LayoutInflater layoutInflater
                        = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup_locationinfo, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
//

                Button btnDismiss = (Button) popupView.findViewById(R.id.locationinfo_confirm);
                btnDismiss.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }
                });

                TextView mTextViewLocationInfoCity = (TextView) popupView.findViewById(R.id.locationinfo_city);
                TextView mTextViewLocationInfoState = (TextView) popupView.findViewById(R.id.locationinfo_state);
                TextView mTextViewLocationInfoCountry = (TextView) popupView.findViewById(R.id.locationinfo_country);

                mTextViewLocationInfoCity.setText(conditionFinal.getmDisplaylocation().getmCity());
                mTextViewLocationInfoState.setText(conditionFinal.getmDisplaylocation().getmState());
                mTextViewLocationInfoCountry.setText(conditionFinal.getmDisplaylocation().getmCountry());

                popupWindow.showAtLocation(mTextViewLocation, Gravity.TOP | Gravity.LEFT, locationOFLocationView.left, locationOFLocationView.bottom);

            }
        });


        BingImageSearchAsyncTask task = new BingImageSearchAsyncTask(this, this);

        System.out.println(condition.getmDisplaylocation().getmCity());
        task.execute(condition.getmDisplaylocation().getmCity());

        Ion.with(mImageView).load(condition.getmIconUrl()).setCallback(new FutureCallback<ImageView>() {
            @Override
            public void onCompleted(Exception e, ImageView result) {
                if (e == null) {
                    //yay

                } else {
                    //log the error information , helping to check. Then keep running app without weather icon
                    Log.d(TAG, "image failed to load " + e.toString());

                }
            }
        });


    }

    @Override
    public void WundergroundConditionNotFound() {

        //Nofity user to check internet or GPS, or change a zipcode or location
        Toast.makeText(getApplicationContext(), R.string.NOTIFICATION_FAILTOGETWEATHER, Toast.LENGTH_LONG).show();
        Log.d(TAG, "Condition failed to get ");

    }


    @Override
    public void imageUrlFound(URL url) {
        Ion.with(mBackgroundView).load(url.toString()).setCallback(new FutureCallback<ImageView>() {
            @Override
            public void onCompleted(Exception e, ImageView result) {
                if (e == null) {

                } else {
                    //log the error information  Then keep running app without weather icon
                    Log.d(TAG, "image failed to load " + e.toString());
                }
            }
        });
    }

    @Override
    public void imageUrlNotFound() {
        //log the error information  Then keep running app without weather icon
        Log.d(TAG, "image url not found");

    }

    // To location a view. Using in popup windows.
    public Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }


}
