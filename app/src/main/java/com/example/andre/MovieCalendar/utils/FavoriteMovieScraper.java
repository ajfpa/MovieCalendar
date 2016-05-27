package com.example.andre.MovieCalendar.utils;

import android.os.AsyncTask;

import com.example.andre.MovieCalendar.MainActivity;
import com.example.andre.MovieCalendar.view.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

/**
 * Created by ANDRE on 20/05/16.
 */
public class FavoriteMovieScraper extends AsyncTask<Void, Void, Void> {

    private String htmlPageUrl = "http://filmspot.pt/filmes/";
    private String htmlPageUrl2 = "http://filmspot.pt/estreias/";
    private Document htmlDocument;
    private Element htmlContentPrimary;
    private List movieList;
    private List<String> favorites;
    private MainActivity mainActivity;

    public FavoriteMovieScraper(List movieAdapter, List<String> favorites, MainActivity mainActivity){
        this.movieList=movieAdapter;
        this.favorites = favorites;
        this.mainActivity=mainActivity;
    }


    @Override
    protected Void doInBackground(Void... params) {

        try {
            if(favorites != null) {
                htmlDocument = Jsoup.connect(htmlPageUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36").get();
                htmlContentPrimary = htmlDocument.getElementById("contentsLeft");
                htmlContentPrimary.children().first().remove();
                htmlContentPrimary.children().first().remove();
                htmlContentPrimary.children().last().remove();
                //Log.e("OUTPUT","We have " +htmlContentPrimary.children().size() + " movies on show.");
                for (int count = 0; count < htmlContentPrimary.children().size(); count++) {
                    String title = htmlContentPrimary.child(count).select("div.filmeListaInfo > h2 > a > span").text();
                    if (isFavorite(title)) {
                        String coverUrl = htmlContentPrimary.child(count).select("div.filmeListaPoster > a > img").attr("abs:src");
                        String redirectUrl = htmlContentPrimary.child(count).select("div.filmeListaPoster > a").attr("abs:href");
                        //Log.d("OUTPUT", title+ "  "+redirectUrl );
                        movieList.add(new Movie(title, coverUrl, redirectUrl, false));
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mainActivity.notifyAdapterOfDataChanged();
    }

    private boolean isFavorite(String title){
        for(int i = 0; i < favorites.size(); i++){
            if(title.equals(favorites.get(i))){
                return true;
            }
        }
        return false;
    }

}

