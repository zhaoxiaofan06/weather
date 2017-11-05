package com.xiaofan.www.weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaofan.www.weather.R;
import com.xiaofan.www.weather.model.Province;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by think on 2017/11/5.
 */

public class ProvinceListAdapter extends RecyclerView.Adapter<ViewHolder>{
    private View view;
    private ArrayList<Province> provinceList;
    private ProvinceViewHolder provinceViewHolder;

    public void setProvinceListAdapter(ArrayList<Province> provinceList){
        this.provinceList=provinceList;
    }
    public class ProvinceViewHolder extends ViewHolder{
        public TextView textView;

        public ProvinceViewHolder(View view){
            super(view);
            textView=(TextView)view.findViewById(R.id.province_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.province_item,parent,false);
        return new ProvinceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        if(viewHolder instanceof ProvinceViewHolder){
            String name=provinceList.get(position).getName();
            provinceViewHolder=(ProvinceViewHolder)viewHolder;
            provinceViewHolder.textView.setText(name);
        }
    }

    @Override
    public int getItemCount(){
        return provinceList.size();
    }
}
