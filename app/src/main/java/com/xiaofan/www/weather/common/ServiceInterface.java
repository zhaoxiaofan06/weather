package com.xiaofan.www.weather.common;

import com.xiaofan.www.weather.model.Province;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by think on 2017/11/5.
 */

public interface ServiceInterface {
    @GET("china/")
    Call<ArrayList<Province>> getProvinceList();
}

