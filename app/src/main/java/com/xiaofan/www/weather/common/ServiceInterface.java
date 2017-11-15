package com.xiaofan.www.weather.common;

import com.xiaofan.www.weather.model.City;
import com.xiaofan.www.weather.model.County;
import com.xiaofan.www.weather.model.Province;
import com.xiaofan.www.weather.model.Weather;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by think on 2017/11/5.
 */

public interface ServiceInterface {
    @GET("china/")
    Call<ArrayList<Province>> getProvinceList();

    @GET("china/{id}")
    Call<ArrayList<City>> getCityList(@Path("id") String id);

    @GET("china/{pid}/{id}")
    Call<ArrayList<County>> getCountyList(@Path("pid") String pid,@Path("id") String id);

    @GET("s6/weather/forecast")
    Call<Weather> getWeatherList(@Query("location") String location,@Query("key") String key);
}

