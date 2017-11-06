package com.xiaofan.www.weather.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.xiaofan.www.weather.MainActivity;
import com.xiaofan.www.weather.fragment.CityFragment;
import com.xiaofan.www.weather.fragment.CountyFragment;
import com.xiaofan.www.weather.model.City;
import com.xiaofan.www.weather.model.Province;

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
    private String province_id;
    private String city_id;
    private Object object;
    private int[] ids= new int[]{1,2,3,4,5,6};

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
                                if(e.getType()==WeatherEvent.Type.GOTO_WEATHER_CITY) {
                                    object=e.getObject();
                                    if(object instanceof Province){
                                        Province province=(Province)object;
                                        province_id=province.getId()+"";

                                        boolean flag=false;
                                        for (int id:ids) {
                                            if(id==province.getId()){
                                                flag=true;
                                            }
                                        }

                                       if(flag){
                                           CountyFragment countyFragment=new CountyFragment();
                                           countyFragment.setCityId(province_id);
                                           countyFragment.setCityName(province.getName());
                                           countyFragment.setProvinceId(province_id);
                                           mainActivity.replaceFragmnet(countyFragment);
                                       }else{
                                           CityFragment cityFragment=new CityFragment();
                                           cityFragment.setProvinceId(province_id);
                                           cityFragment.setProvinceName(province.getName());
                                           mainActivity.replaceFragmnet(cityFragment);
                                       }
                                    }

                                }else if(e.getType()==WeatherEvent.Type.GOTO_WEATHER_COUNTY){
                                    object=e.getObject();
                                    if(object instanceof City){
                                        City city=(City) object;
                                        CountyFragment countyFragment=new CountyFragment();
                                        city_id=city.getId()+"";
                                        countyFragment.setCityId(city_id);
                                        countyFragment.setCityName(city.getName());
                                        countyFragment.setProvinceId(province_id);
                                        mainActivity.replaceFragmnet(countyFragment);
                                    }

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
