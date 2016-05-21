package com.example.andre.MovieCalendar.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.andre.MovieCalendar.R;
import com.example.andre.MovieCalendar.utils.DownloadImageTask;

import java.util.List;

/**
 * Created by ToZe on 20/05/2016.
 */
public class MovieAdapterGrid extends ArrayAdapter {

    protected List<Movie> movieList;
    private LayoutInflater mInflater;
    private Context context;

    public MovieAdapterGrid(Context context, List<Movie> movieList) {
        super(context,0, movieList);
        mInflater = LayoutInflater.from(context);
        this.context=context;
        this.movieList=movieList;
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
            convertView = mInflater.inflate(R.layout.fragment_grid_movie_item, parent, false);
            holder.cover = (ImageView) convertView.findViewById(R.id.iv_grid_MovieCover);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Movie movie = movieList.get(position);

        if(movie.getImageCover()==null) {
            new DownloadImageTask(holder.cover,movie).execute(movie.getCover());
        }
        else {
            holder.cover.setImageBitmap(movie.getImageCover());
        }
        return convertView;
    }

    private static class ViewHolder {
        ImageView cover;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }
}
