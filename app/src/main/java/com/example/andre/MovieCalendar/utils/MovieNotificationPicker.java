package com.example.andre.MovieCalendar.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;
import java.util.Calendar;

/**
 * Created by ToZe on 26/05/2016.
 */
public class MovieNotificationPicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{


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
        timeFragment.show(getFragmentManager(),"timePicker");
    }

    public static class MovieTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener{


        private int day;
        private int month;
        private int year;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR);
            int minutes = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(),this,hour,minutes, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {

        }

        public void setDay(int day) {
            this.day = day;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }
}
