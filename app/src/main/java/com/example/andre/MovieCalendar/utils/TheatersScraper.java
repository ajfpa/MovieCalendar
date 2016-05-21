package com.example.andre.MovieCalendar.utils;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

/**
 * Created by ToZe on 20/05/2016.
 */
public class TheatersScraper extends AsyncTask<Void, Void, Void> {



    private List<Theater> theatersList;


    public TheatersScraper(List theatersList) {
        this.theatersList = theatersList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        String htmlPageUrl = "http://filmspot.pt/salas/";
        Document htmlDocument;
        try {
            htmlDocument = Jsoup.connect(htmlPageUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                    "Chrome/50.0.2661.94 Safari/537.36").timeout(10 * 1000).get();
            Elements htmlContent = htmlDocument.select(".dotsSpace >li");

            for (int count = 0; count < htmlContent.size(); count++) {
                String name = htmlContent.get(count).text();
                String redirectUrl = htmlContent.get(count).select("a").attr("abs:href");
                theatersList.add(new Theater(name, redirectUrl));
            }

            /*for (Theater theater:theatersList) {
                getCompleteTheaterInfo(theater);
            }*/


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void getCompleteTheaterInfo(Theater theater){
        String htmlPageUrl = theater.getRedirectUrl();
        Document htmlDocument;
        try {
            htmlDocument = Jsoup.connect(htmlPageUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                    "Chrome/50.0.2661.94 Safari/537.36").timeout(10 * 1000).get();
            theater.setLatitude(Double.valueOf(htmlDocument.select(".latitude").text()));
            theater.setLongitude(Double.valueOf(htmlDocument.select(".longitude").text()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
