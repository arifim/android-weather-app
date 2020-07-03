package com.cooper.weatherapp;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FullReportFragment extends Fragment {


    private OpenWeather weather;
    private final String TAG = "FullReportFragment";

    TextView tv_cityName;
    TextView tv_time;
    TextView tv_weather_des;

    FullReportFragment(OpenWeather weather) {
        this.weather = weather;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view  = inflater.inflate(R.layout.full_report_fragment_layout, container, false);
        ListView listView = view.findViewById(R.id.fr_list_view);


        tv_cityName = view.findViewById(R.id.fr_tv_city_name);
        tv_time = view.findViewById(R.id.fr_tv_time);
        tv_weather_des = view.findViewById(R.id.fr_tv_weather_des);

        tv_cityName.setText(weather.getLocationName());

        DateFormat date = new SimpleDateFormat("MMM d, h:mm a");
        tv_time.setText(date.format(new Date()));
        tv_weather_des.setText(weather.getWeatherDescription());

        List<FullReportData> dataArrayList = new ArrayList<>();
        dataArrayList.add(new FullReportData("Temperature", weather.getCurrentTemp() + "˚" + weather.getUnitsSymbol()));
        dataArrayList.add(new FullReportData("Feels Like", weather.getFeelsLike() + "˚" + weather.getUnitsSymbol()));
        dataArrayList.add(new FullReportData("UV Index", weather.getUvi()));
        dataArrayList.add(new FullReportData("Humidity", weather.getHumidity() + " %"));
        dataArrayList.add(new FullReportData("Precipitation", ""));
        dataArrayList.add(new FullReportData("Cloud Cover", weather.getCloudCover() + " %"));
        dataArrayList.add(new FullReportData("Dew Point", weather.getDewPoint() + "˚" + weather.getUnitsSymbol()));
        dataArrayList.add(new FullReportData("Wind Speed", weather.getWindSpeed() + " m/s"));
        dataArrayList.add(new FullReportData("Wind Direction", weather.getWindDirection() + "˚"));
        dataArrayList.add(new FullReportData("Pressure", weather.getPressure() + " mb (hPa)"));
        dataArrayList.add(new FullReportData("Visibility", weather.getVisibility() + " m"));


        FullReportListAdapter listAdapter = new FullReportListAdapter(getActivity(), R.layout.adapter_view_layout, dataArrayList);
        listView.setAdapter(listAdapter);
        return view;
    }
}

class FullReportListAdapter extends ArrayAdapter<FullReportData> {

    private Context mContext;
    int mResource;

    public FullReportListAdapter(@NonNull Context context, int resource, @NonNull List<FullReportData> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String description = getItem(position).getDescription();
        String value = getItem(position).getValue();

        //FullReportData fld = new FullReportData(description, value);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tv_description = convertView.findViewById(R.id.fr_descrp);
        TextView tv_value = convertView.findViewById(R.id.fr_value);

        tv_description.setText(description);
        tv_value.setText(value);

        return convertView;
    }
}
