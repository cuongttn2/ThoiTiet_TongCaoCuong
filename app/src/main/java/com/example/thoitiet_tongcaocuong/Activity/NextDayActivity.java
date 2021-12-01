package com.example.thoitiet_tongcaocuong.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.thoitiet_tongcaocuong.Adapter.AdapterNextDay;
import com.example.thoitiet_tongcaocuong.R;
import com.example.thoitiet_tongcaocuong.Weather;

public class NextDayActivity extends AppCompatActivity {

    String cityName = "";
    Button btnback;
    TextView tvName;
    ListView lv;
    //
    AdapterNextDay adapterNextDay;
    ArrayList<Weather> arrayListWT;
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_day);
        /*---------------------------------------------------------------------------------------*/
        // custom actionbar
//        color
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.design_default_color_primary)));
//        title
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_title_action_bar);

        /*---------------------------------------------------------------------------------------*/
        Anhxa();
//
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
//
        cityName = city;
        Get7DayData(cityName);
//      back page
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void Anhxa() {
        btnback = findViewById(R.id.btn_prev_page);
        tvName = findViewById(R.id.tv_cityname_nd);
        lv = findViewById(R.id.lv_nextday);
        arrayListWT = new ArrayList<>();
        adapterNextDay = new AdapterNextDay(NextDayActivity.this, arrayListWT);
        lv.setAdapter(adapterNextDay);
    }

    private void Get7DayData(String data) {
        String url = "https://api.openweathermap.org/data/2.5/forecast/?q=" + data + "&cnt=40&units=metric&appid=3fbb62a8024142917b9a91b892b8f53f";
        RequestQueue requestQueue = Volley.newRequestQueue(NextDayActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                          city
                            JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                            String name = jsonObjectCity.getString("name");
                            tvName.setText(name);
//                          list 7 days
                            JSONArray jsonArrayListDay = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArrayListDay.length(); i += 8) {
                                JSONObject jsonObjectList = jsonArrayListDay.getJSONObject(i);
//                              next 7 days

                                String day = jsonObjectList.getString("dt");
                                long lDay = Long.valueOf(day);
                                Date date = new Date(lDay * 1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy/MM/dd");
                                String strDay = simpleDateFormat.format(date);
//                              Temp
                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("main");
                                String max = jsonObjectTemp.getString("temp_max");
                                String min = jsonObjectTemp.getString("temp_min");
                                Double a = Double.valueOf(max);
                                Double b = Double.valueOf(min);
                                String temMax = String.valueOf(a.intValue());
                                String temMin = String.valueOf(b.intValue());
//                              Sataus
                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String status = jsonObjectWeather.getString("description");
//                              icon
                                String icon = jsonObjectWeather.getString("icon");

                                arrayListWT.add(new Weather(strDay, status, icon, temMax, temMin));
                            }
                            adapterNextDay.notifyDataSetChanged();
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);

    }


}