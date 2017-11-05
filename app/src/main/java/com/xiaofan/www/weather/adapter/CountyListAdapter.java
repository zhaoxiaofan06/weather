package com.xiaofan.www.weather.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaofan.www.weather.R;
import com.xiaofan.www.weather.common.RxBus;
import com.xiaofan.www.weather.common.WeatherEvent;
import com.xiaofan.www.weather.model.Province;

import java.util.ArrayList;

/**
 * Created by think on 2017/11/5.
 */

public class CountyListAdapter extends RecyclerView.Adapter<ViewHolder>{
    private View view;
    private ArrayList<Province> provinceList;
    private ProvinceViewHolder provinceViewHolder;
    private RxBus rxBus;
    private WeatherEvent weatherEvent;

    public void setProvinceListAdapter(ArrayList<Province> provinceList){
        this.provinceList=provinceList;
    }
    public class ProvinceViewHolder extends ViewHolder implements View.OnClickListener{
        private CardView cardView;
        public TextView textView;
        public int id;

        public ProvinceViewHolder(View view){
            super(view);
            cardView=(CardView)view.findViewById(R.id.county_cardview_id);
            textView=(TextView)view.findViewById(R.id.province_name);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            weatherEvent=new WeatherEvent(WeatherEvent.Type.GOTO_WEATHER_CITY,id,null);
            if(rxBus.hasObservers()){
                rxBus.send(weatherEvent);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        rxBus=RxBus.getDefault();
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.province_item,parent,false);
        return new ProvinceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        if(viewHolder instanceof ProvinceViewHolder){
            String name=provinceList.get(position).getName();
            provinceViewHolder=(ProvinceViewHolder)viewHolder;
            provinceViewHolder.id=provinceList.get(position).getId();
            provinceViewHolder.textView.setText(name);
        }
    }

    @Override
    public int getItemCount(){
        return provinceList.size();
    }
}
