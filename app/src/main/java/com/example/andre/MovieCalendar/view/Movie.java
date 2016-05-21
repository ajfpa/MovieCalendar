package com.example.andre.MovieCalendar.view;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANDRE on 27/03/16.
 */
public class Movie implements Parcelable {

    protected long id;
    protected String nome, data, intro, cover, director, starring, redirectUrl;
    protected int favorite=0;
    protected Bitmap imageCover;
    protected List<String> theaters;
    protected boolean onDisplay;

    public Movie(long id, String nome, String data, String intro, String cover, String director, String starring, int favorite) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.intro = intro;
        this.cover = cover;
        this.director = director;
        this.starring = starring;
    }


    public Movie(String nome, String cover ,String redirectUrl, boolean onDisplay) {
        this.nome = nome;
        this.cover=cover;
        this.redirectUrl=redirectUrl;
        this.onDisplay=onDisplay;
        this.theaters= new ArrayList<String>();
    }


    protected Movie(Parcel in) {
        id = in.readLong();
        nome = in.readString();
        data = in.readString();
        intro = in.readString();
        cover = in.readString();
        director = in.readString();
        starring = in.readString();
        redirectUrl = in.readString();
        favorite = in.readInt();
        imageCover = in.readParcelable(Bitmap.class.getClassLoader());
        theaters = in.createStringArrayList();
        onDisplay = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nome);
        dest.writeString(data);
        dest.writeString(intro);
        dest.writeString(cover);
        dest.writeString(director);
        dest.writeString(starring);
        dest.writeString(redirectUrl);
        dest.writeInt(favorite);
        dest.writeParcelable(imageCover, flags);
        dest.writeStringList(theaters);
        dest.writeByte((byte) (onDisplay ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getStarring() {
        return starring;
    }

    public void setStarring(String starring) {
        this.starring = starring;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getRedirectUrl() { return redirectUrl; }

    public Bitmap getImageCover() {
        return imageCover;
    }

    public void setImageCover(Bitmap imageCover) {
        this.imageCover = imageCover;
    }

    public List<String> getTheaters() {
        return theaters;
    }

    public void setTheaters(List<String> theaters) {
        this.theaters = theaters;
    }
}
