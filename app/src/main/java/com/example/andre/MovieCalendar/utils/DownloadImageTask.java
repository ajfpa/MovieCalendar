package com.example.andre.MovieCalendar.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.andre.MovieCalendar.view.Movie;

import java.io.InputStream;

/**
 * Created by ANDRE on 02/05/16.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Movie movie;

    public DownloadImageTask(ImageView bmImage, Movie movie) {
        this.bmImage = bmImage;
        this.movie=movie;
    }


    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        movie.setImageCover(result);
        bmImage.setImageBitmap(movie.getImageCover());
    }
}
