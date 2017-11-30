package com.xiaofan.www.weather.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.gson.Gson;
import com.xiaofan.www.weather.MainActivity;
import com.xiaofan.www.weather.R;
import com.xiaofan.www.weather.common.Common;
import com.xiaofan.www.weather.common.RestClient;
import com.xiaofan.www.weather.model.Province;
import com.xiaofan.www.weather.model.Weather;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {
    private MainActivity mainActivity;
    private String weather_api_url;
    private String weather_api_key;
    private View view;
    private String city_id;
    private String city_name;
    private String weather_id;
    private TableLayout tableLayout;
    private ViewPager viewPager;
    private View view1,view2,view3;
    private ArrayList<View> viewList;

    public WeatherFragment() {
        // Required empty public constructor
    }

    public void setWeatherId(String weather_id) {this.weather_id=weather_id;}

    public void setCityId(String city_id){
        this.city_id=city_id;
    }

    public void setCityName(String city_name){
        this.city_name=city_name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.weather_detail, container, false);

        mainActivity=(MainActivity)getActivity();
        mainActivity.toolbar.setTitle("");
        mainActivity.toolbar.setTitleTextColor(Color.WHITE);
        mainActivity.toolbar.setNavigationIcon(R.drawable.back_write_32);
        mainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });
        mainActivity.title.setText(city_name);
        mainActivity.menu.findItem(R.id.more).setVisible(false);

        flushData();
        //Log.d("Weather",city_name);
        return view;
    }

    public void flushData(){
        weather_api_url=mainActivity.getResources().getString(R.string.weather_api_url);
        weather_api_key=mainActivity.getResources().getString(R.string.weather_api_key);
        RestClient client= RestClient.getDedault(weather_api_url);
        if(!client.getApiUrl().equals(weather_api_url)){
            client.setDefault(weather_api_url);
        }
        Call<Weather> call=client.getWeatherList(weather_id,weather_api_key);
        Log.d("County:",call.request().toString());
        call.enqueue(new Callback<Weather>(){
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response){
                try{
                    int code=response.code();
                    switch (code){
                        case 200:
                            Gson gson=new Gson();
                            Log.d("County:",gson.toJson(call.request()));
                            Log.d("County:",response.headers().toString());
                            Log.d("County:",gson.toJson(response.body()));
                            break;
                    }
                }finally {

                }
            }

            @Override
            public void onFailure(Call<Weather> call,Throwable t){
                t.printStackTrace();
            }
        });
    }
}
