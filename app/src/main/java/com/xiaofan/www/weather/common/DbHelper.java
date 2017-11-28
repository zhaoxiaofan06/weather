package com.xiaofan.www.weather.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.xiaofan.www.weather.model.Province;

import java.sql.SQLException;

/**
 * Created by think on 2017/10/27.
 */

public class DbHelper extends OrmLiteSqliteOpenHelper{
    public static final String DATABASE_NAME = "weather.db";
    public static final int DATABASE_VERSION = 171128;
    private static DbHelper instance;

    public DbHelper(Context context)	{
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DbHelper getInstance(Context c) {
        if (instance == null) {
            instance = new DbHelper(c);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try{
            TableUtils.createTableIfNotExists(connectionSource, Province.class);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Dao<Province,Integer> provincesDao() throws SQLException {return getDao(Province.class);}

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
