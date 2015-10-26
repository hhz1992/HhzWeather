package com.gwu.huanzhou.hhzweather.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwu.huanzhou.hhzweather.Constants;
import com.gwu.huanzhou.hhzweather.R;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Huanzhou on 2015/10/25.
 * To set the viewlist in detailed activity, see comments in DetailedActivity.class
 */
public class ListViewAdapters extends BaseAdapter {

    private final String TAG = "ListViewAdapters";

    public ArrayList<HashMap<String, String>> list;

    Activity activity;
    TextView mTextViewForecastDay;
    ImageView mImageViewForcastImage;
    TextView mTextViewForecastHighTemp;
    TextView mTextViewForecastLowTemp;
    TextView mTextViewForecastHumidity;

    public ListViewAdapters(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.listview_row, null);

            mTextViewForecastDay = (TextView) convertView.findViewById(R.id.forcast_day);
            mImageViewForcastImage = (ImageView) convertView.findViewById(R.id.forcast_image);
            mTextViewForecastHighTemp = (TextView) convertView.findViewById(R.id.forcast_hightemp);
            mTextViewForecastLowTemp = (TextView) convertView.findViewById(R.id.forcast_lowtemp);
            mTextViewForecastHumidity = (TextView) convertView.findViewById(R.id.forcast_humidity);
        }

        HashMap<String, String> map = list.get(position);
        mTextViewForecastDay.setText(map.get(Constants.FORECAST_DAY));
        mTextViewForecastHighTemp.setText(map.get(Constants.FORECAST_HIGHTEMP));
        mTextViewForecastLowTemp.setText(map.get(Constants.FORECAST_LOWTEMP));
        mTextViewForecastHumidity.setText(map.get(Constants.FORECAST_HUMIDITY));

        Ion.with(mImageViewForcastImage).load(map.get(Constants.FORECAST_IMAGE)).setCallback(new FutureCallback<ImageView>() {
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

        return convertView;
    }
}
