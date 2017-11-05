package com.xiaofan.www.weather.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.xiaofan.www.weather.MainActivity;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by think on 2017/11/5.
 */

public class Presenter {
    private Context context;
    private MainActivity mainActivity;
    private Subscription subscription;

    public Presenter(Context context){
        this.context=context;
    }

    public void setSubscription(){
        if(context instanceof MainActivity){
            mainActivity=(MainActivity) context;
            subscription=mainActivity.rxBus.toObserverable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object event){
                            if (event instanceof WeatherEvent) {
                                WeatherEvent e=(WeatherEvent)event;
                                if(e.getType()==WeatherEvent.Type.GOTO_WEATHER_PROVINCE) {

                                }
                            }
                        }
                    });
        }else{
            subscription=null;
        }
    }



    public Subscription getSubscription(){
        return subscription;
    }

    public void appExit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);

        int imageResource = android.R.drawable.ic_dialog_alert;
        Drawable image = mainActivity.getResources().getDrawable(imageResource);

        builder.setTitle("退出")
                .setMessage("退出程序吗?")
                .setIcon(image)
                .setCancelable(false)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mainActivity.startActivity(startMain);
                        mainActivity.finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }
}
