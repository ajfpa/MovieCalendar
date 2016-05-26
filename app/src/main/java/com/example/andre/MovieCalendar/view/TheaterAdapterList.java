package com.example.andre.MovieCalendar.view;

/**
 * Created by ANDRE on 26/05/16.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.andre.MovieCalendar.R;

import java.util.List;

public class TheaterAdapterList extends ArrayAdapter<TheaterItem> {

    protected List<TheaterItem> theaterList;
    private LayoutInflater mInflater;
    private Context context;

    public TheaterAdapterList(Context context, List<TheaterItem> theaterList) {
        super(context,0,theaterList);
        mInflater = LayoutInflater.from(context);
        this.theaterList = theaterList;
        this.context = context;

        for(int i = 0; i < theaterList.size(); i++){
            Log.d("Cinema", theaterList.get(i).getNome() + " - " + theaterList.get(i).getLocation());
        }
    }

    @Override
    public int getCount() {
        return theaterList.size();
    }

    @Override
    public TheaterItem getItem(int position) {
        return theaterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;


        if (convertView == null || convertView.getTag() == null) {

            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.fragment_theater_item, parent, false);

            holder.name = (TextView) convertView.findViewById(R.id.theater_nome);
            holder.location = (TextView) convertView.findViewById(R.id.theater_location);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TheaterItem theater = theaterList.get(position);
        holder.name.setText(theater.getNome());
        holder.location.setText(theater.getLocation());
        return convertView;
    }



    private static class ViewHolder {
        TextView name;
        TextView location;
    }

    public List<TheaterItem> getTheaterList() {
        return theaterList;
    }

    /*public void setMovieList(List<Movie> movieList) {
        this.movieList.clear();
        this.movieList.addAll(movieList);
        notifyDataSetChanged();
    }*/
}


