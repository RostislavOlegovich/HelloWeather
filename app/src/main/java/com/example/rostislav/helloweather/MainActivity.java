package com.example.rostislav.helloweatherupd;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 0;
    private String units = "metric";
    private String appId = "b3c830c98614296bcf31d8190248da99";

    private String mQueryString, mLongitude, mLatitude = null;

    private TextView mCityName, mMainTemp, mTempMax, mWindSpeed,
            mTempMin, mDescription, mDate , mErrorResponceMessage;

    private ImageView mIconWeather;
    private RecyclerView mRecyclerView;
    private WeatherAdapter mWeatherAdapter;
    private SwipeRefreshLayout mMySwipeRefreshLayout;
    private RelativeLayout mRelativeLayout;
    private ConstraintLayout mConstraintLayout;
    private CardView mCardViewMain , mCardViewRecycler;
    private Toolbar mToolbar;

    private Calendar mCalendar;
    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private SharedPreferences mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        mCardViewMain = (CardView) findViewById(R.id.cardViewMain);
        mCardViewRecycler = (CardView) findViewById(R.id.cardViewRecycler);
        mMySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.noConnectionLayout);

        mIconWeather = (ImageView) findViewById(R.id.weather_icon);
        mCityName = (TextView) findViewById(R.id.city_name);
        mMainTemp = (TextView) findViewById(R.id.main_temp);
        mTempMax = (TextView)findViewById(R.id.temp_max);
        mTempMin = (TextView)findViewById(R.id.temp_min);
        mWindSpeed = (TextView) findViewById(R.id.windSpeed);
        mDescription = (TextView) findViewById(R.id.weather_description);
        mErrorResponceMessage = (TextView) findViewById(R.id.errorResponceMessage);
        mDate = (TextView) findViewById(R.id.date);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration mDividerItemDecoration =
                new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        setupSwipeRefreshLayout();
        setCurrentDate();
        setupSharedPref();
    }

    public void optionsMenuGetLocation(){
        mCardViewRecycler.setVisibility(View.VISIBLE);
        mErrorResponceMessage.setVisibility(View.GONE);
        mMySwipeRefreshLayout.setEnabled(true);
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.clear();
        editor.apply();
        setupSharedPref();

    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {

            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null && mQueryString == null) {
                        mLastLocation = location;

                        mLongitude = Double.toString(mLastLocation.getLongitude());
                        mLatitude = Double.toString(mLastLocation.getLatitude());
                        mQueryString = null;
                        getConnectionInfo();

                    } else {
                        mDescription.setText(R.string.no_location);
                        getConnectionInfo();
                    }

                }
            });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    getConnectionInfo();
                }
                break;
        }
    }


    private void getConnectionInfo(){

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mConstraintLayout.setVisibility(View.INVISIBLE);
            networkRequest();
            mRelativeLayout.setVisibility(View.VISIBLE);

        }else {
           mRelativeLayout.setVisibility(View.INVISIBLE);
            mConstraintLayout.setVisibility(View.VISIBLE);

        }

    }



    public void setupSharedPref(){

        android.support.v7.preference.PreferenceManager
                .setDefaultValues(this, R.xml.preferences, false);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String string = "";
        String city = mSharedPref.getString("location_preference", null);

        if(city == null || city.equals(string)){
            mQueryString = null;
            getLocation();

        } else {
            mCityName.setText(city);
            mQueryString = mCityName.getText().toString();
            getLocation();
        }
    }


    public void setupRecyclerView(List<WeatherDailyListPOJO> weatherDailyListPOJOS){

            mWeatherAdapter = new WeatherAdapter(this, weatherDailyListPOJOS);
            mRecyclerView.setAdapter(mWeatherAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_get_location) {
            optionsMenuGetLocation();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void networkRequest(){

            NetworkService networkService = NetworkService.getInstance();

            if(mQueryString == null){
                networkService.getJSONApi()
                        .getWeatherInfo(mLatitude,mLongitude,units,appId)
                        .enqueue(new Callback<WeatherResponcePOJO>() {
                            @Override
                            public void onResponse(@NonNull Call<WeatherResponcePOJO> call,
                                                   @NonNull Response<WeatherResponcePOJO> response) {
                                if (response.isSuccessful()) {
                                    WeatherResponcePOJO weatherResponcePOJO = response.body();

                                    List<WeatherPOJO> weathers = weatherResponcePOJO.getWeather();

                                    String main_temp = Integer.toString(Math.round(weatherResponcePOJO.getMain().getTemp()));
                                    String max_temp = Integer.toString(Math.round(weatherResponcePOJO.getMain().getTempMax()));
                                    String min_temp = Integer.toString(Math.round(weatherResponcePOJO.getMain().getTempMin()));
                                    String wind = Float.toString(weatherResponcePOJO.getWindPOJO().getSpeed());

                                    String mainWeatherDesc = weathers.get(0).getMain();
                                    String description = weathers.get(0).getDescription();
                                    String icon = weathers.get(0).getIcon();
                                    int id = weathers.get(0).getId();
                                    String name = weatherResponcePOJO.getName();

                                    mCityName.setText(name);

                                    mQueryString = mCityName.getText().toString();

                                    setWeatherItemsMain(main_temp,max_temp,min_temp,description,wind);

                                    setWeatherIcon(mainWeatherDesc, icon, id);

                                } else {
                                    showErrorResponceMessage();
                                }

                            }

                            @Override
                            public void onFailure(Call<WeatherResponcePOJO> call, Throwable t) {

                                t.printStackTrace();

                            }
                        });
            } else {

                networkService.getJSONApi()
                        .getWeatherInfo(mQueryString, units, appId)
                        .enqueue(new Callback<WeatherResponcePOJO>() {
                            @Override
                            public void onResponse(@NonNull Call<WeatherResponcePOJO> call,
                                                   @NonNull Response<WeatherResponcePOJO> response) {
                                if (response.isSuccessful()) {
                                    WeatherResponcePOJO weatherResponcePOJO = response.body();

                                    List<WeatherPOJO> weathers = weatherResponcePOJO.getWeather();

                                    String main_temp = Integer.toString(Math.round(weatherResponcePOJO.getMain().getTemp()));
                                    String max_temp = Integer.toString(Math.round(weatherResponcePOJO.getMain().getTempMax()));
                                    String min_temp = Integer.toString(Math.round(weatherResponcePOJO.getMain().getTempMin()));
                                    String wind = Float.toString(weatherResponcePOJO.getWindPOJO().getSpeed());

                                    String mainWeatherDesc = weathers.get(0).getMain();
                                    String description = weathers.get(0).getDescription();
                                    String icon = weathers.get(0).getIcon();
                                    int id = weathers.get(0).getId();

                                    setWeatherItemsMain(main_temp, max_temp, min_temp, description,wind);

                                    setWeatherIcon(mainWeatherDesc, icon, id);

                                } else {
                                    showErrorResponceMessage();
                                }

                            }

                            @Override
                            public void onFailure(Call<WeatherResponcePOJO> call, Throwable t) {

                                t.printStackTrace();

                            }
                        });
            }
            if(mQueryString == null){

                networkService
                        .getJSONApi()
                        .getWeatherDailyInfo(mLatitude,mLongitude, units, appId)
                        .enqueue(new Callback<WeatherResponcePOJO>() {
                        @Override
                        public void onResponse(Call<WeatherResponcePOJO> call,
                                               Response<WeatherResponcePOJO> response) {

                            if (response.isSuccessful()) {

                                WeatherResponcePOJO weatherResponcePOJO = response.body();

                                List<WeatherDailyListPOJO> weatherDailyListPOJOS =
                                        weatherResponcePOJO.getWeatherDailyListPOJO();

                                setupRecyclerView(weatherDailyListPOJOS);
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherResponcePOJO> call, Throwable t) {


                            t.printStackTrace();

                        }
                    });
            } else {

                networkService
                        .getJSONApi()
                        .getWeatherDailyInfo(mQueryString, units, appId)
                        .enqueue(new Callback<WeatherResponcePOJO>() {
                        @Override
                        public void onResponse(Call<WeatherResponcePOJO> call,
                                               Response<WeatherResponcePOJO> response) {

                            if (response.isSuccessful()) {

                                WeatherResponcePOJO weatherResponcePOJO = response.body();

                                List<WeatherDailyListPOJO> weatherDailyListPOJOS =
                                        weatherResponcePOJO.getWeatherDailyListPOJO();

                                setupRecyclerView(weatherDailyListPOJOS);
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherResponcePOJO> call, Throwable t) {


                            t.printStackTrace();

                        }
                    });

        }

    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void setWeatherIcon(String description, String icon, int id) {

        if (description.equals("Clear")) {

            if (icon.equals("01n")) {
                mIconWeather.setImageResource(R.drawable.ic_weather_night);
            } else {
                mIconWeather.setImageResource(R.drawable.ic_weather_sunny);
            }
        } else if (description.equals("Clouds")) {

            if (id == 801 || id == 802) {
                mIconWeather.setImageResource(R.drawable.ic_weather_cloudy);
            } else {
                mIconWeather.setImageResource(R.drawable.ic_cloud);
            }

        } else if (description.equals("Rain")) {
            mIconWeather.setImageResource(R.drawable.ic_weather_pouring);
        } else if (description.equals("Drizzle")) {
            mIconWeather.setImageResource(R.drawable.ic_weather_rainy);
        } else if (description.equals("Thunderstorm")) {
            mIconWeather.setImageResource(R.drawable.ic_flash);
        } else if (description.equals("Snow")) {
            mIconWeather.setImageResource(R.drawable.ic_weather_snowy);
        } else {
            mIconWeather.setImageResource(R.drawable.ic_weather_fog);
        }
    }

    public void setCurrentDate(){
        mCalendar = Calendar.getInstance();
        mDate.setText(new SimpleDateFormat("EE, dd MMMM", Locale.getDefault())
                .format(mCalendar.getTime()));
    }

    public void setupSwipeRefreshLayout(){

        mMySwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_green_light);

        mMySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                visibilityRelativeLayout();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getConnectionInfo();
                        mMySwipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });
    }

    private void visibilityRelativeLayout(){
        int i = mRelativeLayout.getVisibility();
        if (i == View.VISIBLE){
            mRelativeLayout.setVisibility(View.INVISIBLE);
        }else {
            mRelativeLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void setWeatherItemsMain(String main_temp, String max_temp,
                                    String min_temp, String description, String wind){

        mMainTemp.setText(main_temp);
        mTempMax.setText(max_temp);
        mTempMin.setText(min_temp);
        mDescription.setText(description);
        mWindSpeed.setText(wind);
        mCardViewMain.setVisibility(View.VISIBLE);

    }

    public void showErrorResponceMessage(){
        mCardViewMain.setVisibility(View.INVISIBLE);
        mCardViewRecycler.setVisibility(View.INVISIBLE);
        mErrorResponceMessage.setVisibility(View.VISIBLE);
        mMySwipeRefreshLayout.setEnabled(false);
    }

}
