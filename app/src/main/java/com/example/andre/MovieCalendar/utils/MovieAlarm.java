package com.example.andre.MovieCalendar.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by ToZe on 27/05/2016.
 */
public class MovieAlarm extends AsyncTask<Void,Void,Void> {


    private Calendar movieDate;
    private ScheduleMovieService scheduleMovieService;
    private AlarmManager alarmManager;

    public MovieAlarm(ScheduleMovieService scheduleMovieService, Calendar movieDate) {

        this.scheduleMovieService=scheduleMovieService;
        this.movieDate=movieDate;
        this.alarmManager = (AlarmManager) scheduleMovieService.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected Void doInBackground(Void... params) {
        Intent intent = new Intent(scheduleMovieService, MovieNotifyService.class);
        intent.putExtra(MovieNotifyService.NOTIFY_MOVIE, true);
        PendingIntent pendingIntent = PendingIntent.getService(scheduleMovieService, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC, movieDate.getTimeInMillis(), pendingIntent);
        return null;
    }
}
