package com.xiaofan.www.weather.common;

/**
 * Created by think on 2017/10/14.
 */

public abstract class AbstractEvent {
    private Enum _type;

    protected AbstractEvent(Enum type)
    {
        this._type = type;
    }

    public Enum getType()
    {
        return this._type;
    }
}
