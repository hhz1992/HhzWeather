package com.gwu.huanzhou.hhzweather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwu.huanzhou.hhzweather.PersistanceManager;
import com.gwu.huanzhou.hhzweather.R;
import com.gwu.huanzhou.hhzweather.model.Condition;
import com.gwu.huanzhou.hhzweather.model.Displaylocation;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class DetailedActivity extends AppCompatActivity {

    private final String TAG = "DetailedActivity";

    private TextView mDetailedTemp;
    private ImageView mDetailedImage;

    private TextView mDetailedHumidity;
    private TextView mDetailedDewpoint;
    private TextView mDetailedVisibility;

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

        mPersistanceManager.getCurrentCondition(condition);
        mDetailedTemp.setText(condition.getmTemperatureF());
        mDetailedHumidity.setText(condition.getmRelativeHumidity());
        mDetailedDewpoint.setText(condition.getmDewpointF());
        mDetailedVisibility.setText(condition.getmVisibilityMi());

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


    }

}
