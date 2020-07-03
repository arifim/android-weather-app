package com.cooper.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Toolbar toolbar;

    Button bt_fullReport;
    ImageView im_appBackground;
    ImageView weatherIcon;
    TextView tv_CurrentTemperature;
    TextView tv_TemperatureUnit;
    TextView tv_maxTemperature;
    TextView tv_minTemperature;
    TextView tv_feelsLikeTemp;
    TextView tv_weatherDescription;
    TextView tv_uviIndex;

    androidx.appcompat.widget.SearchView searchView;

    boolean currentUnitIsCelsius = true;
    Bundle savedInstanceState;
    SharedPreferences preferences;
    MenuItem.OnActionExpandListener onActionExpandListener;

    OpenWeather w = new OpenWeather();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.savedInstanceState = savedInstanceState;

        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        w.setLocation(preferences.getString("locationName", "New York"));
        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        w.setUnits(preferences.getString("units_preference", "metric"));




        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Full Report Button
        bt_fullReport = findViewById(R.id.bt_full_report_button);
        bt_fullReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFullReportActivity(v);
            }
        });


        // App Background
        im_appBackground = findViewById(R.id.app_background);
        weatherIcon = findViewById(R.id.im_weather_icon);

        tv_CurrentTemperature = findViewById(R.id.tv_temperature);
        tv_maxTemperature = findViewById(R.id.tv_max_temp);
        tv_minTemperature = findViewById(R.id.tv_min_temp);
        tv_feelsLikeTemp = findViewById(R.id.tv_feels_like_temp);
        tv_weatherDescription = findViewById(R.id.tv_weather_description);
        tv_TemperatureUnit = findViewById(R.id.tv_temp_unit);
        tv_uviIndex = findViewById(R.id.tv_uvi_index);



        new GetWeatherTask().execute();
    }

    // This code is not good !!!!!
    @Override
    protected void onPostResume() {
        super.onPostResume();
        String currentUnit = preferences.getString("units_preference", "metric");
        if (currentUnit != w.getUnits()) {
            w.setUnits(currentUnit);
            new GetWeatherTask().execute();
        }
        if (w.getUnits().equals(OpenWeather.METRIC)) {
            tv_TemperatureUnit.setText("C");
        }
        else
            tv_TemperatureUnit.setText("F");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);

        final MenuItem search = menu.findItem(R.id.toolbar_search_btn);
        searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(this);
//        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                System.out.println("Expand");
//
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                System.out.println("Collapse");
//                return true;
//            }
 //       };


        //search.setOnActionExpandListener(onActionExpandListener);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_setting_btn:
                startActivity(new Intent(this, SettingsActivity.class));
                return  true;
            case R.id.toolbar_search_btn:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void openFullReportActivity(View view) {
        Intent intent = new Intent(this, FullReportActivity.class);
        intent.putExtra("openWeather", w);
        startActivity(intent);
    }


    public void setIcon(int iconId) {
        if (iconId >= 200 && iconId <= 231)
            weatherIcon.setImageResource(R.drawable.ic_storm);
        else if (iconId >= 300 && iconId <= 321)
            weatherIcon.setImageResource(R.drawable.ic_rain_2);
        else if (iconId >= 500 && iconId <= 531)
            weatherIcon.setImageResource(R.drawable.ic_rain);
        else if (iconId >= 600 && iconId <= 622)
            weatherIcon.setImageResource(R.drawable.ic_snowing_1);
        else if (iconId >= 701 && iconId <= 781)
            weatherIcon.setImageResource(R.drawable.ic_rain);
        else if (iconId == 800)
            weatherIcon.setImageResource(R.drawable.ic_sun);
        else
            weatherIcon.setImageResource(R.drawable.ic_cloud);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        w.setLocation(query);
        new GetWeatherTask().execute();
        searchView.clearFocus();
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("locationName", query);
        editor.apply();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private class GetWeatherTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            w.update();
            w.fullReport();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            tv_CurrentTemperature.setText(w.getCurrentTemp().split("\\.")[0]);
            tv_maxTemperature.setText(w.getMaxTemp().split("\\.")[0]);
            tv_minTemperature.setText(w.getMinTemp().split("\\.")[0]);
            tv_feelsLikeTemp.setText(w.getFeelsLike());
            tv_weatherDescription.setText(w.getWeatherDescription());
            setIcon(Integer.parseInt(w.getIconId()));
            toolbar.setTitle(w.getLocationName());
            tv_TemperatureUnit.setText(String.valueOf(w.getUnitsSymbol()));
            tv_uviIndex.setText("UV Index " + w.getUvi());
        }
    }


}
