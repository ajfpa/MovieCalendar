package com.example.andre.MovieCalendar;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.andre.MovieCalendar.utils.TestLocation;

/**
 * Created by ToZe on 18/05/2016.
 */
public class DrawerItemListener implements ListView.OnItemClickListener{

    MainActivity activity;
    DrawerLayout mDrawerLayout;

    public DrawerItemListener(MainActivity activity, DrawerLayout mDrawerLayout) {
        this.activity = activity;
        this.mDrawerLayout=mDrawerLayout;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position==0) {
          /*  activity.getLcCurrent().clear();
            activity.getLcCurrent().addAll(activity.getLcFav());*/
            activity.setLcCurrent(activity.getLcFav());
            activity.notifyAdapterOfDataChanged();
            TextView tv_movie= (TextView)activity.findViewById(R.id.tv_Movies);
            tv_movie.setText("Favorite");
        }else if (position==1){
            /*activity.getLcCurrent().addAll(activity.getLcMovies());
            activity.notifyAdapterOfDataChanged();*/
            activity.setLcCurrent(activity.getLcMovies());
            TextView tv_movie= (TextView)activity.findViewById(R.id.tv_Movies);
            tv_movie.setText("On Cinema");
        }else if(position==2){
           /* activity.getLcCurrent().clear();
            activity.getLcCurrent().addAll(activity.getLcUpcoming());*/
            activity.setLcCurrent(activity.getLcUpcoming());
            activity.notifyAdapterOfDataChanged();
            TextView tv_movie= (TextView)activity.findViewById(R.id.tv_Movies);
            tv_movie.setText("Upcoming");
        }else if (position==3){
            new TestLocation(activity).execute();
        }else if(position==4){
            Intent i = new Intent(activity.getApplicationContext(), SettingsActivity.class);
            activity.startActivity(i);
        }else if (position==5){
            Intent i = new Intent(activity.getApplicationContext(), AboutActivity.class);
            activity.startActivity(i);
        }
        mDrawerLayout.closeDrawers();

    }
}
