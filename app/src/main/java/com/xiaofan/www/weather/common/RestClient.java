package com.xiaofan.www.weather.common;

import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiaofan.www.weather.model.City;
import com.xiaofan.www.weather.model.County;
import com.xiaofan.www.weather.model.Province;
import com.xiaofan.www.weather.model.Weather;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by think on 2017/10/14.
 */

public class RestClient {
    private static String api_url;
    private static RestClient restClient=null;
    private Retrofit retrofit=null;
    private ServiceInterface serviceInterface=null;

    private RestClient(String api_url){
        this.api_url=api_url;
    }

    public static RestClient getDedault(String api_url){
        if(restClient==null){
            synchronized (RestClient.class){
                restClient=new RestClient(api_url);
                restClient.initRetrofit(api_url);
            }
        }
        return restClient;
    }

    public void setDefault(String api_url){
        this.api_url=api_url;
        Gson gson = new GsonBuilder()
                //配置你的Gson
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        retrofit=new Retrofit
                .Builder()
                .baseUrl(api_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        serviceInterface=retrofit.create(ServiceInterface.class);
    }

    private void initRetrofit(String api_url){
        if(retrofit==null){
            setDefault(api_url);
        }
    }

    public String getApiUrl(){
        return api_url;
    }

    public Call<ArrayList<Province>> getProvinceList(){
        return serviceInterface.getProvinceList();
    }

    public Call<ArrayList<City>> getCityList(String id){
        return serviceInterface.getCityList(id);
    }

    public Call<ArrayList<County>> getCountyList(String pid,String id){
        return serviceInterface.getCountyList(pid,id);
    }

    public Call<Weather> getWeatherList(String location,String key){
        return serviceInterface.getWeatherList(location,key);
    }

}
