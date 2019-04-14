package com.example.rostislav.helloweatherupd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponcePOJO {

    @SerializedName("weather")
    @Expose
    private List<WeatherPOJO> weather = null;

    @SerializedName("main")
    @Expose
    private MainPOJO main;

    @SerializedName("wind")
    @Expose
    private WindPOJO windPOJO;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("list")
    @Expose
    private List<WeatherDailyListPOJO> weatherDailyListPOJO = null;

    public List<WeatherDailyListPOJO> getWeatherDailyListPOJO() {
        return weatherDailyListPOJO;
    }

    public void setWeatherDailyListPOJO(List<WeatherDailyListPOJO> weatherDailyListPOJO) {
        this.weatherDailyListPOJO = weatherDailyListPOJO;
    }

    public List<WeatherPOJO> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherPOJO> weather) {
        this.weather = weather;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public MainPOJO getMain() {
        return main;
    }

    public void setMain(MainPOJO main) {
        this.main = main;
    }

    public WindPOJO getWindPOJO() {
        return windPOJO;
    }

    public void setWindPOJO(WindPOJO windPOJO) {
        this.windPOJO = windPOJO;
    }
}
