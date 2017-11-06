package com.xiaofan.www.weather.model;

/**
 * Created by think on 2017/11/5.
 */

public class County {
    private int id;
    private String name;
    private String weather_id;
    public int getId(){return id;}
    public String getName(){return name;}
    public String getWeatherId(){return weather_id;}
    public void setId(int id){this.id=id;}
    public void setName(String name){this.name=name;}
    public void setWeatherId(String weather_id){this.weather_id=weather_id;}
}
