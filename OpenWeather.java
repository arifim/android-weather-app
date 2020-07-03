package com.cooper.weatherapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class OpenWeather implements Serializable {


    class Coord implements Serializable {
        String lon;
        String lat;
    }

    private final String API_KEY = "14ec2812a9f58ad7c1401a8cfd350351";

    private String units = "metric"; // Default metric -> celsius

    public final static String METRIC = "metric";
    public final static String IMPERIAL = "imperial";
    private final String TAG = "openweather";


    private String locationName;
    private String location;
    private String currentTemp;
    private String maxTemp;
    private String minTemp;
    private String feelsLike;
    private String weatherDescription;
    private String iconId;
    private char unitsSymbol;
    private String uvi;
    private String humidity;
    private String precipitation;
    private String cloudCover;
    private String dewPoint;
    private String windSpeed;
    private String windDirection;
    private String pressure;
    private String visibility;
    private Coord coords = new Coord();

    public Coord getCoords() {
        return coords;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public String getCloudCover() {
        return cloudCover;
    }

    public String getDewPoint() {
        return dewPoint;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public String getPressure() {
        return pressure;
    }

    public String getVisibility() {
        return visibility;
    }

    public char getUnitsSymbol() {
        return unitsSymbol;
    }
    public String getWeatherDescription() {
        return weatherDescription;
    }
    public String getCurrentTemp() { return currentTemp; }
    public String getMaxTemp() { return maxTemp; }
    public String getMinTemp() { return minTemp; }
    public String getFeelsLike() { return feelsLike; }
    public void setUnits(String units) {
        if (units.equals(METRIC)) unitsSymbol = 'C';
        else unitsSymbol = 'F';
        this.units = units;
    }
    public String getUnits() { return units; }
    public String getIconId() { return  this.iconId; }
    public String getLocationName() { return this.locationName; }
    public String getUvi() { return uvi; }

    public OpenWeather() {
    }

    public OpenWeather(String location) {
        this.location = location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    private String buildUrlWithLocationName() {
        String uri = Uri.parse("https://api.openweathermap.org/data/2.5/weather")
                .buildUpon()
                .appendQueryParameter("q", location)
                .appendQueryParameter("units", units)
                .appendQueryParameter("appid", API_KEY)
                .build()
                .toString();
        return uri;

    }
// lat=33.441792&lon=-94.037689&exclude=hourly,daily
    private String buildUrlWithCoords() {
        String uri = Uri.parse("https://api.openweathermap.org/data/2.5/onecall?")
                .buildUpon()
                .appendQueryParameter("lat", coords.lat)
                .appendQueryParameter("lon", coords.lon)
                .appendQueryParameter("units", units)
                .appendQueryParameter("exclude", "hourly")
                .appendQueryParameter("appid", API_KEY)
                .build()
                .toString();
        return uri;

    }

    private String getResponse(String uri) {
        String response = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            InputStream in = connection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                response = scanner.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert connection != null;
            connection.disconnect();
        }
        return response;
    }


    public void update() {

        String response = getResponse(buildUrlWithLocationName());
        if (response == null) {
            Log.d(TAG, "update method: The response is null");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(response);

            coords.lon = jsonObject.getJSONObject("coord").getString("lon");
            coords.lat = jsonObject.getJSONObject("coord").getString("lat");
            JSONArray array = jsonObject.getJSONArray("weather");
            JSONObject jb = new JSONObject(array.getString(0));

            Log.d(TAG, "update: " + coords.lat);

            weatherDescription = jb.getString("main");
            System.out.println(weatherDescription);
            iconId = jb.getString("id");
            currentTemp = jsonObject.getJSONObject("main").getString("temp");
            feelsLike = jsonObject.getJSONObject("main").getString("feels_like");
            locationName = jsonObject.getString("name");
            locationName += ", " + jsonObject.getJSONObject("sys").getString("country");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fullReport() {
        String url = buildUrlWithCoords();
        String response = getResponse(url);
        Log.d(TAG, response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            maxTemp = jsonObject.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("max");
            minTemp = jsonObject.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("min");
            uvi = jsonObject.getJSONObject("current").getString("uvi");
            pressure = jsonObject.getJSONObject("current").getString("pressure");
            humidity = jsonObject.getJSONObject("current").getString("humidity");
            dewPoint = jsonObject.getJSONObject("current").getString("dew_point");
            cloudCover = jsonObject.getJSONObject("current").getString("clouds");
            windSpeed = jsonObject.getJSONObject("current").getString("wind_speed");
            windDirection = jsonObject.getJSONObject("current").getString("wind_deg");
            visibility = jsonObject.getJSONObject("current").getString("visibility");
            //precipitation = jsonObject.getJSONArray("minutely").getJSONObject(0).getString("precipitation");


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
