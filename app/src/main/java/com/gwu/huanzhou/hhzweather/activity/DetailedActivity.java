package com.gwu.huanzhou.hhzweather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.gwu.huanzhou.hhzweather.PersistanceManager;
import com.gwu.huanzhou.hhzweather.R;
import com.gwu.huanzhou.hhzweather.model.Condition;
import com.gwu.huanzhou.hhzweather.model.Displaylocation;

public class DetailedActivity extends AppCompatActivity {


    private TextView mTextViewWeather;
    private TextView mTextViewWind;
    private PersistanceManager mPersistanceManager;

    Condition condition;
    Displaylocation displaylocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        mPersistanceManager = new PersistanceManager(this);
        condition = new Condition();
        condition.setmDisplaylocation(new Displaylocation());

        mTextViewWeather = (TextView) findViewById(R.id.detailed_weather);
        mTextViewWind = (TextView) findViewById(R.id.detailed_wind);

        mPersistanceManager.getCurrentCondition(condition);

        mTextViewWeather.setText(condition.getmWeather());
        mTextViewWind.setText(condition.getmWind());


    }

}
