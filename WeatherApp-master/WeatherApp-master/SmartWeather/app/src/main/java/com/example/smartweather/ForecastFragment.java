package com.example.smartweather;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.TransitionDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;




/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "ForecastFragment";
    private static JSONObject weatherObject;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProgressDialog progressDialog;
    private ScrollView parentScrollView;
    private LinearLayout parentLinearLayout;
    private RelativeLayout nextDay1;
    private RelativeLayout nextDay2;
    private RelativeLayout nextDay3;
    private RelativeLayout nextDay4;
    private RelativeLayout nextDay5;
    private RelativeLayout nextDay6;
    private RelativeLayout nextDay7;

    private TextView forecastPrecipitation1;
    private TextView forecastPrecipitation2;
    private TextView forecastPrecipitation3;
    private TextView forecastPrecipitation4;
    private TextView forecastPrecipitation5;
    private TextView forecastPrecipitation6;
    private TextView forecastPrecipitation7;

    private TextView forecastHigh1;
    private TextView forecastHigh2;
    private TextView forecastHigh3;
    private TextView forecastHigh4;
    private TextView forecastHigh5;
    private TextView forecastHigh6;
    private TextView forecastHigh7;

    private TextView forecastLow1;
    private TextView forecastLow2;
    private TextView forecastLow3;
    private TextView forecastLow4;
    private TextView forecastLow5;
    private TextView forecastLow6;
    private TextView forecastLow7;

    private ImageView icon1;
    private ImageView icon2;
    private ImageView icon3;
    private ImageView icon4;
    private ImageView icon5;
    private ImageView icon6;
    private ImageView icon7;

    private TextView nextDayDescription1;
    private TextView nextDayDescription2;
    private TextView nextDayDescription3;
    private TextView nextDayDescription4;
    private TextView nextDayDescription5;
    private TextView nextDayDescription6;
    private TextView nextDayDescription7;

    private TextView nextDayDate1;
    private TextView nextDayDate2;
    private TextView nextDayDate3;
    private TextView nextDayDate4;
    private TextView nextDayDate5;
    private TextView nextDayDate6;
    private TextView nextDayDate7;

    private TextView nextDayName1;
    private TextView nextDayName2;
    private TextView nextDayName3;
    private TextView nextDayName4;
    private TextView nextDayName5;
    private TextView nextDayName6;
    private TextView nextDayName7;

    public ForecastFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForecastFragment newInstance(String param1, String param2) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Getting latest info");
        progressDialog.show();

        parentScrollView = view.findViewById(R.id.parent_scroll_view);
        parentLinearLayout = view.findViewById(R.id.parent_linear_layout);
        nextDay1 = view.findViewById(R.id.next_day_1_relative_layout);
        nextDay2 = view.findViewById(R.id.next_day_2_relative_layout);
        nextDay3 = view.findViewById(R.id.next_day_3_relative_layout);
        nextDay4 = view.findViewById(R.id.next_day_4_relative_layout);
        nextDay5 = view.findViewById(R.id.next_day_5_relative_layout);
        nextDay6 = view.findViewById(R.id.next_day_6_relative_layout);
        nextDay7 = view.findViewById(R.id.next_day_7_relative_layout);

        forecastPrecipitation1 = view.findViewById(R.id.forecast_precipitation_1_text_view);
        forecastPrecipitation2 = view.findViewById(R.id.forecast_precipitation_2_text_view);
        forecastPrecipitation3 = view.findViewById(R.id.forecast_precipitation_3_text_view);
        forecastPrecipitation4 = view.findViewById(R.id.forecast_precipitation_4_text_view);
        forecastPrecipitation5 = view.findViewById(R.id.forecast_precipitation_5_text_view);
        forecastPrecipitation6 = view.findViewById(R.id.forecast_precipitation_6_text_view);
        forecastPrecipitation7 = view.findViewById(R.id.forecast_precipitation_7_text_view);

        forecastHigh1 = view.findViewById(R.id.forecast_high_1_text_view);
        forecastHigh2 = view.findViewById(R.id.forecast_high_2_text_view);
        forecastHigh3 = view.findViewById(R.id.forecast_high_3_text_view);
        forecastHigh4 = view.findViewById(R.id.forecast_high_4_text_view);
        forecastHigh5 = view.findViewById(R.id.forecast_high_5_text_view);
        forecastHigh6 = view.findViewById(R.id.forecast_high_6_text_view);
        forecastHigh7 = view.findViewById(R.id.forecast_high_7_text_view);

        forecastLow1 = view.findViewById(R.id.forecast_low_1_text_view);
        forecastLow2 = view.findViewById(R.id.forecast_low_2_text_view);
        forecastLow3 = view.findViewById(R.id.forecast_low_3_text_view);
        forecastLow4 = view.findViewById(R.id.forecast_low_4_text_view);
        forecastLow5 = view.findViewById(R.id.forecast_low_5_text_view);
        forecastLow6 = view.findViewById(R.id.forecast_low_6_text_view);
        forecastLow7 = view.findViewById(R.id.forecast_low_7_text_view);

        icon1 = view.findViewById(R.id.imageView_1_image_view);
        icon2 = view.findViewById(R.id.imageView_2_image_view);
        icon3 = view.findViewById(R.id.imageView_3_image_view);
        icon4 = view.findViewById(R.id.imageView_4_image_view);
        icon5 = view.findViewById(R.id.imageView_5_image_view);
        icon6 = view.findViewById(R.id.imageView_6_image_view);
        icon7 = view.findViewById(R.id.imageView_7_image_view);

        nextDayDescription1 = view.findViewById(R.id.next_day_1_description_text_view);
        nextDayDescription2 = view.findViewById(R.id.next_day_2_description_text_view);
        nextDayDescription3 = view.findViewById(R.id.next_day_3_description_text_view);
        nextDayDescription4 = view.findViewById(R.id.next_day_4_description_text_view);
        nextDayDescription5 = view.findViewById(R.id.next_day_5_description_text_view);
        nextDayDescription6 = view.findViewById(R.id.next_day_6_description_text_view);
        nextDayDescription7 = view.findViewById(R.id.next_day_7_description_text_view);

        nextDayDate1 = view.findViewById(R.id.next_day_1_date_text_view);
        nextDayDate2 = view.findViewById(R.id.next_day_2_date_text_view);
        nextDayDate3 = view.findViewById(R.id.next_day_3_date_text_view);
        nextDayDate4 = view.findViewById(R.id.next_day_4_date_text_view);
        nextDayDate5 = view.findViewById(R.id.next_day_5_date_text_view);
        nextDayDate6 = view.findViewById(R.id.next_day_6_date_text_view);
        nextDayDate7 = view.findViewById(R.id.next_day_7_date_text_view);

        nextDayName1 = view.findViewById(R.id.next_day_1_day_text_view);
        nextDayName2 = view.findViewById(R.id.next_day_2_day_text_view);
        nextDayName3 = view.findViewById(R.id.next_day_3_day_text_view);
        nextDayName4 = view.findViewById(R.id.next_day_4_day_text_view);
        nextDayName5 = view.findViewById(R.id.next_day_5_day_text_view);
        nextDayName6 = view.findViewById(R.id.next_day_6_day_text_view);
        nextDayName7 = view.findViewById(R.id.next_day_7_day_text_view);



        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Log.e("FusedLocation", String.valueOf(location.getLatitude()));
                        Log.e("FusedLocation", String.valueOf(location.getLongitude()));
                        CurrentWeatherForecast cwi = new CurrentWeatherForecast();
                        cwi.execute(location.getLatitude() + "," + location.getLongitude());
                    }
                }
            });
        }
        return view;

    }
    class CurrentWeatherForecast extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... strings) {
            try{
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("JSONObject", Context.MODE_PRIVATE);
                String weatherString = sharedPreferences.getString("JSONObject", "None");
                JSONObject weatherObject = new JSONObject(weatherString);
                Log.e(TAG, "JSONObject: " + weatherObject.toString());
                setCurrentForecastUI(weatherObject);
            } catch(Exception e){
                Log.e(TAG, "WeatherInfo; JSONObject", e);
            }
            return null;
        }

    }

    private void setCurrentForecastUI(JSONObject myResponse) throws Exception {
        JSONArray data = myResponse.getJSONObject("daily").getJSONArray("data");
        final double high1 = Double.parseDouble(data.getJSONObject(1).getString("temperatureHigh"));
        final double high2 = Double.parseDouble(data.getJSONObject(2).getString("temperatureHigh"));
        final double high3 = Double.parseDouble(data.getJSONObject(3).getString("temperatureHigh"));
        final double high4 = Double.parseDouble(data.getJSONObject(4).getString("temperatureHigh"));
        final double high5 = Double.parseDouble(data.getJSONObject(5).getString("temperatureHigh"));
        final double high6 = Double.parseDouble(data.getJSONObject(6).getString("temperatureHigh"));
        final double high7 = Double.parseDouble(data.getJSONObject(7).getString("temperatureHigh"));

        final double low1 = Double.parseDouble(data.getJSONObject(1).getString("temperatureLow"));
        final double low2 = Double.parseDouble(data.getJSONObject(2).getString("temperatureLow"));
        final double low3 = Double.parseDouble(data.getJSONObject(3).getString("temperatureLow"));
        final double low4 = Double.parseDouble(data.getJSONObject(4).getString("temperatureLow"));
        final double low5 = Double.parseDouble(data.getJSONObject(5).getString("temperatureLow"));
        final double low6 = Double.parseDouble(data.getJSONObject(6).getString("temperatureLow"));
        final double low7 = Double.parseDouble(data.getJSONObject(7).getString("temperatureLow"));

        final double prec1 = Double.parseDouble(data.getJSONObject(1).getString("precipProbability"));
        final double prec2 = Double.parseDouble(data.getJSONObject(2).getString("precipProbability"));
        final double prec3 = Double.parseDouble(data.getJSONObject(3).getString("precipProbability"));
        final double prec4 = Double.parseDouble(data.getJSONObject(4).getString("precipProbability"));
        final double prec5 = Double.parseDouble(data.getJSONObject(5).getString("precipProbability"));
        final double prec6 = Double.parseDouble(data.getJSONObject(6).getString("precipProbability"));
        final double prec7 = Double.parseDouble(data.getJSONObject(7).getString("precipProbability"));

        final String icon_1 = data.getJSONObject(1).getString("icon");
        final String icon_2 = data.getJSONObject(2).getString("icon");
        final String icon_3 = data.getJSONObject(3).getString("icon");
        final String icon_4 = data.getJSONObject(4).getString("icon");
        final String icon_5 = data.getJSONObject(5).getString("icon");
        final String icon_6 = data.getJSONObject(6).getString("icon");
        final String icon_7 = data.getJSONObject(7).getString("icon");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        Calendar c = Calendar.getInstance();
        Date currentDate = new Date();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 1);
        final String date1 = dateFormat.format(c.getTime());
        final String day1 = "Tomorrow";
        c.add(Calendar.DATE, 1);
        final String date2 = dateFormat.format(c.getTime());
        final String day2 = dayFormat.format(c.getTime());
        c.add(Calendar.DATE, 1);
        final String date3 = dateFormat.format(c.getTime());
        final String day3 = dayFormat.format(c.getTime());
        c.add(Calendar.DATE, 1);
        final String date4 = dateFormat.format(c.getTime());
        final String day4 = dayFormat.format(c.getTime());
        c.add(Calendar.DATE, 1);
        final String date5 = dateFormat.format(c.getTime());
        final String day5 = dayFormat.format(c.getTime());
        c.add(Calendar.DATE, 1);
        final String date6 = dateFormat.format(c.getTime());
        final String day6 = dayFormat.format(c.getTime());
        c.add(Calendar.DATE, 1);
        final String date7 = dateFormat.format(c.getTime());
        final String day7 = dayFormat.format(c.getTime());

        final String description1 = data.getJSONObject(1).getString("summary");
        final String description2 = data.getJSONObject(2).getString("summary");
        final String description3 = data.getJSONObject(3).getString("summary");
        final String description4 = data.getJSONObject(4).getString("summary");
        final String description5 = data.getJSONObject(5).getString("summary");
        final String description6 = data.getJSONObject(6).getString("summary");
        final String description7 = data.getJSONObject(7).getString("summary");

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String highString1 = (int) high1 + "°";
                forecastHigh1.setText(highString1);
                String highString2 = (int) high2 + "°";
                forecastHigh2.setText(highString2);
                String highString3 = (int) high3 + "°";
                forecastHigh3.setText(highString3);
                String highString4 = (int) high4 + "°";
                forecastHigh4.setText(highString4);
                String highString5 = (int) high5 + "°";
                forecastHigh5.setText(highString5);
                String highString6 = (int) high6 + "°";
                forecastHigh6.setText(highString6);
                String highString7 = (int) high7 + "°";
                forecastHigh7.setText(highString7);

                String lowString1 = (int) low1 + "°";
                forecastLow1.setText(lowString1);
                String lowString2 = (int) low2 + "°";
                forecastLow2.setText(lowString2);
                String lowString3 = (int) low3 + "°";
                forecastLow3.setText(lowString3);
                String lowString4 = (int) low4 + "°";
                forecastLow4.setText(lowString4);
                String lowString5 = (int) low5 + "°";
                forecastLow5.setText(lowString5);
                String lowString6 = (int) low6 + "°";
                forecastLow6.setText(lowString6);
                String lowString7 = (int) low7 + "°";
                forecastLow7.setText(lowString7);

                String precipString1 = (int)(prec1*100) + "%";
                forecastPrecipitation1.setText(precipString1);
                String precipString2 = (int)(prec2*100) + "%";
                forecastPrecipitation2.setText(precipString2);
                String precipString3 = (int)(prec3*100) + "%";
                forecastPrecipitation3.setText(precipString3);
                String precipString4 = (int)(prec4*100) + "%";
                forecastPrecipitation4.setText(precipString4);
                String precipString5 = (int)(prec5*100) + "%";
                forecastPrecipitation5.setText(precipString5);
                String precipString6 = (int)(prec6*100) + "%";
                forecastPrecipitation6.setText(precipString6);
                String precipString7 = (int)(prec7*100) + "%";
                forecastPrecipitation7.setText(precipString7);

                nextDayDate1.setText(date1);
                nextDayDate2.setText(date2);
                nextDayDate3.setText(date3);
                nextDayDate4.setText(date4);
                nextDayDate5.setText(date5);
                nextDayDate6.setText(date6);
                nextDayDate7.setText(date7);

                nextDayName1.setText(day1);
                nextDayName2.setText(day2);
                nextDayName3.setText(day3);
                nextDayName4.setText(day4);
                nextDayName5.setText(day5);
                nextDayName6.setText(day6);
                nextDayName7.setText(day7);


                setIcon(icon_1, icon1);
                setIcon(icon_2, icon2);
                setIcon(icon_3, icon3);
                setIcon(icon_4, icon4);
                setIcon(icon_5, icon5);
                setIcon(icon_6, icon6);
                setIcon(icon_7, icon7);

                String des1 = description1.replaceAll("throughout the day.", "");
                nextDayDescription1.setText(des1);
                String des2 = description2.replaceAll("throughout the day.", "");
                nextDayDescription2.setText(des1);
                String des3 = description3.replaceAll("throughout the day.", "");
                nextDayDescription3.setText(des1);
                String des4 = description4.replaceAll("throughout the day.", "");
                nextDayDescription4.setText(des1);
                String des5 = description5.replaceAll("throughout the day.", "");
                nextDayDescription5.setText(des1);
                String des6 = description6.replaceAll("throughout the day.", "");
                nextDayDescription6.setText(des1);
                String des7 = description7.replaceAll("throughout the day.", "");
                nextDayDescription7.setText(des1);

            }
        });
        progressDialog.dismiss();

    }

    private void setIcon(String iconType, ImageView currView) {
        if (iconType.contains("clear"))
            currView.setImageResource(R.drawable.weather_mostly_sunny_black);
        else if (iconType.contains("rain"))
            currView.setImageResource(R.drawable.weather_light_showers_black);
        else if (iconType.contains("snow"))
            currView.setImageResource(R.drawable.weather_snow_black);
        else if (iconType.contains("cloudy"))
            currView.setImageResource(R.drawable.weather_partly_cloudy_black);
        else if (iconType.contains("thunder"))
            currView.setImageResource(R.drawable.weather_thunderstorm_black);
        else if (iconType.contains("wind"))
            currView.setImageResource(R.drawable.weather_windy_black);
        else if (iconType.contains("fog"))
            currView.setImageResource(R.drawable.weather_fog_black);
        else if (iconType.contains("sleet"))
            currView.setImageResource(R.drawable.weather_sleet_black);
        else if (iconType.contains("hail"))
            currView.setImageResource(R.drawable.weather_hail_black);
        else
            currView.setImageResource(R.drawable.weather_partly_cloudy_black);
    }
}
