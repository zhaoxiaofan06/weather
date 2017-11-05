package com.xiaofan.www.weather.common;

/**
 * Created by think on 2017/10/14.
 */

public class WeatherEvent extends AbstractEvent {
    public enum Type
    {
        GOTO_WEATHER_PROVINCE,
        GOTO_WEATHER_CITY,
        GOTO_WEATHER_COUNTY
    }

    private int _resultCode;
    private Object _object;

    public WeatherEvent(
            Type type,
            int resultCode,
            Object object
    ) {

        super(type);

        this._resultCode = resultCode;
        this._object = object;
    }

    public int getResultCode()
    {
        return _resultCode;
    }

    public Object getObject()
    {
        return _object;
    }
}
