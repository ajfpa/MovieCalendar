package com.example.andre.MovieCalendar.utils;

import android.os.AsyncTask;

import com.example.andre.MovieCalendar.view.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

/**
 * Created by ToZe on 16/05/2016.
 */
public class CurrentMoviesScraper extends AsyncTask<Void, Void, Void> {

    private String htmlPageUrl = "http://filmspot.pt/filmes/";
    private String htmlPageUrl2 = "http://filmspot.pt/estreias/";
    private Document htmlDocument;
    private Element htmlContentPrimary;
    private List movieList;

    public CurrentMoviesScraper(List movieAdapter){
        this.movieList=movieAdapter;
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

}

