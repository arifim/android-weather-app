package com.cooper.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.r0adkll.slidr.Slidr;

public class FullReportActivity extends AppCompatActivity {

    private OpenWeather weather;
    private final String TAG = "FullReportActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_report);
        weather = (OpenWeather) getIntent().getSerializableExtra("openWeather");

        Log.d(TAG, "onCreate: " + weather.getLocationName());

        Slidr.attach(this);

        BottomNavigationView navigation_menu = findViewById( R.id.navigation_menu);
        navigation_menu.setOnNavigationItemSelectedListener(nav_items_listener);
        navigation_menu.setSelectedItemId(R.id.nav_full_report); // By default full report fragment
    }

    private BottomNavigationView.OnNavigationItemSelectedListener nav_items_listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_daily:
                            selectedFragment = new DailyFragment();
                            break;
                        case R.id.nav_full_report:
                            selectedFragment = new FullReportFragment(weather);
                            break;
                        case R.id.nav_settings:
                            selectedFragment = new SettingsFragment();
                            break;
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout, selectedFragment)
                            .commit();
                    return true;
                }
            };
}
