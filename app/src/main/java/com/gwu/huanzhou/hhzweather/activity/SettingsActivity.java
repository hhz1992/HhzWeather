package com.gwu.huanzhou.hhzweather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.gwu.huanzhou.hhzweather.PersistanceManager;
import com.gwu.huanzhou.hhzweather.R;

/**
 * Created by Huanzhou on 2015/10/26.
 *
 * SettingsActivity is to set the display style of temperature, either be Fahrenheit or Celsius.
 * In addition, it can change the number of forecast days
 */
public class SettingsActivity extends AppCompatActivity {

    private PersistanceManager mPersistanceManager;
    private Spinner mSpinnerTempDisplay;
    private Spinner mSpinnerDayDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mSpinnerTempDisplay = (Spinner) findViewById(R.id.activity_settings_temp_display);
        mSpinnerDayDisplay = (Spinner) findViewById(R.id.activity_settings_day_display);

        mPersistanceManager = new PersistanceManager(this);

        //this can make the selection display consistent with the current sitting
        if (mPersistanceManager.getCurrentTempDisplay() != null && mPersistanceManager.getCurrentTempDisplay() != "") {
            ArrayAdapter myAdapTempDisplay = (ArrayAdapter) mSpinnerTempDisplay.getAdapter(); //cast to an ArrayAdapter
            int spinnerPosition = myAdapTempDisplay.getPosition(mPersistanceManager.getCurrentTempDisplay());
            mSpinnerTempDisplay.setSelection(spinnerPosition);
        }

        if (mPersistanceManager.getCurrentDayDisplay() != null && mPersistanceManager.getCurrentDayDisplay() != "") {
            ArrayAdapter myAdapDayDisplay = (ArrayAdapter) mSpinnerDayDisplay.getAdapter(); //cast to an ArrayAdapter
            int spinnerPosition = myAdapDayDisplay.getPosition(mPersistanceManager.getCurrentDayDisplay());
            mSpinnerDayDisplay.setSelection(spinnerPosition);
        }

        //when the selection get changed, it will save the changes in PersistanceManager and send a toast to user say saved
        mSpinnerTempDisplay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!mPersistanceManager.getCurrentTempDisplay().equals(parent.getItemAtPosition(position).toString())) {
                    mPersistanceManager.saveTempDisplay(parent.getItemAtPosition(position).toString());
                    Toast.makeText(parent.getContext(), R.string.NOTIFICATION_SAVE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerDayDisplay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!mPersistanceManager.getCurrentDayDisplay().equals(parent.getItemAtPosition(position).toString())) {
                    mPersistanceManager.saveDayDisplay(parent.getItemAtPosition(position).toString());
                    Toast.makeText(parent.getContext(),  R.string.NOTIFICATION_SAVE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}
