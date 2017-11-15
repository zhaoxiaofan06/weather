package com.xiaofan.www.weather.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.xiaofan.www.weather.MainActivity;
import com.xiaofan.www.weather.R;
import com.xiaofan.www.weather.adapter.CityListAdapter;
import com.xiaofan.www.weather.adapter.CountyListAdapter;
import com.xiaofan.www.weather.common.RestClient;
import com.xiaofan.www.weather.model.City;
import com.xiaofan.www.weather.model.County;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CountyFragment extends Fragment {
    private View view;
    private MainActivity mainActivity;
    private ArrayList<County> countyList;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CountyListAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout progress_wrap;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String city_id;
    private String province_id;
    private String city_name;

    public CountyFragment() {
        // Required empty public constructor
    }

    public void setCityId(String city_id){
        this.city_id=city_id;
    }

    public void setProvinceId(String province_id){
        this.province_id=province_id;
    }

    public void setCityName(String city_name){
        this.city_name=city_name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.item_list, container, false);
        mainActivity=(MainActivity) getActivity();
        mainActivity.toolbar.setTitle("");
        mainActivity.toolbar.setTitleTextColor(Color.WHITE);
        mainActivity.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });
        mainActivity.title.setText(city_name);

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
        adapter=new CountyListAdapter();
        adapter.setCountyListAdapter(new ArrayList<County>());
        recyclerView.setAdapter(adapter);

        flushData();
        return view;
    }

    public void flushData(){
        String url=mainActivity.getResources().getString(R.string.api_url);
        RestClient client= RestClient.getDedault(url);
        if(!client.getApiUrl().equals(url)){
            client.setDefault(url);
        }
        Call<ArrayList<County>> call=client.getCountyList(province_id,city_id);
        call.enqueue(new Callback<ArrayList<County>>(){
            @Override
            public void onResponse(Call<ArrayList<County>> call, Response<ArrayList<County>> response){
                try{
                    int code=response.code();
                    switch (code){
                        case 200:
                            //Gson gson=new Gson();
                            //Log.d("County:",gson.toJson(call.request()));
                            //Log.d("County:",response.headers().toString());
                            //Log.d("County:",gson.toJson(response.body()));
                            countyList=response.body();
                            adapter.setCountyListAdapter(countyList);
                            recyclerView.setAdapter(adapter);
                            break;
                    }
                }finally {
                    progress_wrap.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<County>> call,Throwable t){
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
