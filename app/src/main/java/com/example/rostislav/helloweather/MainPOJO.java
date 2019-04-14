package com.example.rostislav.helloweatherupd;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class MainPOJO {


    @SerializedName("temp")
    @Expose
    private float  temp;

    @SerializedName("temp_min")
    @Expose
    private float tempMin;

    @SerializedName("temp_max")
    @Expose
    private float tempMax;



    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

}
