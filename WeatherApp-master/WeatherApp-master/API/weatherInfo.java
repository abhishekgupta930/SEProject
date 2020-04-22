import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import org.json.*;
import java.text.*;

public class weatherInfo {

    private static JSONObject weatherObject;

    private static JSONObject fetchWeather(String coordinates, String time) throws Exception {
        String url, inputLine;
        URL obj;
        HttpURLConnection con;
        StringBuffer response = new StringBuffer();
        BufferedReader in;
        url = "https://api.darksky.net/forecast/1ff53cf31359609672bb0aa29a0e6ea5/" + coordinates + "," + time
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

    private static JSONObject fetchWeather(String coordinates) throws Exception {
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

    public static void main(String args[]) throws Exception {
        String location;
        location = getLocationCoordinates("Raleigh");
        System.out.println(location);
        location = getLocationCoordinates("Doha");
        System.out.println(location);
        location = getLocationCoordinates("Santa+Clara");
        System.out.println(location);
        location = getLocationCoordinates("27606");
        System.out.println(location);

        System.out.println("Querying the weather");
        //weatherObject = getLatestWeatherAt("Raleigh");
        weatherObject = getTimedWeatherAt("Raleigh","2020-04-13T10:00:00");

        System.out.println("==========CURRENTLY=========");
        getCurrent(getLastWeather());
        System.out.println("==========ALERTS=========");
        System.out.println(checkAlert());
        System.out.println("==========WEATHER STRING=========");
        System.out.println(getWeatherSting());
        System.out.println("==========HOURLY=========");
        getHourly(weatherObject);
        System.out.println("==========DAILY=========");
        getDaily(weatherObject);

        //JSONObject weatherObject = fetchWeather(2020-04-13T10:00:00);
    }

    private static String getLocationCoordinates(String locationIndicator) throws Exception
    {
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
        float lat = myArr.getJSONObject(0).getJSONObject("geometry").getFloat("lat");
        float lng = myArr.getJSONObject(0).getJSONObject("geometry").getFloat("lng");
        location= String.valueOf(lat)+","+ String.valueOf(lng);
        return location;
    }

    private static void getCurrent(JSONObject myResponse) throws Exception
    {
        String summary, icon;
        float temperature, apparentTemperature, dewPoint, humidity, pressure, windSpeed, cloudCover, uvIndex, visibility;
        summary = myResponse.getJSONObject("currently").getString("summary");
        icon = myResponse.getJSONObject("currently").getString("icon");
        temperature = myResponse.getJSONObject("currently").getFloat("temperature");
        apparentTemperature = myResponse.getJSONObject("currently").getFloat("apparentTemperature");
        dewPoint = myResponse.getJSONObject("currently").getFloat("dewPoint");
        humidity = myResponse.getJSONObject("currently").getFloat("humidity");
        pressure = myResponse.getJSONObject("currently").getFloat("pressure");
        windSpeed = myResponse.getJSONObject("currently").getFloat("windSpeed");
        cloudCover = myResponse.getJSONObject("currently").getFloat("cloudCover");
        uvIndex = myResponse.getJSONObject("currently").getFloat("uvIndex");
        visibility = myResponse.getJSONObject("currently").getFloat("visibility"); 
        System.out.println(summary+","+icon+","+temperature+","+apparentTemperature+","+dewPoint+","+humidity+","+pressure+","+windSpeed+","+cloudCover+","+uvIndex+","+visibility);
    }

    private static void getHourly(JSONObject myResponse)
    {
        String summary, icon;
        float temperature, apparentTemperature, dewPoint, humidity, pressure, windSpeed, cloudCover, uvIndex, visibility;
        JSONArray arr = myResponse.getJSONObject("hourly").getJSONArray("data");
        for (int i = 0; i < arr.length(); i++) {
            summary = arr.getJSONObject(i).getString("summary");
            icon = arr.getJSONObject(i).getString("icon");
            temperature = arr.getJSONObject(i).getFloat("temperature");
            apparentTemperature = arr.getJSONObject(i).getFloat("apparentTemperature");
            dewPoint = arr.getJSONObject(i).getFloat("dewPoint");
            humidity = arr.getJSONObject(i).getFloat("humidity");
            pressure = arr.getJSONObject(i).getFloat("pressure");
            windSpeed = arr.getJSONObject(i).getFloat("windSpeed");
            cloudCover = arr.getJSONObject(i).getFloat("cloudCover");
            uvIndex = arr.getJSONObject(i).getFloat("uvIndex");
            visibility = arr.getJSONObject(i).getFloat("visibility");
            System.out.println(summary+","+icon+","+temperature+","+apparentTemperature+","+dewPoint+","+humidity+","+pressure+","+windSpeed+","+cloudCover+","+uvIndex+","+visibility);
       }
   
    }

    private static void getDaily(JSONObject myResponse)
    {
        String summary, icon, sunrise, sunset;
        float temperatureMin, temperatureMax, apparentTemperatureMin, apparentTemperatureMax, dewPoint, humidity, pressure, windSpeed, cloudCover, uvIndex, visibility;
        int sunriseTime, sunsetTime;
        JSONArray arr = myResponse.getJSONObject("daily").getJSONArray("data");
        for (int i = 0; i < arr.length(); i++) {
            summary = arr.getJSONObject(i).getString("summary");
            icon = arr.getJSONObject(i).getString("icon");
            temperatureMin = arr.getJSONObject(i).getFloat("temperatureMin");
            temperatureMax = arr.getJSONObject(i).getFloat("temperatureMax");
            apparentTemperatureMin = arr.getJSONObject(i).getFloat("apparentTemperatureMin");
            apparentTemperatureMax = arr.getJSONObject(i).getFloat("apparentTemperatureMax");
            dewPoint = arr.getJSONObject(i).getFloat("dewPoint");
            humidity = arr.getJSONObject(i).getFloat("humidity");
            pressure = arr.getJSONObject(i).getFloat("pressure");
            windSpeed = arr.getJSONObject(i).getFloat("windSpeed");
            cloudCover = arr.getJSONObject(i).getFloat("cloudCover");
            uvIndex = arr.getJSONObject(i).getFloat("uvIndex");
            visibility = arr.getJSONObject(i).getFloat("visibility");
            sunriseTime = arr.getJSONObject(i).getInt("sunriseTime");
            sunsetTime = arr.getJSONObject(i).getInt("sunsetTime");
            Date sunrise_time = new Date(Long.parseLong(Integer.toString(sunriseTime)) * 1000);
            Date sunset_time = new Date(Long.parseLong(Integer.toString(sunsetTime)) * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sunrise = sdf.format(sunrise_time);
            sunset = sdf.format(sunset_time);
            System.out.println(summary+","+icon+","+temperatureMin+","+temperatureMax+","+apparentTemperatureMin+","+apparentTemperatureMax+","+sunrise+","+sunset+","+dewPoint+","+humidity+","+pressure+","+windSpeed+","+cloudCover+","+uvIndex+","+visibility);
       }

    }
    
    
    public static JSONObject getLastWeather() throws Exception
    {
        return weatherObject;
    }

    public static JSONObject getLatestWeatherAt(String location) throws Exception
    {
        String coordinates = getLocationCoordinates(location);
        return fetchWeather(coordinates);
    }

    public static JSONObject getTimedWeatherAt(String location,String time) throws Exception
    {
        String coordinates = getLocationCoordinates(location);
        return fetchWeather(coordinates,time);
    }

    public static boolean checkAlert() 
    {
        return weatherObject.has("alerts");    
    }

    public static JSONObject getAlert() 
    {
        return weatherObject.getJSONObject("alerts");
    }

    public static String getWeatherSting()
    {
        return weatherObject.getJSONObject("currently").getString("summary");
    }
    public static String getWeatherIconSting()
    {
        return weatherObject.getJSONObject("currently").getString("icon");
    }




    
}