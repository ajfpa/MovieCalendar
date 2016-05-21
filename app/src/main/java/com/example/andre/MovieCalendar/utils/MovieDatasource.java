package com.example.andre.MovieCalendar.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANDRE on 27/03/16.
 */
public class MovieDatasource {
    protected SQLiteDatabase db;
    protected DatabaseHelper dbhelper;
    private String sql = "INSERT INTO favorites VALUES ( NULL, ?)";

    public MovieDatasource(Context c) {
        dbhelper = new DatabaseHelper(c);
    }

    public void open() {
        db = dbhelper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public List<String> getAll() {
        Cursor c = db.rawQuery("SELECT * FROM favorites", null);

        if(c.getCount() == 0) {
            return null;
        } else {
            c.moveToFirst();

            List<String> favList = new ArrayList<String>();

            while(!c.isAfterLast()) {
                favList.add(c.getString(1));
                c.moveToNext();
            }
            c.close();

            return favList;
        }
    }

    public void removeFavorite(String name) {
        db.execSQL("DELETE FROM favorites WHERE nome = ?", new String[]{name});
    }

    public void addFavoriteMovieToDB(String name){
        db.execSQL(sql, new String[]{name});
    }

    public boolean isFavorite(String nome) {
        Cursor c = db.rawQuery("SELECT * FROM favorites WHERE nome = '" + nome + "'", null);

        if(c.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }
}
