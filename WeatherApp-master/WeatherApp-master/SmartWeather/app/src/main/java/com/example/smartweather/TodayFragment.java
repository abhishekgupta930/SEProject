package com.example.smartweather;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "TodayFragment";
    private static JSONObject weatherObject;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean isNewLocation = false;

    private ScrollView parentScrollView;
    private TextView currDateTimeTextView;
    private TextView currHighLowTempTextView;
    private TextView currTempTextView;
    private TextView currFeelsLikeTempTextView;
    private TextView currOutlookTextView;
    private ImageView currWeatherImageView;
    private ImageView downChevronImageView;
    private TextView advisoryTitleTextView;
    private TextView currentPrecipitationTextView;
    private ProgressDialog progressDialog;

    private TextView detailValueTextView1;
    private TextView detailValueTextView2;
    private TextView detailValueTextView3;
    private TextView detailValueTextView4;
    private TextView detailValueTextView5;
    private TextView detailValueTextView6;
    private TextView detailValueTextView7;

    private TextView advisoryContentTextView;

    public TodayFragment() {
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
    public static TodayFragment newInstance(String param1, String param2) {
        TodayFragment fragment = new TodayFragment();
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
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Getting latest info");
        progressDialog.show();

        parentScrollView = view.findViewById(R.id.parent_scroll_view);
        currDateTimeTextView = view.findViewById(R.id.current_date_time_text_view);
        currHighLowTempTextView = view.findViewById(R.id.temp_high_low_text_view);
        currTempTextView = view.findViewById(R.id.curr_temp_text_view);
        currFeelsLikeTempTextView = view.findViewById(R.id.curr_feels_like_temp_text_view);
        currOutlookTextView = view.findViewById(R.id.curr_outlook_text_view);
        currWeatherImageView = view.findViewById(R.id.curr_weather_image_view);
        downChevronImageView = view.findViewById(R.id.down_chevron_image_view);
        advisoryTitleTextView = view.findViewById(R.id.advisory_title_text_view);
        currentPrecipitationTextView = view.findViewById(R.id.precipitation_text_view);

        detailValueTextView1 = view.findViewById(R.id.detail_value_1_text_view);
        detailValueTextView2 = view.findViewById(R.id.detail_value_2_text_view);
        detailValueTextView3 = view.findViewById(R.id.detail_value_3_text_view);
        detailValueTextView4 = view.findViewById(R.id.detail_value_4_text_view);
        detailValueTextView5 = view.findViewById(R.id.detail_value_5_text_view);
        detailValueTextView6 = view.findViewById(R.id.detail_value_6_text_view);
        detailValueTextView7 = view.findViewById(R.id.detail_value_7_text_view);

        advisoryContentTextView = view.findViewById(R.id.advisory_content_text_view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int[] location = new int[2];
        advisoryTitleTextView.getLocationOnScreen(location);

        int distance = displayMetrics.heightPixels - location[1] - 1184;
        Log.e(TAG, "distance = " + distance);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) advisoryTitleTextView.getLayoutParams();
        layoutParams.setMargins(0, distance, 0, 0);
        advisoryTitleTextView.setLayoutParams(layoutParams);

        Animation bounceAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce_animation);
        downChevronImageView.startAnimation(bounceAnimation);

        MainActivity activity = (MainActivity) getActivity();
        String receivedLocation = activity.getReceivedLocation();
        if (receivedLocation != null) {
            isNewLocation = true;
            Log.e(TAG, "ReceivedLocation: " + receivedLocation);
            CurrentWeatherInfo cwi = new CurrentWeatherInfo();
            cwi.execute(receivedLocation);
            String query = activity.getQuery();
            advisoryContentTextView.setText("No advisory for " + query);
        } else {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.e(TAG, "FusedLocation lat: " + String.valueOf(location.getLatitude()));
                            Log.e(TAG, "FusedLocation long: " + String.valueOf(location.getLongitude()));
//                            Log.e(TAG, "LcoationTime: " + location.getTime());
                            CurrentWeatherInfo cwi = new CurrentWeatherInfo();
                            cwi.execute(location.getLatitude() + "," + location.getLongitude());
                        }
                    }
                });
            }
        }
        return view;
    }

    class CurrentWeatherInfo extends AsyncTask<String, Void, JSONObject> {
        private JSONObject fetchWeather(String coordinates) throws Exception {
            String url, inputLine;
            URL obj;
            HttpURLConnection con;
            StringBuffer response = new StringBuffer();
            BufferedReader in;

            url = "https://api.darksky.net/forecast/1ff53cf31359609672bb0aa29a0e6ea5/" + coordinates
                    + "?exclude=minutely,flags";

            System.out.println(url);
            obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // System.out.println(response.toString());
            JSONObject myResponse = new JSONObject(response.toString());
            return myResponse;
        }

        private Date fetchTime(Double latitude, Double longitude) throws Exception {
            String url, inputLine;
            URL obj;
            HttpURLConnection con;
            StringBuffer response = new StringBuffer();
            BufferedReader in;

            url = "https://api.timezonedb.com/v2.1/get-time-zone?key=O5ZOSGJ5MTTB&format=json&by=position&lat=" + latitude + "&lng=" + longitude;

            System.out.println(url);
            obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // System.out.println(response.toString());
            JSONObject myResponse = new JSONObject(response.toString());
            Log.e(TAG, "JSON TIME: " + myResponse.toString());
            String formattedDate = myResponse.getString("formatted");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfTo = new SimpleDateFormat("E, dd MMM yyyy, h:mm");
            return sdfFrom.parse(formattedDate);
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                weatherObject = fetchWeather(strings[0]);

                int commaIndex = strings[0].indexOf(',');
                Date date = null;
                if (isNewLocation) {
                    Double latitude = Double.parseDouble(strings[0].substring(0, commaIndex));
                    Double longitude = Double.parseDouble(strings[0].substring(commaIndex + 1, strings[0].length()));
                    date = fetchTime(latitude, longitude);
                }

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("JSONObject", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("JSONObject", weatherObject.toString());
                editor.commit();
                Log.e(TAG, "SharedPreferences committed");
                Log.e(TAG, "JSONObject: " + weatherObject.toString());
                setCurrentWeatherUI(weatherObject, date);
            } catch (Exception e) {
                Log.e(TAG, "WeatherInfo; JSONObject", e);
            }
            return null;
        }
    }

    private void setCurrentWeatherUI(JSONObject myResponse, Date newDate) throws Exception {
        final String summary = myResponse.getJSONObject("currently").getString("summary");
        final String icon = myResponse.getJSONObject("currently").getString("icon");
        final double temperature = Double.parseDouble(myResponse.getJSONObject("currently").getString("temperature"));
        final double apparentTemperature = Double.parseDouble(myResponse.getJSONObject("currently").getString("apparentTemperature"));
        final String dewPoint = myResponse.getJSONObject("currently").getString("dewPoint");
        final String humidity = myResponse.getJSONObject("currently").getString("humidity");
        final String pressure = myResponse.getJSONObject("currently").getString("pressure");
        final String windSpeed = myResponse.getJSONObject("currently").getString("windSpeed");
        final String cloudCover = myResponse.getJSONObject("currently").getString("cloudCover");
        final String uvIndex = myResponse.getJSONObject("currently").getString("uvIndex");
        final String visibility = myResponse.getJSONObject("currently").getString("visibility");

        JSONArray arr = myResponse.getJSONObject("daily").getJSONArray("data");
        final double temperatureMin = Double.parseDouble(arr.getJSONObject(0).getString("temperatureMin"));
        final double temperatureMax = Double.parseDouble(arr.getJSONObject(0).getString("temperatureMax"));
        final String precipitationProbability = myResponse.getJSONObject("currently").getString("precipProbability");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy, h:mm aa");
        Date today;
        if (newDate == null)
            today = Calendar.getInstance().getTime();
        else
            today = newDate;

        final String date = dateFormat.format(today);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String tempString = (int) temperature + "°";
                currTempTextView.setText(tempString);
                String appTempString = "Feels like " + ((int) apparentTemperature) + "°";
                currFeelsLikeTempTextView.setText(appTempString);
                currOutlookTextView.setText(summary);
                String minMaxTempString = "Day " + (int) temperatureMax + "°↑ | " + "Night " + (int) temperatureMin + "°↓";
                currHighLowTempTextView.setText(minMaxTempString);
                currDateTimeTextView.setText(date);
                String precipChanceString = "Chance of precipitation: " + precipitationProbability + "%";
                currentPrecipitationTextView.setText(precipChanceString);

                setDetailsTab(dewPoint, humidity, pressure, windSpeed, cloudCover, uvIndex, visibility);
                setIcon(icon);
            }
        });
        progressDialog.dismiss();
    }

    private void setDetailsTab(String dewPoint, String humidity, String pressure, String windSpeed, String cloudCover, String uvIndex, String visibility) {
        String dewPointString = dewPoint + "°";
        detailValueTextView1.setText(dewPointString);
        String humidityString = humidity + "%";
        detailValueTextView2.setText(humidityString);
        String pressureString = pressure + " hPa";
        detailValueTextView3.setText(pressureString);
        String windSpeedString = windSpeed + " mph";
        detailValueTextView4.setText(windSpeedString);
        String cloudCoverString = cloudCover + " okta";
        detailValueTextView5.setText(cloudCoverString);
        detailValueTextView6.setText(uvIndex);
        String visibilityString = visibility + " miles";
        detailValueTextView7.setText(visibilityString);
    }

    private void setIcon(String iconType) {
        if (iconType.contains("clear"))
            currWeatherImageView.setImageResource(R.drawable.weather_mostly_sunny_white);
        else if (iconType.contains("rain"))
            currWeatherImageView.setImageResource(R.drawable.weather_light_showers_white);
        else if (iconType.contains("snow"))
            currWeatherImageView.setImageResource(R.drawable.weather_snow_white);
        else if (iconType.contains("cloudy"))
            currWeatherImageView.setImageResource(R.drawable.weather_partly_cloudy_white);
        else if (iconType.contains("thunder"))
            currWeatherImageView.setImageResource(R.drawable.weather_thunderstorm_white);
        else if (iconType.contains("wind"))
            currWeatherImageView.setImageResource(R.drawable.weather_windy_white);
        else if (iconType.contains("fog"))
            currWeatherImageView.setImageResource(R.drawable.weather_fog_white);
        else if (iconType.contains("sleet"))
            currWeatherImageView.setImageResource(R.drawable.weather_sleet_white);
        else if (iconType.contains("hail"))
            currWeatherImageView.setImageResource(R.drawable.weather_hail_white);
        else
            currWeatherImageView.setImageResource(R.drawable.weather_partly_cloudy_white);
    }
}
