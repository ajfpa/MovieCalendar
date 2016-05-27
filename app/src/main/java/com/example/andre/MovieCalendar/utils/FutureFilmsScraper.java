package com.example.andre.MovieCalendar.utils;

import android.os.AsyncTask;

import com.example.andre.MovieCalendar.MainActivity;
import com.example.andre.MovieCalendar.view.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by ToZe on 18/05/2016.
 */
public class FutureFilmsScraper extends AsyncTask<Void, Void, Void> {


    private String htmlPageUrl = "http://filmspot.pt/estreias/";
    private Document htmlDocument;
    private List movieList,favoritesList;
    private HashMap<String,Movie> webList;
    private List<String> favorites;
    private MainActivity mainActivity;

    public FutureFilmsScraper(List movieList,List<String> favorites, MainActivity mainActivity){
        this.movieList = movieList;
        this.favorites=favorites;
        this.favoritesList=new ArrayList();
        this.mainActivity=mainActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void avoid) {

        movieList.addAll(webList.values());
        mainActivity.notifyAdapterOfDataChanged();
        if(favorites.size()!=0){
            setFavorites();
            mainActivity.getLcFav().addAll(favoritesList);
            mainActivity.notifyAdapterOfDataChanged();
        }

    }

    private void setFavorites(){
        for(int i = 0; i < favorites.size(); i++){
            if(webList.containsKey(favorites.get(i))){
                favoritesList.add(webList.get(favorites.get(i)));
            }
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        webList = new HashMap<String,Movie>();
        try {
            Calendar calendar= new GregorianCalendar().getInstance(TimeZone.getDefault());
            int month_cal=calendar.get(Calendar.MONTH)+1;
            int year_cal=calendar.get(Calendar.YEAR);

            for (int i = 1; i < 4 ; i++) {
                int month =0;
                int year =year_cal;
                if(month_cal+i>12){
                    year+=1;
                    month=month_cal+i-12;
                }else
                    month=month_cal+i;

                htmlDocument = Jsoup.connect(htmlPageUrl +year+"0"+month).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/50.0.2661.94 Safari/537.36").timeout(10*1000).get();
                Elements htmlContent = htmlDocument.select("div > div[class*=filmeLista]");
                //Log.d("OUTPUT", "Size is: " + htmlSecondary.size());

                for (int count = 0; count < htmlContent.size(); count++) {
                    String title = htmlContent.get(count).select("div.filmeListaInfo > h3 > a > span").text();
                    String coverUrl = htmlContent.get(count).select("div.filmeListaPoster > a > img").attr("abs:src");
                    String redirectUrl = htmlContent.get(count).select("div.filmeListaPoster > a").attr("abs:href");
                    if (!webList.containsKey(title) && title != null && redirectUrl != null && coverUrl!=null) {
                        webList.put(title, new Movie(title,coverUrl, redirectUrl,false));
                        //Log.d("OUTPUT", title +  " : " + redirectUrl);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
