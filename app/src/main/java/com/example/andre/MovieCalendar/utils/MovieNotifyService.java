package com.example.andre.MovieCalendar.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.andre.MovieCalendar.R;

/**
 * Created by ToZe on 27/05/2016.
 */
public class MovieNotifyService extends Service{

    private final int NOTIFICATION_ID = 1984;
    private final IBinder MOVIE_BINDER = new ScheduleMovieBinder();
    public static String NOTIFY_MOVIE = "NOTIFY_MOVIE";
    private Intent movieIntent;
    private NotificationManager notificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return MOVIE_BINDER;
    }

    public class ScheduleMovieBinder extends Binder {
        MovieNotifyService getService(){
            return MovieNotifyService.this;
        }

    }

    @Override
    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        this.movieIntent=intent;
        if(intent.getBooleanExtra(NOTIFY_MOVIE,false))
            showNotification();
        return START_NOT_STICKY;
    }

    private void showNotification(){

        String title = movieIntent.getStringExtra("MovieName");
        int movieIcon = R.drawable.ic_movie_black_24dp;
        String message= title+" has been released in theaters";
        long time = System.currentTimeMillis();
        NotificationCompat.Builder movieNotification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(movieIcon)
                        .setContentTitle(title)
                        .setContentText(message);
        notificationManager.notify(NOTIFICATION_ID,movieNotification.build());
        stopSelf();

    }
}
