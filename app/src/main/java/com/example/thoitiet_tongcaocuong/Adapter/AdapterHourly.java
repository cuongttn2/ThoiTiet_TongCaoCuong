package com.example.thoitiet_tongcaocuong.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thoitiet_tongcaocuong.Activity.MainActivity;
import com.example.thoitiet_tongcaocuong.R;
import com.example.thoitiet_tongcaocuong.Weather;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterHourly extends RecyclerView.Adapter<AdapterHourly.ViewHolder> {

    ArrayList<Weather> weatherList;

    public AdapterHourly(MainActivity mainActivity, ArrayList<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public AdapterHourly.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_hourly, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterHourly.ViewHolder holder, int position) {
        Weather weather = weatherList.get(position);
        holder.time.setText(weather.Day);
        holder.min.setText(weather.MinTemp+"ºC");
        holder.max.setText(weather.MaxTemp+"ºC");
        Picasso.with(holder.icon.getContext()).load("https://openweathermap.org/img/wn/" + weather.Image + ".png").into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, min, max;
        ImageView icon;
        public ViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.it_rv_time);
            min = itemView.findViewById(R.id.it_rv_tempmin);
            max = itemView.findViewById(R.id.it_rv_tempmax);
            icon = itemView.findViewById(R.id.it_rv_icon);
        }
    }
}
