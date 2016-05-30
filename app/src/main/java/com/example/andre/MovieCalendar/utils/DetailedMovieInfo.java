package com.example.andre.MovieCalendar.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.andre.MovieCalendar.view.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by ToZe on 16/05/2016.
 */
public class DetailedMovieInfo extends AsyncTask<Movie,Void,Void> {

    public DetailedMovieInfo(){

    }

    @Override
    protected Void doInBackground(Movie... params) {
        Movie movieData = params[0];
        String absoluteUrl=movieData.getRedirectUrl();
        Document tempDoc = null;
        Log.d("MOVIE NAME: ", movieData.getNome() + " " + movieData.getRedirectUrl());
        try {
            tempDoc = Jsoup.connect(absoluteUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36").timeout(10*1000).get();
            String name=tempDoc.select("span[itemprop=name]").first().text();
            //Log.d("DUMBICES"," HI"+tempDoc.select("[itemprop=datePublished]").text());
            String data=tempDoc.select("div.caixaPais.caixaPaisPortugal > p > span[itemprop=datePublished]").text();
            String cover= tempDoc.select("div#filmePosterDiv > p > a > img").attr("abs:src");
            String description=tempDoc.select("div[itemprop=description]").text();
            String director=tempDoc.select("span[itemprop=director]").text();
            String starring=tempDoc.select("span[itemprop=actors] > a").text();
            Elements cinemas = tempDoc.select("#filmeInfoDivSessoes > .coluna > .colunaInside > ul > li.zsmall");
            String imdbUrl= tempDoc.select("[itemprop=sameAs]").text();
            for (Element element :cinemas){
                movieData.getTheaters().add(element.text());
            }
            String[] key = imdbUrl.split("/");
            movieData.setData(data);
            movieData.setCover(cover);
            movieData.setIntro(description);
            movieData.setDirector(director);
            movieData.setStarring(starring);
            movieData.setImdbUrl(imdbUrl);
            movieData.setImdbKey(key[key.length-1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
