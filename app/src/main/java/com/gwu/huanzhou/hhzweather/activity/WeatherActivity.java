package com.gwu.huanzhou.hhzweather.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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

import com.gwu.huanzhou.hhzweather.Constants;
import com.gwu.huanzhou.hhzweather.PersistanceManager;
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
    private static final int LOCATION_ACCESS_REQUEST_CODE = 1;

    private ImageView mImageView;
    private ImageView mBackgroundView;

    private TextView mTextViewTemp;
    private TextView mTextViewWeather;
    private TextView mTextViewRelativeHumidity;
    private TextView mTextViewLocation;
    private LinearLayout mLinearLayoutRound;
    private TextView mTextViewNotification;

    private PersistanceManager mPersistanceManager;

    private EditText mEditTextZipcode;
    Activity activity;
    ConditionSearchAsyncTask.ConditionSearchCompletionListener inter = this;



    Condition condition;
    private boolean conditionFoundFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        activity = this;

        mPersistanceManager = new PersistanceManager(this);

        mImageView = (ImageView) findViewById(R.id.image);
        mTextViewTemp = (TextView) findViewById(R.id.temp);
        mTextViewWeather = (TextView) findViewById(R.id.weather);
        mTextViewRelativeHumidity = (TextView) findViewById(R.id.relative_humidity);
        mLinearLayoutRound = (LinearLayout) findViewById(R.id.round);
        mBackgroundView = (ImageView) findViewById(R.id.background);

        mTextViewLocation = (TextView) findViewById(R.id.location);
        mTextViewNotification =  (TextView) findViewById(R.id.notification);

        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE);

        mLinearLayoutRound.startAnimation(animation);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
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

                            System.out.println("click");

                        } else {

                            v.animate()
                                    .y(roundY)
                                    .setDuration(1000)
                                    .start();

                            mLinearLayoutRound.startAnimation(animation);

                            Context context = getApplicationContext();
                            CharSequence text = "Getting Your Weather";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

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
                if(mEditTextZipcode.getText().length()==5){

                    Context context = getApplicationContext();
                    CharSequence text = Constants.NOTIFICATION_GETWEATHER;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    System.out.println(mEditTextZipcode.getText());
                    new ConditionSearchAsyncTask(getApplicationContext(),inter).execute(mEditTextZipcode.getText().toString());

                }
                else{


                }


            }
        };

        mEditTextZipcode.addTextChangedListener(textWatcher);




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
            Log.d(TAG,"settings button pressed");
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }


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

        this.condition = condition;

        mPersistanceManager.saveConditionLocally(condition);

        if(mPersistanceManager.getCurrentTempDisplay()!=null && mPersistanceManager.getCurrentTempDisplay()!=""){
            condition.setTEMPDISPLAY(mPersistanceManager.getCurrentTempDisplay());
        }

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


        final Condition conditionFinal = condition;

        mTextViewLocation.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                Rect  locationOFLocationView = locateView(mTextViewLocation);

                LayoutInflater layoutInflater
                        = (LayoutInflater)getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup_locationinfo, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,true);
//

                Button btnDismiss = (Button)popupView.findViewById(R.id.locationinfo_confirm);
                btnDismiss.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }
                });

                TextView  mTextViewLocationInfoCity =(TextView) popupView.findViewById(R.id.locationinfo_city);
                TextView  mTextViewLocationInfoState=(TextView) popupView.findViewById(R.id.locationinfo_state);
                TextView  mTextViewLocationInfoCountry=(TextView) popupView.findViewById(R.id.locationinfo_country);

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
        System.out.println("not found!");

    }


    @Override
    public void imageUrlFound(URL url) {
        Ion.with(mBackgroundView).load(url.toString()).setCallback(new FutureCallback<ImageView>() {
            @Override
            public void onCompleted(Exception e, ImageView result) {
                if (e == null) {
                    //yay

                } else {
                    Log.d(TAG, "image failed to load");

                }
            }
        });
    }

    @Override
    public void imageUrlNotFound() {
        Log.d(TAG, "image url not found");

    }

    public  Rect locateView(View v)
    {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try
        {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe)
        {
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


//    private void callPopup() {
//




}
