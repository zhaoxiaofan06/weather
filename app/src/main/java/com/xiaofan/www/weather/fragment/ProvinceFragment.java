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
import com.xiaofan.www.weather.adapter.ProvinceListAdapter;
import com.xiaofan.www.weather.common.RestClient;
import com.xiaofan.www.weather.model.Province;

import java.util.ArrayList;

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


    public ProvinceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.province_list, container, false);
        mainActivity=(MainActivity) getActivity();
        mainActivity.toolbar.setTitle("");
        mainActivity.toolbar.setTitleTextColor(Color.WHITE);
        mainActivity.title.setText("中国");

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

        flushData();
        return view;
    }

    public void flushData(){
        String url=mainActivity.getResources().getString(R.string.api_url);
        RestClient client= RestClient.getDedault(url);
        Call<ArrayList<Province>> call=client.getProvinceList();
        call.enqueue(new Callback<ArrayList<Province>>(){
            @Override
            public void onResponse(Call<ArrayList<Province>> call, Response<ArrayList<Province>> response){
                try{
                    int code=response.code();
                    switch (code){
                        case 200:
                            provinceList=response.body();
                            Gson gson=new Gson();
                            Log.d("中国",gson.toJson(provinceList));

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

}
