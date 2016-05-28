package com.example.andre.MovieCalendar.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.andre.MovieCalendar.DetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ToZe on 27/05/2016.
 */
public class ImdbApiCall extends AsyncTask<Void,Void,Void> {

    private String key;
    private String omdbUrl="http://www.omdbapi.com/?i=";
    private String apiOptions="&plot=short&r=json";
    private double rating=0.0;
    private DetailsActivity detailsActivity;
    public ImdbApiCall(DetailsActivity detailsActivity,String key) {
        this.detailsActivity=detailsActivity;
        this.key=key;
    }


    @Override
    protected Void doInBackground(Void... params) {
        String jsonResult="";

        try {
            URL finalUrl = new URL(omdbUrl+key+apiOptions);
            Log.d("JSON",finalUrl.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) finalUrl.openConnection();
            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            jsonResult = stringBuilder.toString();
            httpURLConnection.disconnect();
            JSONObject movieJson = new JSONObject(jsonResult);
            Log.d("JSON",movieJson.toString());
            rating=movieJson.getDouble("imdbRating");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(rating!=0.0){
        detailsActivity.getMovie().setRating(rating/2);
        detailsActivity.setRating(rating/2);
        }
    }
}
