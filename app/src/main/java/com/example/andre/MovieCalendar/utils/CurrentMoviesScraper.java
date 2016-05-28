package com.example.andre.MovieCalendar.utils;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.andre.MovieCalendar.MainActivity;
import com.example.andre.MovieCalendar.R;
import com.example.andre.MovieCalendar.view.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by ToZe on 16/05/2016.
 */
public class CurrentMoviesScraper extends AsyncTask<Void, Void, Void> {

    private String htmlPageUrl = "http://filmspot.pt/filmes/";
    private String htmlPageUrl2 = "http://filmspot.pt/estreias/";
    private Document htmlDocument;
    private Element htmlContentPrimary;
    private List<Movie> movieList;
    private ArrayList<Movie> favoritesList;
    private List<String> favorites;
    private MainActivity mainActivity;

    public CurrentMoviesScraper(List<Movie> movieAdapter,List<String> favorites, MainActivity mainActivity){
        this.movieList=movieAdapter;
        this.favorites=favorites;
        this.favoritesList=new ArrayList<Movie>();
        this.mainActivity=mainActivity;
    }


    @Override
    protected Void doInBackground(Void... params) {

        try {

            htmlDocument = Jsoup.connect(htmlPageUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                    "Chrome/50.0.2661.94 Safari/537.36").get();
            htmlContentPrimary = htmlDocument.getElementById("contentsLeft");
            htmlContentPrimary.children().first().remove();
            htmlContentPrimary.children().first().remove();
            htmlContentPrimary.children().last().remove();
            //Log.e("OUTPUT","We have " +htmlContentPrimary.children().size() + " movies on show.");
            for (int count=0; count < htmlContentPrimary.children().size();count++) {
                String title = htmlContentPrimary.child(count).select("div.filmeListaInfo > h2 > a > span").text();
                String coverUrl = htmlContentPrimary.child(count).select("div.filmeListaPoster > a > img").attr("abs:src");
                String redirectUrl = htmlContentPrimary.child(count).select("div.filmeListaPoster > a").attr("abs:href");
                //Log.d("OUTPUT", title+ "  "+redirectUrl );
                movieList.add(new Movie(title,coverUrl,redirectUrl,true));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d("FAVORITE","Starting Favorites");
        mainActivity.notifyAdapterOfDataChanged();
        if (favorites.size() != 0) {
            for (String title : favorites) {
                for (Movie movie : movieList) {
                    if (title.equalsIgnoreCase(movie.getNome())){
                        Log.d("FAVORITE","Found Favorite : " + movie.getNome() + " size of fav list is: " + favoritesList.size());
                        favoritesList.add(movie);
                        Log.d("FAVORITE", "size of fav list is: " + favoritesList.size());

                    }
                }
            }

        }
        mainActivity.getLcFav().addAll(favoritesList);
        TextView tv_movie =(TextView) mainActivity.findViewById(R.id.tv_Movies);
        if(Objects.equals(tv_movie.getText(),"Favorite")){
            mainActivity.getLcCurrent().addAll(mainActivity.getLcFav());
        }
        mainActivity.notifyAdapterOfDataChanged();
    }
}


