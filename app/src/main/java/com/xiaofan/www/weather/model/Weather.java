package com.xiaofan.www.weather.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by think on 2017/11/7.
 */

public class Weather {
    @SerializedName("HeWeather6")
    private ArrayList<HeWeather> data;

    public void setData(ArrayList<HeWeather> data){
        this.data=data;
    }

    public ArrayList<HeWeather> getData(){
        return data;
    }

    public class HeWeather{
        @SerializedName("basic")
        public CityInfo cityInfo;

        @SerializedName("update")
        public UpdateTime updateTime;

        public String status;

        public ArrayList<ForeCast> daily_forecast;

        public class CityInfo {
            public String cid;
            public String location;
            public String parent_city;
            public String admin_area;
            public String cnty;
            public String lat;
            public String lon;
            public String tz;
        }

        public class UpdateTime {
            public String loc;
            public String utc;
        }

        public class ForeCast{
            public String cond_code_d;
            public String cond_code_n;
            public String cond_txt_d;
            public String cond_txt_n;
            public String date;
            public String hum;
            public String mr;
            public String ms;
            public String pcpn;
            public String pop;
            public String pres;
            public String sr;
            public String ss;
            public String tmp_max;
            public String tmp_min;
            public String uv_index;
            public String vis;
            public String wind_deg;
            public String wind_dir;
            public String wind_sc;
            public String wind_spd;
        }
    }
}
