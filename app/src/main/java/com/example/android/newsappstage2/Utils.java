package com.example.android.newsappstage2;


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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Utils {


    private Utils() {
    }

    //Return URL object from string URL.
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("utils", "Cannot create URL ", e);
        }
        return url;
    }


    //Make an HTTP request to URL and handle response accordingly
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
                Log.e("Utils", "response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("Utils", "IOException thrown", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //Convert InputStream response to String
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    //parse JSON response into article object items
    private static List<Article> extractNewsItems(String jsonData) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonData)) {
            return null;
        }

        // Create an empty ArrayList for articles
        List<Article> articlesList = new ArrayList<>();

        // Create JSONObject
        try {
            JSONObject jsonRootObject = new JSONObject(jsonData);
            JSONObject response = jsonRootObject.getJSONObject("response");

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = response.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject newsItem = jsonArray.getJSONObject(i);

                String title = newsItem.getString("webTitle");
                JSONObject published = newsItem.getJSONObject("fields");
                String author = published.getString("byline");
                String section = newsItem.getString("sectionName");
                String url = newsItem.getString("webUrl");
                String date = newsItem.getString("webPublicationDate");

                Article articleItem = new Article(title, author, date, section, url);
                articlesList.add(articleItem);
            }


            //to handle JSON exceptions if thrown
        } catch (JSONException e) {
            Log.e("Utils", "JSON results could not be parsed", e);
        }

        // Return list of articles
        return articlesList;
    }

    //get a news item from server
    public static List<Article> getNewsData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Utils", "Error closing input stream", e);
        }

        //extract data from JSON and form list of articles
        List<Article> articlesList = extractNewsItems(jsonResponse);

        //return article list
        return articlesList;
    }
}
