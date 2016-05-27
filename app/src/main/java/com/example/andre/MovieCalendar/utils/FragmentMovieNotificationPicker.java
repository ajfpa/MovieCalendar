package com.example.andre.MovieCalendar.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by ToZe on 26/05/2016.
 */
public class FragmentMovieNotificationPicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private ScheduleMovie scheduleMovie;
    private String movieName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        MovieTimePicker timeFragment = new MovieTimePicker();
        timeFragment.setDay(dayOfMonth);
        timeFragment.setMonth(monthOfYear);
        timeFragment.setYear(year);
        timeFragment.setTimeScheduleMovie(scheduleMovie,movieName);
        timeFragment.show(getFragmentManager(),"timePicker");
    }

    public void setDateScheduleMovie(ScheduleMovie scheduleMovie,String movieName) {
        this.scheduleMovie = scheduleMovie;
        this.movieName=movieName;
    }

    public static class MovieTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener{


        private int day;
        private int month;
        private int year;
        private ScheduleMovie scheduleMovie;
        private String movieName;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(),this,hour,minutes,DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            Calendar dateChosen= Calendar.getInstance();
            dateChosen.set(year,month,day);
            dateChosen.set(Calendar.HOUR_OF_DAY,hourOfDay);
            dateChosen.set(Calendar.MINUTE,minute);
            dateChosen.set(Calendar.SECOND,0);
            scheduleMovie.setMovieNotificationDate(dateChosen,movieName);

        }

        protected void setDay(int day) {
            this.day = day;
        }

        protected void setMonth(int month) {
            this.month = month;
        }

        protected void setYear(int year) {
            this.year = year;
        }

        protected void setTimeScheduleMovie(ScheduleMovie scheduleMovie, String movieName) {
            this.scheduleMovie = scheduleMovie;
            this.movieName=movieName;
        }
    }
}
