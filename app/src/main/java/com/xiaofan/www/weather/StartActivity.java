package com.xiaofan.www.weather;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaofan.www.weather.common.DbClient;
import com.xiaofan.www.weather.common.RestClient;
import com.xiaofan.www.weather.model.Province;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StartActivity extends AppCompatActivity{
    private ViewPager viewPager;
    private List<ImageView> imageList;
    private List<ImageView> indicatorList;
    private Handler handler;
    public int currentItem=0;
    private Timer timer;
    private LinearLayout linearLayout;
    private TextView textView;
    private int timer_holder;
    private List<Province> provinces=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        init();
    }

    private void init(){
        viewPager=(ViewPager)findViewById(R.id.start_view_pager);
        linearLayout=(LinearLayout)findViewById(R.id.linear_layout);
        textView=(TextView)findViewById(R.id.timer_text);

        imageList=new ArrayList<ImageView>();
        indicatorList=new ArrayList<ImageView>();

        for (int i=0;i<3;i++){
            ImageView imageView=new ImageView(this);
            LinearLayout.LayoutParams params=
                    new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            params.leftMargin=5;
            params.rightMargin=5;
            //params.height=60;
            //params.width=60;

            if(i==0){
                imageView.setImageResource(R.drawable.start1);
            }else if(i==1){
                imageView.setImageResource(R.drawable.start2);
            }else if(i==2){
                imageView.setImageResource(R.drawable.start3);
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            ImageView circleImage=new ImageView(this);
            if(i==0){
                circleImage.setImageResource(R.drawable.line_arrow_up_gray_24);
            }else{
                circleImage.setImageResource(R.drawable.line_write_24);
            }
            linearLayout.addView(circleImage,params);
            indicatorList.add(circleImage);

            imageList.add(imageView);
        }

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(imageList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(imageList.get(position));
                return imageList.get(position);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int size=indicatorList.size();
                for (int i=0;i<size;i++){
                    if(i==(position % size)){
                        indicatorList.get(i).setImageResource(R.drawable.line_arrow_up_gray_24);
                    }else{
                        indicatorList.get(i).setImageResource(R.drawable.line_write_24);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        provinces=DbClient.getInstance(getApplicationContext()).getProvinces();
        if(provinces==null){
            getData();
        }
        timer_holder=imageList.size();
        textView.setText(timer_holder+"");
    }

    @Override
    public void onResume(){
        super.onResume();
        if(currentItem==imageList.size()){
            Intent intent=new Intent(StartActivity.this,MainActivity.class);
            startActivity(intent);
        }else{
            handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    currentItem=msg.what;
                    if(currentItem==imageList.size()){
                        currentItem=0;
                    }
                    viewPager.setCurrentItem(currentItem);
                    textView.setText(timer_holder+"");
                }
            };

            timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    --timer_holder;
                    int number=++currentItem;
                    if(number<imageList.size()){
                        handler.sendEmptyMessage(number);
                    }else{
                        timer.cancel();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent=new Intent(StartActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        },500);
                    }
                }
            },3000,3000);
        }
    }


    public void getData(){
        String url=getResources().getString(R.string.api_url);
        RestClient client= RestClient.getDedault(url);
        Call<ArrayList<Province>> call=client.getProvinceList();
        call.enqueue(new Callback<ArrayList<Province>>(){
            @Override
            public void onResponse(Call<ArrayList<Province>> call, Response<ArrayList<Province>> response){
                try{
                    int code=response.code();
                    switch (code){
                        case 200:
                            DbClient.getInstance(getApplicationContext()).dbHelper.provincesDao().create(response.body());
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Province>> call,Throwable t){
                t.printStackTrace();
            }
        });
    }
}
