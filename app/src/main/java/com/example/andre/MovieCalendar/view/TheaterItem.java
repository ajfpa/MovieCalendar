package com.example.andre.MovieCalendar.view;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.andre.MovieCalendar.view.Theater;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANDRE on 26/05/16.
 */
public class TheaterItem implements Parcelable {

        protected long id;
        protected String nome, location;
        protected boolean onDisplay;

        public TheaterItem(long id, String nome, String location) {
            this.id = id;
            this.nome = nome;
            this.location = location;
        }


        protected TheaterItem(Parcel in) {
            id = in.readLong();
            nome = in.readString();
            location = in.readString();
            onDisplay = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(id);
            dest.writeString(nome);
            dest.writeString(location);
            dest.writeByte((byte) (onDisplay ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<TheaterItem> CREATOR = new Creator<TheaterItem>() {
            @Override
            public TheaterItem createFromParcel(Parcel in) {
                return new TheaterItem(in);
            }

            @Override
            public TheaterItem[] newArray(int size) {
                return new TheaterItem[size];
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

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

    }
