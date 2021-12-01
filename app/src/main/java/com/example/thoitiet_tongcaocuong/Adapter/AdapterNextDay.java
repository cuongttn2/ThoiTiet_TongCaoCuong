package com.example.thoitiet_tongcaocuong.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thoitiet_tongcaocuong.R;
import com.example.thoitiet_tongcaocuong.Weather;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterNextDay extends BaseAdapter {
    Context context;
    ArrayList<Weather> arrayList;

    public AdapterNextDay(Context context, ArrayList<Weather> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_lv_nextday, null);

        Weather weather = arrayList.get(position);

        TextView tvDay = convertView.findViewById(R.id.tv_nextday);
        TextView tvStatus = convertView.findViewById(R.id.tv_status_nextday);
        TextView tvMaxTemp = convertView.findViewById(R.id.tv_maxtemp_nextday);
        TextView tvMinTemp = convertView.findViewById(R.id.tv_mintemp_nextday);
        ImageView ImgStatus = convertView.findViewById(R.id.img_status_nextday);

        tvDay.setText(weather.Day);
        tvStatus.setText(weather.Status);
        tvMaxTemp.setText("max: " + weather.MaxTemp + "ºC");
        tvMinTemp.setText("min: " + weather.MinTemp + "ºC");

        Picasso.with(context).load("https://openweathermap.org/img/wn/" + weather.Image + ".png").into(ImgStatus);

        return convertView;
    }
}
