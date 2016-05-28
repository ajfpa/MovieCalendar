package com.example.andre.MovieCalendar.view;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ToZe on 20/05/2016.
 */
public class Theater implements Parcelable{

    private double latitude;
    private double longitude;
    private String redirectUrl;
    private String name;

    public Theater(String name,String redirectUrl) {
        this.name = name;
        this.redirectUrl = redirectUrl;
    }

    protected Theater(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        redirectUrl = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(redirectUrl);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Theater> CREATOR = new Creator<Theater>() {
        @Override
        public Theater createFromParcel(Parcel in) {
            return new Theater(in);
        }

        @Override
        public Theater[] newArray(int size) {
            return new Theater[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
