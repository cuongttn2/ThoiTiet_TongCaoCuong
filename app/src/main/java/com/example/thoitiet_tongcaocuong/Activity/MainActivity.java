package com.example.thoitiet_tongcaocuong.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.example.thoitiet_tongcaocuong.R;
import com.example.thoitiet_tongcaocuong.Weather;
import com.example.thoitiet_tongcaocuong.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.thoitiet_tongcaocuong.Adapter.AdapterHourly;


public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 1;
    String citydefault; // city default
    ActivityMainBinding binding;
    ArrayList<Weather> weatherList;
    AdapterHourly adapterHourly;

    private LocationManager locationManager;
    private static final int PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        /*-----------------------------------------------------------------------------------------------------------------------------------------*/
        // custom actionbar
//        color
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.textmain)));
//        title
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_title_action_bar);


        /*-----------------------------------------------------------------------------------------------------------------------------------------*/
//      RECYCLERVIEW
        weatherList = new ArrayList<>();
        binding.rvHourly.setHasFixedSize(true);

        /*-----------------------------------------------------------------------------------------------------------------------------------------*/
//      RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,1,RecyclerView.HORIZONTAL,false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvHourly.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.rvHourly.addItemDecoration(dividerItemDecoration);

        /*-----------------------------------------------------------------------------------------------------------------------------------------*/
//      default location

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        /*-----------------------------------------------------------------------------------------------------------------------------------------*/
//      set cityname

        try {
            if (citydefault == null) {
                String str = "hanoi";
                citydefault = str;
            } else
                citydefault = getCity(location.getLongitude(), location.getLatitude());

            GetHourlyWeather(citydefault);
            sendNotification1();
//            sendNotification3();

        } catch (Exception exceptione) {
            sendNotification2();
        }

//        String test = getCity(location.getLongitude(),location.getLatitude());

        /*-----------------------------------------------------------------------------------------------------------------------------------------*/
//      get weather hourly
//        GetHourlyWeather(citydefault);
        GetCurrentWeather1(citydefault);
        GetCurrentWeather2(citydefault);

        /*-----------------------------------------------------------------------------------------------------------------------------------------*/
//      search, weather of default city
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }

        });
        Intent intent = getIntent();
        String cityname = intent.getStringExtra("cityname");
        GetCurrentWeather1(cityname);
        GetCurrentWeather2(cityname);

        /*-----------------------------------------------------------------------------------------------------------------------------------------*/
//      reload location
        binding.imgLoadLoaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = "hanoi";//getCity(location.getLongitude(), location.getLatitude());
                GetCurrentWeather1(city);
                GetCurrentWeather2(city);
            }
        });

        /*-----------------------------------------------------------------------------------------------------------------------------------------*/
        //      Next 7 days
        binding.btnNextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = binding.tvLocation.getText().toString();

                Intent intent = new Intent(MainActivity.this, NextDayActivity.class);
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });

    }// end onCreate

    //  Noftiication
    private int getNotificationId(){

        return (int) new Date().getTime();
    }

    private void sendNotification2() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.weather_screen_icon);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Turn on GPS ")
                .setContentText("Message")
                .setSmallIcon(R.drawable.ic_baseline_message_24)
                .setLargeIcon(bitmap)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(getNotificationId(), notification);
    }


    private void sendNotification1() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.weather_screen_icon);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Turn on GPS when using the app")
                .setContentText("Message")
                .setSmallIcon(R.drawable.ic_baseline_message_24)
                .setLargeIcon(bitmap)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(getNotificationId(), notification);


    }

    private void sendNotification3() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.weather_screen_icon);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("it's raining today")
                .setContentText("Message")
                .setSmallIcon(R.drawable.ic_baseline_message_24)
                .setLargeIcon(bitmap)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(getNotificationId(), notification);


    }

    /*-----------------------------------------------------------------------------------------------------------------------------------------*/


    /*-----------------------------------------------------------------------------------------------------------------------------------------*/
    //  closeKeyBoard
    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethod.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /*-----------------------------------------------------------------------------------------------------------------------------------------*/
    private String getCity(double longitude, double latitude) {
        String city = "not found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);
            for (Address adr : addresses) {
                String citydefault = adr.getLocality();
                if (citydefault != null && !citydefault.equals("")) {
                    city = citydefault;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return city;
    }

    /*-----------------------------------------------------------------------------------------------------------------------------------------*/
    // check permission,     PEMISSION LOCCATION
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "pesmission grant", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "please provide the permission ", Toast.LENGTH_SHORT).show();
//                finish();
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------------------------------------------------*/
    // get hourly
    public void GetHourlyWeather(String data) {
//
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + data + "&units=metric&appid=3fbb62a8024142917b9a91b892b8f53f";
//       String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + data + "&units=metric&appid=3fbb62a8024142917b9a91b892b8f53f";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            weatherList = new ArrayList<>();
                            Weather weather;

                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArrayListDay = jsonObject.getJSONArray("list");

                            for (int i = 0; i < 8; i++) {
                                JSONObject jsonObjectList = jsonArrayListDay.getJSONObject(i);
//                      Hour
                                String hour = jsonObjectList.getString("dt_txt");

//                      Temp
                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("main");

//                        String feel = jsonObjectMain.getString("feels_like");
//                        Double a = Double.valueOf(feel);
//                        String tempFeel = String.valueOf(a.intValue());
                                String max = jsonObjectTemp.getString("temp_max");
                                Double a = Double.valueOf(max);
                                String temMax = String.valueOf(a.intValue());

                                String min = jsonObjectTemp.getString("temp_min");
                                Double b = Double.valueOf(min);
                                String temMin = String.valueOf(b.intValue());
//                      icon
                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String icon = jsonObjectWeather.getString("icon");


//                      add hour
                                weather = new Weather(hour, temMax, temMin, icon);

                                weatherList.add(weather);

                            }//end for

                            Collections.reverse(weatherList);
                            adapterHourly = new AdapterHourly(MainActivity.this, weatherList);
                            binding.rvHourly.setAdapter(adapterHourly);

                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                });
        requestQueue.add(stringRequest);
    }
    /*-----------------------------------------------------------------------------------------------------------------------------------------*/
//  get current day weather

    public void GetCurrentWeather1(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&appid=3fbb62a8024142917b9a91b892b8f53f";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //city name
                    String cityName = jsonObject.getString("name");
                    binding.tvLocation.setText(cityName + "");
                    // date
                    //----------------------------------------------------------------
                    // current day
                    String day = jsonObject.getString("dt");
                    long lDay = Long.valueOf(day);
                    Date date = new Date(lDay * 1000L);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy/MM/dd HH:mm:ss");
                    String strDay = simpleDateFormat.format(date);
                    binding.tvDay.setText(strDay);
                    // City
                    JSONObject jsonObjectCity = jsonObject.getJSONObject("sys");
                    // sunsise
                    String timeRise = jsonObjectCity.getString("sunrise");
                    long lTimeRise = Long.valueOf(timeRise);
                    Date dateRise = new Date(lTimeRise * 1000L);
                    SimpleDateFormat simpleDateFormatTR = new SimpleDateFormat("HH:mm:ss");
                    String strTimeRise = simpleDateFormatTR.format(dateRise);
                    binding.tvCitySunrise.setText("Sunrise: " + strTimeRise);
                    //sunset
                    String timeSet = jsonObjectCity.getString("sunset");
                    long lTimeSet = Long.valueOf(timeSet);
                    Date dateSet = new Date(lTimeSet * 1000L);
                    SimpleDateFormat simpleDateFormatTS = new SimpleDateFormat("HH:mm:ss");
                    String strTimeSet = simpleDateFormatTS.format(dateSet);
                    binding.tvCitySunset.setText("Sunset: " + strTimeSet);

                    String country = jsonObjectCity.getString("country");
                    binding.tvCityCountry.setText("Country: " + country);
                    //----------------------------------------------------------------------


                    // channge background
                    if (lDay > lTimeRise && lDay < lTimeSet) {
                        if (binding.tvWtMain.equals("Rain")) {
                            binding.home1.setBackgroundResource(R.drawable.morning_rain);
                        } else {
                            binding.home1.setBackgroundResource(R.drawable.mnt5);
                        }

                    } else if (lDay == lTimeRise) {
                        if (binding.tvWtMain.equals("Rain")) {
                            binding.home1.setBackgroundResource(R.drawable.morning_rain);
                        } else {
                            binding.home1.setBackgroundResource(R.drawable.mnt9);
                        }

                    } else if (lDay == lTimeSet) {
                        if (binding.tvWtMain.equals("Rain")) {
                            binding.home1.setBackgroundResource(R.drawable.night_rain);
                        } else {
                            binding.home1.setBackgroundResource(R.drawable.mnt3);
                        }
                    } else {
                        if (binding.tvWtMain.equals("Rain")) {
                            binding.home1.setBackgroundResource(R.drawable.night_rain);
                        } else {
                            binding.home1.setBackgroundResource(R.drawable.ns6);
                        }
                    }
                    //----------------------------------------------------------------------
                    // weather
                    JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                    String status = jsonObjectWeather.getString("main");
                    binding.tvWtMain.setText("Status: " + status);
                    String description = jsonObjectWeather.getString("description");
                    binding.tvWtDecription.setText("Description: " + description);
                    String icon = jsonObjectWeather.getString("icon");

                    Picasso.with(MainActivity.this).load("https://openweathermap.org/img/wn/" + icon + ".png").into(binding.iconWeather);
                    // main information weather
                    JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                    String temp = jsonObjectMain.getString("temp");
                    Double tempX = Double.valueOf(temp);
                    binding.tvTemp.setText(temp + "ºC");

                    String pressure = jsonObjectMain.getString("pressure");
                    binding.tvPressure.setText("Pressure: " + pressure + " hpa");
                    String humidity = jsonObjectMain.getString("humidity");
                    binding.tvHumidity.setText("Humidity: " + humidity + " %");
                    String tempMin = jsonObjectMain.getString("temp_min");
                    binding.tvTempMin.setText("Min: " + tempMin + " ºC");
                    String tempMax = jsonObjectMain.getString("temp_max");
                    binding.tvTempMax.setText("Max: " + tempMax + " ºC");
                    // Wind
                    JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                    String speed = jsonObjectWind.getString("speed");
                    binding.tvWindSpeed.setText("Speed: " + speed + " m/s");
                    String deg = jsonObjectWind.getString("deg");
                    binding.tvWindDeg.setText("Deg: " + deg);


                    // Clouds
                    JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                    String all = jsonObjectClouds.getString("all");
                    binding.tvClouds.setText("Clouds: " + all + " %");

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

    /*-----------------------------------------------------------------------------------------------------------------------------------------*/
    public void GetCurrentWeather2(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + data + "&cnt=7&units=metric&appid=3fbb62a8024142917b9a91b892b8f53f";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // temp K, feel_like
                    JSONArray jsonArrayListDay = jsonObject.getJSONArray("list");
                    JSONObject jsonObjectDay1 = jsonArrayListDay.getJSONObject(0);

                    JSONObject jsonObjectMain = jsonObjectDay1.getJSONObject("main");
                    // temp K
                    String tempKF = jsonObjectMain.getString("temp_kf");
                    binding.tvTempKF.setText("Temp(F): " + tempKF + "°");
                    // feel
                    String feel = jsonObjectMain.getString("feels_like");
                    Double a = Double.valueOf(feel);
                    String tempFeel = String.valueOf(a.intValue());
                    binding.tvFeelsLike.setText("Feels like: " + tempFeel + " °C");
                    // visibility
                    String visibility = jsonObjectDay1.getString("visibility");
                    binding.tvVisibility.setText("Visibility: " + visibility + " m");
                    // population
                    JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                    String population = jsonObjectCity.getString("population");
                    binding.tvCityPopulation.setText("Population: " + population);

                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
                //
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                });
        requestQueue.add(stringRequest);
    }


}//end