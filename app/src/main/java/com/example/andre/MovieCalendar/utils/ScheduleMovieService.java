package com.example.andre.MovieCalendar.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by ToZe on 26/05/2016.
 */
public class ScheduleMovieService extends Service {

    private final IBinder MOVIE_BINDER = new ScheduleMovieBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return MOVIE_BINDER;
    }

    public class ScheduleMovieBinder extends Binder {
        ScheduleMovieService getService(){
            return ScheduleMovieService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void setMovieNotification(Calendar movieDate) {
        new MovieAlarm(this, movieDate).execute();
    }
}
