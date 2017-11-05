package com.xiaofan.www.weather.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by think on 2017/10/27.
 */

public class DbHelper extends OrmLiteSqliteOpenHelper{
    public static final String DATABASE_NAME = "shop.db";
    public static final int DATABASE_VERSION = 1710272;
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
//        try{
//            TableUtils.createTableIfNotExists(connectionSource, LoginReturn.class);
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
    }

//    public Dao<LoginReturn,Integer> loginReturnsDao() throws SQLException {return getDao(LoginReturn.class);}

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
