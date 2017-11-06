package com.xiaofan.www.weather.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaofan.www.weather.R;
import com.xiaofan.www.weather.common.RxBus;
import com.xiaofan.www.weather.common.WeatherEvent;
import com.xiaofan.www.weather.model.County;
import com.xiaofan.www.weather.model.Province;

import java.util.ArrayList;

/**
 * Created by think on 2017/11/5.
 */

public class CountyListAdapter extends RecyclerView.Adapter<ViewHolder>{
    private View view;
    private ArrayList<County> countyList;
    private CountyViewHolder countyViewHolder;
    private RxBus rxBus;
    private WeatherEvent weatherEvent;
    private County county;

    public void setCountyListAdapter(ArrayList<County> countyList){
        this.countyList=countyList;
        county=new County();
    }
    public class CountyViewHolder extends ViewHolder implements View.OnClickListener{
        private CardView cardView;
        public TextView textView;
        public int id;
        public String weather_id;

        public CountyViewHolder(View view){
            super(view);
            cardView=(CardView)view.findViewById(R.id.county_cardview_id);
            textView=(TextView)view.findViewById(R.id.county_name);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            county.setId(id);
            county.setName(textView.getText()+"");
            county.setWeatherId(weather_id);
            weatherEvent=new WeatherEvent(WeatherEvent.Type.GOTO_WEATHER_CITY,0,county);
            if(rxBus.hasObservers()){
                rxBus.send(weatherEvent);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        rxBus=RxBus.getDefault();
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.county_item,parent,false);
        return new CountyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        if(viewHolder instanceof CountyViewHolder){
            String name=countyList.get(position).getName();
            countyViewHolder=(CountyViewHolder)viewHolder;
            countyViewHolder.id=countyList.get(position).getId();
            countyViewHolder.weather_id=countyList.get(position).getWeatherId();
            countyViewHolder.textView.setText(name);
        }
    }

    @Override
    public int getItemCount(){
        return countyList.size();
    }
}
