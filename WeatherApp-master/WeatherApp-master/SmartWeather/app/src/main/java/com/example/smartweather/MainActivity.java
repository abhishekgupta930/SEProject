package com.example.smartweather;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.ViewPager;

import com.example.smartweather.ui.main.SectionsPagerAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private static JSONObject weatherObject;
    private String receivedLocation, query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        Intent intent = getIntent();
        receivedLocation = intent.getStringExtra("coordinates");
        query = intent.getStringExtra("query");

        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        ImageView navBarButtonImageView = findViewById(R.id.navbar_button);
        navBarButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_settings:
                        Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_view_history:
                        Toast.makeText(MainActivity.this, "View History", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

        EditText searchEditText = findViewById(R.id.search_edit_text);
        if (receivedLocation != null)
            searchEditText.setHint(query);
        searchEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.e("mainActivity", v.getText().toString());
                    GeocodeAsyncTask gat = new GeocodeAsyncTask(MainActivity.this);
                    gat.execute(v.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    public String getReceivedLocation() {
        return receivedLocation;
    }

    public String getQuery() {
        return query;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 1: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                } else {
//                }
//                return;
//            }
//        }
//    }

//    public StringLocation getLastLocation() {
//        final StringLocation stringLocation = new StringLocation();
//        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//                Location location = task.getResult();
//                if (location != null) {
//                    stringLocation.setLatitude(String.valueOf(location.getLatitude()));
//                    stringLocation.setLongitude(String.valueOf(location.getLongitude()));
//                }
//            }
//        });
//        Log.e("FusedLocation", "" + stringLocation.getLatitude());
//        Log.e("FusedLocation", "" + stringLocation.getLongitude());
//        return stringLocation;
//    }

    class CurrentWeatherInfo extends AsyncTask<String, Void, JSONObject>{
        private JSONObject fetchWeather(String coordinates) throws Exception {
            Log.e("CurrentWeatherInfo", "Coordinates " + coordinates);
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

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                weatherObject = fetchWeather(strings[0]);
                Log.e("mainActivity", "JSONObject: " + weatherObject.toString());
            } catch (Exception e) {
                Log.e("mainActivity", "WeatherInfo; JSONObject", e);
            }
            return null;
        }
    }
}

class GeocodeAsyncTask extends AsyncTask<String, Void, String> {

    private Context context;

    public GeocodeAsyncTask(Context context) {
        this.context = context;
    }

    private static String getLocationCoordinates(String locationIndicator) throws Exception {
        String url, inputLine;
        URL obj;
        HttpURLConnection con;
        StringBuffer response = new StringBuffer();
        BufferedReader in;
        String location = "";
        url = "https://api.opencagedata.com/geocode/v1/json?q="+locationIndicator+"&key=aa6078cd33c248e9ae8a306ea4af0159";
        //System.out.println(url);
        obj = new URL(url);
        con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONObject myResponse = new JSONObject(response.toString());
        JSONArray myArr = myResponse.getJSONArray("results");
        String lat = myArr.getJSONObject(0).getJSONObject("geometry").getString("lat");
        String lng = myArr.getJSONObject(0).getJSONObject("geometry").getString("lng");
        location = lat + "," + lng;
        return location;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String coordinates = getLocationCoordinates(strings[0]);
            Log.e("mainActivity", coordinates);
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("coordinates", coordinates);
            intent.putExtra("query", strings[0]);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("mainActivity", "GeocodeAsync", e);
        }
        return null;
    }
}

class StringLocation {
    String latitude, longitude;

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}