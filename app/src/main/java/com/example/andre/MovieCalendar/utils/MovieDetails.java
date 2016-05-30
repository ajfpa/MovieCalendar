package com.example.andre.MovieCalendar.utils;

import com.example.andre.MovieCalendar.view.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by ToZe on 18/05/2016.
 */
public class MovieDetails {


    private void getCompleteMovieInfo(Movie movieData){
        String absoluteUrl=movieData.getRedirectUrl();
        Document tempDoc = null;
        try {
            tempDoc = Jsoup.connect(absoluteUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36").timeout(10*1000).get();
            String name=tempDoc.select("span[itemprop=name]").first().text();
            //Log.d("DUMBICES"," HI"+tempDoc.select("[itemprop=datePublished]").text());
            String data=tempDoc.select("div.caixaPais.caixaPaisPortugal > p > span[itemprop=datePublished]").text();
            String cover= tempDoc.select("div#filmePosterDiv > p > a > img").attr("abs:src");
            String description=tempDoc.select("div[itemprop=description]").text();
            String director=tempDoc.select("span[itemprop=director]").text();
            String starring=tempDoc.select("span[itemprop=actors] > a").text();
            movieData.setData(data);
            movieData.setCover(cover);
            movieData.setIntro(description);
            movieData.setDirector(director);
            movieData.setStarring(starring);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
