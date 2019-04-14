package com.example.rostislav.helloweatherupd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WindPOJO {

    @SerializedName("speed")
    @Expose
    private float speed;

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
