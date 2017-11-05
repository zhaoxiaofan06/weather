package com.xiaofan.www.weather.common;

import android.content.Context;

import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by think on 2017/10/27.
 */

public class DbClient {
    private static DbClient instance;

    public static synchronized DbClient getInstance(Context c) {
        if (instance == null)
            instance = new DbClient(c);

        return instance;
    }

    public DbHelper dbHelper;

    public DbClient(Context c) {
        this.dbHelper = DbHelper.getInstance(c);
    }

}
