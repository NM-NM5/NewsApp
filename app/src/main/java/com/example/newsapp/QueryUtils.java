package com.example.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private static final String LOCATION_SEPARATOR = "T";
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Event> fetchNewsData(String requestUrl1, String requestUrl2) {
        URL url1 = createUrl(requestUrl1);
        URL url2 = createUrl(requestUrl2);

        String jsonResponse1 = null;
        String jsonResponse2 = null;
        try {
            jsonResponse1 = makeHttpRequest(url1);
            jsonResponse2 = makeHttpRequest(url2);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Event> news = extractEvents(jsonResponse1, jsonResponse2);
        return news;
    }

    public static List<Event> extractEvents(String eventJSON1, String eventJSON2) {

        // verify that there's data in one link at list
        if (TextUtils.isEmpty(eventJSON1) && TextUtils.isEmpty(eventJSON2)) {
            return null;
        }

        List<Event> news = new ArrayList<>();
        try {

            //getting results array from first link
            JSONObject baseJsonResponse1 = new JSONObject(eventJSON1);
            JSONObject mainObject1 = baseJsonResponse1.getJSONObject("response");
            JSONArray eventArray1 = mainObject1.getJSONArray("results");
            //getting results array from second link
            JSONObject baseJsonResponse2 = new JSONObject(eventJSON2);
            JSONObject mainObject2 = baseJsonResponse2.getJSONObject("response");
            JSONArray eventArray2 = mainObject2.getJSONArray("results");

            // reading required data from array of first link
            for (int i = 0; i < eventArray1.length(); i++) {
                JSONObject currentEvent = eventArray1.getJSONObject(i);

                //getting url
                String webUrl = currentEvent.getString("webUrl");
                //getting title
                String webTitle = currentEvent.getString("webTitle");
                //getting sectionName
                String sectionName = currentEvent.getString("sectionName");
                //getting author name
                String author = "The Guardian";
                JSONArray tagsArray = currentEvent.getJSONArray("tags");
                if (tagsArray.length() != 0) {
                    JSONObject tagsObject = tagsArray.getJSONObject(0);
                    author = tagsObject.getString("webTitle");
                }
                //getting webPublicationDate
                String webPublicationDate = currentEvent.getString("webPublicationDate");
                String date = "0000 - 00 - 00";
                String time = "00:00:00";
                if (webPublicationDate.contains(LOCATION_SEPARATOR)) {
                    String[] parts = webPublicationDate.split(LOCATION_SEPARATOR);
                    date = parts[0];
                    time = parts[1].substring(0, parts[1].length() - 1);
                }

                // Make Quake Of the Strings And Assigning it to earthquakes ArrayList<Quake>
                Event quake = new Event(sectionName, webTitle, date, time, webUrl, author);
                news.add(quake);
            }
            // reading required data from array of first link
            for (int i = 0; i < eventArray2.length(); i++) {
                JSONObject currentEvent = eventArray2.getJSONObject(i);

                //getting url
                String webUrl = currentEvent.getString("webUrl");
                //getting title
                String webTitle = currentEvent.getString("webTitle");
                //getting sectionName
                String sectionName = currentEvent.getString("sectionName");
                //getting author name
                String author = "The Guardian";
                JSONArray tagsArray = currentEvent.getJSONArray("tags");
                if (tagsArray.length() != 0) {
                    JSONObject tagsObject = tagsArray.getJSONObject(0);
                    author = tagsObject.getString("webTitle");
                }
                //getting webPublicationDate
                String webPublicationDate = currentEvent.getString("webPublicationDate");
                String date = "0000 - 00 - 00";
                String time = "00:00:00";
                if (webPublicationDate.contains(LOCATION_SEPARATOR)) {
                    String[] parts = webPublicationDate.split(LOCATION_SEPARATOR);
                    date = parts[0];
                    time = parts[1].substring(0, parts[1].length() - 1);
                }

                // Make Quake Of the Strings And Assigning it to earthquakes ArrayList<Quake>
                Event quake = new Event(sectionName, webTitle, date, time, webUrl, author);
                news.add(quake);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }
        // Return the list of earthquakes
        return news;
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
