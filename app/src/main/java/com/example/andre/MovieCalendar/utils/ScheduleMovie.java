package com.example.andre.MovieCalendar.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by ToZe on 27/05/2016.
 */
public class ScheduleMovie {


    private ScheduleMovieService boundMovieService;
    private Context movieContext;
    private boolean isBound;

    private ServiceConnection serviceConnection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            boundMovieService = ((ScheduleMovieService.ScheduleMovieBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public ScheduleMovie (Context movieContext){
        this.movieContext=movieContext;
    }

    public void setMovieNotificationDate(Calendar movieDate,String movieName){
        boundMovieService.setMovieNotification(movieDate,movieName);
    }

    public void bindService(){
        movieContext.bindService(new Intent(movieContext,ScheduleMovieService.class),serviceConnection,Context.BIND_AUTO_CREATE);
    }

    public void unbindService(){
        if(isBound){
            movieContext.unbindService(serviceConnection);
            isBound=false;
        }
    }
}
