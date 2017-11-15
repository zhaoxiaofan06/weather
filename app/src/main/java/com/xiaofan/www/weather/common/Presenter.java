package com.xiaofan.www.weather.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.xiaofan.www.weather.MainActivity;
import com.xiaofan.www.weather.R;
import com.xiaofan.www.weather.fragment.CityFragment;
import com.xiaofan.www.weather.fragment.CountyFragment;
import com.xiaofan.www.weather.fragment.WeatherFragment;
import com.xiaofan.www.weather.model.City;
import com.xiaofan.www.weather.model.County;
import com.xiaofan.www.weather.model.Province;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
    private Handler handler;

    public Presenter(Context context){
        this.context=context;
    }

    public void setSubscription(){
        if(context instanceof MainActivity){
            mainActivity=(MainActivity) context;
            subscription=mainActivity.rxBus.toObserverable(WeatherEvent.class)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<WeatherEvent>() {
                        @Override
                        public void call(WeatherEvent e){
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

                            }else if(e.getType()==WeatherEvent.Type.GOTO_WEATHER_DETAIL){
                                object=e.getObject();
                                if(object instanceof County){
                                    County county=(County) object;
                                    WeatherFragment weatherFragment=new WeatherFragment();
                                    weatherFragment.setCityName(county.getName());
                                    weatherFragment.setCityId(county.getId()+"");
                                    weatherFragment.setWeatherId(county.getWeatherId());
                                    mainActivity.replaceFragmnet(weatherFragment);
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

    public void setViewPager(){
        LayoutInflater inflater=mainActivity.getLayoutInflater();
        View view1=inflater.inflate(R.layout.layout1, null);
        View view2=inflater.inflate(R.layout.layout2, null);
        View view3=inflater.inflate(R.layout.layout3, null);
        ImageView imageView=new ImageView(mainActivity);
        imageView.setImageResource(R.drawable.ic_arrow_back_white_24dp);

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mainActivity,"测试1被点击"+mainActivity.currentItem,Toast.LENGTH_SHORT).show();
            }
        });

        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mainActivity,"测试2被点击"+mainActivity.currentItem,Toast.LENGTH_SHORT).show();
            }
        });

        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mainActivity,"测试3被点击"+mainActivity.currentItem,Toast.LENGTH_SHORT).show();
            }
        });

        mainActivity.viewList=new ArrayList<View>();
        mainActivity.viewList.add(view1);
        mainActivity.viewList.add(view2);
        mainActivity.viewList.add(view3);

        mainActivity.viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mainActivity.viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(mainActivity.viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(mainActivity.viewList.get(position));


                return mainActivity.viewList.get(position);
            }
        });

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                mainActivity.currentItem=msg.what;
                if(mainActivity.currentItem==mainActivity.viewList.size()){
                    mainActivity.currentItem=0;
                }
                mainActivity.viewPager.setCurrentItem(mainActivity.currentItem);
            }
        };

        mainActivity.timer=new Timer();
        mainActivity.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(++mainActivity.currentItem);
            }
        },1000,5000);
    }
}
