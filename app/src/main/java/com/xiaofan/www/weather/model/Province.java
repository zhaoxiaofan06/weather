package com.xiaofan.www.weather.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by think on 2017/11/5.
 */

public class Province {
    @DatabaseField(id=true)
    private int id;
    @DatabaseField
    private String name;
    public int getId(){return id;}
    public String getName(){return name;}
    public void setId(int id){this.id=id;}
    public void setName(String name){this.name=name;}
}
