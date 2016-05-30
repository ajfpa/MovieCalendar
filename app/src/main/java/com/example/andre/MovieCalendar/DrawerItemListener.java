package com.example.andre.MovieCalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andre.MovieCalendar.utils.TestLocation;

/**
 * Created by ToZe on 18/05/2016.
 */
public class DrawerItemListener implements ListView.OnItemClickListener{

    MainActivity activity;
    DrawerLayout mDrawerLayout;
    boolean access = false;

    public DrawerItemListener(MainActivity activity, boolean access, DrawerLayout mDrawerLayout) {
        this.activity = activity;
        this.mDrawerLayout=mDrawerLayout;
        this.access = access;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position==0) {
            activity.getLcCurrent().clear();
            activity.getLcCurrent().addAll(activity.getLcFav());
            activity.notifyAdapterOfDataChanged();
            TextView tv_movie= (TextView)activity.findViewById(R.id.tv_Movies);
            tv_movie.setText(activity.getResources().getString(R.string.title_favorites));
        }else if (position==1){
            activity.getLcCurrent().clear();
            activity.getLcCurrent().addAll(activity.getLcMovies());
            activity.notifyAdapterOfDataChanged();
            TextView tv_movie= (TextView)activity.findViewById(R.id.tv_Movies);
            tv_movie.setText(activity.getResources().getString(R.string.title_onCinema));
        }else if(position==2){
            activity.getLcCurrent().clear();
            activity.getLcCurrent().addAll(activity.getLcUpcoming());
            activity.notifyAdapterOfDataChanged();
            TextView tv_movie= (TextView)activity.findViewById(R.id.tv_Movies);
            tv_movie.setText(activity.getResources().getString(R.string.title_upcoming));
        }else if (position==3){
            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            boolean gpsON = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            boolean networkON = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(access) {
                if (gpsON && networkON) {
                    new TestLocation(activity).execute();
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.no_location), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.logged_in_see_cinemas), Toast.LENGTH_LONG).show();
            }
        }else if (position==4){
            Intent i = new Intent(activity.getApplicationContext(), AboutActivity.class);
            activity.startActivity(i);
        }else if(position==5){
            SharedPreferences sharedPrefs = activity.getSharedPreferences("login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.remove("username");
            editor.remove("password");
            editor.commit();
            Toast.makeText(activity, activity.getResources().getString(R.string.logged_out), Toast.LENGTH_LONG).show();
            activity.setAccess(false);
           // activity.onBackPressed();
        }
        mDrawerLayout.closeDrawers();

    }
}
