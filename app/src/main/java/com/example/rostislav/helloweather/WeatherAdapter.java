package com.example.rostislav.helloweatherupd;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherAdapter
        extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private List<WeatherDailyListPOJO> weatherDailyListPOJOS;

    public WeatherAdapter(Context context, List<WeatherDailyListPOJO> weatherDailyListPOJOS){
        this.context = context;
        this.weatherDailyListPOJOS = weatherDailyListPOJOS;
    }

    @NonNull
    @Override
    public WeatherAdapter.WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        mInflater = LayoutInflater.from(context);
        View weatherViewHolder = mInflater.inflate(R.layout.holder_layout,viewGroup,false);
        return new WeatherViewHolder(weatherViewHolder);

    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.WeatherViewHolder weatherViewHolder, int position) {

        // Add the data to the view

        weatherViewHolder.dayOfWeek.setText(
                new SimpleDateFormat("EE, dd MMMM\nHH:mm",Locale.getDefault())
                        .format(new Date(weatherDailyListPOJOS.get(position).getDt()*1000L).getTime()));

        weatherViewHolder.temp.setText(Integer.toString
                (Math.round(weatherDailyListPOJOS.get(position).getMain().getTemp())));


        weatherViewHolder.wind.setText(Float.toString(weatherDailyListPOJOS.get(position).getWindPOJO()
                .getSpeed()));


        weatherViewHolder.descriptionText.setText(weatherDailyListPOJOS
                .get(position)
                .getWeatherPOJO()
                .get(0)
                .getDescription());

        String mainDescription = weatherDailyListPOJOS
                .get(position)
                .getWeatherPOJO()
                .get(0)
                .getMain();


        String icons = weatherDailyListPOJOS
                .get(position)
                .getWeatherPOJO()
                .get(0)
                .getIcon();

        int id = weatherDailyListPOJOS
                .get(position)
                .getWeatherPOJO()
                .get(0)
                .getId();

            if (mainDescription.equals("Clear")) {

                if (icons.equals("01n")) {
                    weatherViewHolder.imageWeather.setImageResource(R.drawable.ic_weather_night);
                } else {
                    weatherViewHolder.imageWeather.setImageResource(R.drawable.ic_weather_sunny);
                }
            } else if (mainDescription.equals("Clouds")) {

                if (id == 801 || id == 802) {
                    weatherViewHolder.imageWeather.setImageResource(R.drawable.ic_weather_cloudy);
                } else {
                    weatherViewHolder.imageWeather.setImageResource(R.drawable.ic_cloud);
                }

            } else if (mainDescription.equals("Rain")) {
                weatherViewHolder.imageWeather.setImageResource(R.drawable.ic_weather_pouring);
            } else if (mainDescription.equals("Drizzle")) {
                weatherViewHolder.imageWeather.setImageResource(R.drawable.ic_weather_rainy);
            } else if (mainDescription.equals("Thunderstorm")) {
                weatherViewHolder.imageWeather.setImageResource(R.drawable.ic_flash);
            } else if (mainDescription.equals("Snow")) {
                weatherViewHolder.imageWeather.setImageResource(R.drawable.ic_weather_snowy);
                //Тут ошибка прои сравненнии не показівает иконку туман
            } else {
                weatherViewHolder.imageWeather.setImageResource(R.drawable.ic_weather_fog);

        }

    }

    @Override
    public int getItemCount() {
        return weatherDailyListPOJOS.size();
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder{

        TextView temp, descriptionText,dayOfWeek, wind;
        ImageView imageWeather;


        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            temp = (TextView)itemView.findViewById(R.id.temp_holder);
            dayOfWeek = (TextView)itemView.findViewById(R.id.day_holder);
            descriptionText = (TextView)itemView.findViewById(R.id.txt_desc_holder);
            wind = (TextView)itemView.findViewById(R.id.wind_speed_holder);
            imageWeather = (ImageView)itemView.findViewById(R.id.image_holder);
        }
    }
}
