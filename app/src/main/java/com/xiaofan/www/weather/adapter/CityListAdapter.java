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
import com.xiaofan.www.weather.model.City;

import java.util.ArrayList;

/**
 * Created by think on 2017/11/5.
 */

public class CityListAdapter extends RecyclerView.Adapter<ViewHolder>{
    private View view;
    private ArrayList<City> cityList;
    private CityViewHolder cityViewHolder;
    private RxBus rxBus;
    private WeatherEvent weatherEvent;
    private City city;

    public void setCityListAdapter(ArrayList<City> cityList){
        this.cityList=cityList;
        city=new City();
    }
    public class CityViewHolder extends ViewHolder implements View.OnClickListener{
        private CardView cardView;
        public TextView textView;
        public int id;

        public CityViewHolder(View view){
            super(view);
            cardView=(CardView)view.findViewById(R.id.city_cardview_id);
            textView=(TextView)view.findViewById(R.id.city_name);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            city.setId(id);
            city.setName(textView.getText()+"");
            weatherEvent=new WeatherEvent(WeatherEvent.Type.GOTO_WEATHER_COUNTY,0,city);
            if(rxBus.hasObservers()){
                rxBus.send(weatherEvent);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        rxBus=RxBus.getDefault();
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item,parent,false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        if(viewHolder instanceof CityViewHolder){
            String name=cityList.get(position).getName();
            cityViewHolder=(CityViewHolder)viewHolder;
            cityViewHolder.id=cityList.get(position).getId();
            cityViewHolder.textView.setText(name);
        }
    }

    @Override
    public int getItemCount(){
        return cityList.size();
    }
}
