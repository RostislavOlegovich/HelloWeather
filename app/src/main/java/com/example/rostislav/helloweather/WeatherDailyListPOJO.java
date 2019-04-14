package com.example.rostislav.helloweatherupd;

import com.google.android.gms.awareness.state.Weather;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class WeatherDailyListPOJO {

    @SerializedName("main")
    @Expose
    private MainPOJO main;

    @SerializedName("weather")
    @Expose
    private List<WeatherPOJO> weatherPOJO = null;

    @SerializedName("dt")
    @Expose
    private long dt;

    @SerializedName("wind")
    @Expose
    private WindPOJO windPOJO;

    public WindPOJO getWindPOJO() {
        return windPOJO;
    }

    public void setWindPOJO(WindPOJO windPOJO) {
        this.windPOJO = windPOJO;
    }

    public MainPOJO getMain() {
        return main;
    }

    public List<WeatherPOJO> getWeatherPOJO() {
        return weatherPOJO;
    }

    public void setWeatherPOJO(List<WeatherPOJO> weatherPOJO) {
        this.weatherPOJO = weatherPOJO;
    }

    public long getDt() {
        return dt;
    }

    public void setDt_txt(long dt) {
        this.dt = dt;
    }
}
