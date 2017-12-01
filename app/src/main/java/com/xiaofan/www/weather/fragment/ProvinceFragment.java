package com.xiaofan.www.weather.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.xiaofan.www.weather.MainActivity;
import com.xiaofan.www.weather.R;
import com.xiaofan.www.weather.adapter.ProvinceListAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ProvinceFragment extends Fragment {
    private View view;
    private MainActivity mainActivity;
    private ArrayList<Province> provinceList;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ProvinceListAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout progress_wrap;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler;
    private ViewPager viewPager;
    private ArrayList<ImageView> viewList;
    public int currentItem;


    public ProvinceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.home_item_list, container, false);
        mainActivity=(MainActivity) getActivity();
        viewPager=(ViewPager)view.findViewById(R.id.view_page_main);

        viewList=new ArrayList<ImageView>();
        for (int i=0;i<3;i++){
            ImageView imageView=new ImageView(mainActivity);
            if(i==0){
                imageView.setImageResource(R.mipmap.weather_pic1);
            }else if(i==1){
                imageView.setImageResource(R.mipmap.weather_pic2);
            }else if(i==2){
                imageView.setImageResource(R.mipmap.weather_pic3);
            }
            viewList.add(imageView);
        }

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));


                return viewList.get(position);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //viewList.get(position).setBackgroundResource(R.drawable.ic_mood_black_18dp);
                //Toast.makeText(mainActivity, position+"被选择了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                currentItem=msg.what;
                if(currentItem==viewList.size()){
                    currentItem=0;
                }
                viewPager.setCurrentItem(currentItem);
            }
        };

        mainActivity.timer=new Timer();
        mainActivity.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(++currentItem);
            }
        },1000,5000);


        mainActivity.toolbar.setTitle("");
        mainActivity.toolbar.setTitleTextColor(Color.WHITE);
        mainActivity.toolbar.setNavigationIcon(R.drawable.menu_write_32);
        mainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        mainActivity.title.setText("中国");
        mainActivity.menu.findItem(R.id.more).setVisible(true);

        progress_wrap=(LinearLayout)view.findViewById(R.id.progress_wrap);
        progress_wrap.setVisibility(View.VISIBLE);
        progressBar=(ProgressBar)view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorDodgerBlue));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ProvinceListAdapter();

        List<Province> provinces= DbClient.getInstance(getContext()).getProvinces();
        if(provinces==null){
            adapter.setProvinceListAdapter(new ArrayList<Province>());
            recyclerView.setAdapter(adapter);
            flushData();
        }else{
            adapter.setProvinceListAdapter((ArrayList<Province>)provinces);
            recyclerView.setAdapter(adapter);
            progress_wrap.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }

        return view;
    }

    public void flushData(){
        String url=mainActivity.getResources().getString(R.string.api_url);
        RestClient client= RestClient.getDedault(url);
        if(!client.getApiUrl().equals(url)){
            client.setDefault(url);
        }
        Call<ArrayList<Province>> call=client.getProvinceList();
        call.enqueue(new Callback<ArrayList<Province>>(){
            @Override
            public void onResponse(Call<ArrayList<Province>> call, Response<ArrayList<Province>> response){
                try{
                    int code=response.code();
                    switch (code){
                        case 200:
                            provinceList=response.body();
                            //Gson gson=new Gson();
                            //Log.d("中国",gson.toJson(provinceList));

                            adapter.setProvinceListAdapter(provinceList);
                            recyclerView.setAdapter(adapter);
                            break;
                    }
                }finally {
                    progress_wrap.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Province>> call,Throwable t){
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
