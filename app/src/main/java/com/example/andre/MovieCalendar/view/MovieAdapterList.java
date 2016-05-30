package com.example.andre.MovieCalendar.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andre.MovieCalendar.utils.DownloadImageTask;
import com.example.andre.MovieCalendar.R;


import java.util.List;


public class MovieAdapterList extends ArrayAdapter<Movie> {

    protected List<Movie> movieList;
    private LayoutInflater mInflater;
    private Context context;

    public MovieAdapterList(Context context, List<Movie> movieList) {
        super(context,0,movieList);
        mInflater = LayoutInflater.from(context);
        this.movieList = movieList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Movie getItem(int position) {
        return movieList.get(position);
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
            convertView = mInflater.inflate(R.layout.fragment_movie_item, parent, false);

            holder.name = (TextView) convertView.findViewById(R.id.etNome);
            holder.cover = (ImageView) convertView.findViewById(R.id.iv_movieCover);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Movie movie = movieList.get(position);
        holder.name.setText(movie.getNome());
        if(movie.getImageCover()==null) {
            new DownloadImageTask(holder.cover,movie).execute(movie.getCover());
        }
        else {
            holder.cover.setImageBitmap(movie.getImageCover());
        }
        return convertView;
    }



    private static class ViewHolder {
        TextView name;
        ImageView cover;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList=movieList;
    }
}

