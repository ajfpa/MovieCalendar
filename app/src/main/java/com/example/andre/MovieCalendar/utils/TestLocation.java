package com.example.andre.MovieCalendar.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.andre.MovieCalendar.MapsActivity;
import com.example.andre.MovieCalendar.R;

/**
 * Created by ANDRE on 16/05/16.
 */
public class TestLocation extends AsyncTask<String, String, String> implements LocationListener {

    protected LocationManager locationManager;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    String nome;

    private ProgressDialog progressDialog = null;
    private Activity a = null;

    boolean gpsON = false;
    boolean networkON = false;

    public TestLocation(Activity a) {
        this.a = a;
    }

    public TestLocation(Activity a, String nome) {
        this.a = a;
        this.nome  = nome;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(a);
        progressDialog.setMessage(a.getResources().getString(R.string.location_loading));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    @Override
    protected String doInBackground(String... params) {
        try {
            locationManager = (LocationManager) a.getSystemService(Context.LOCATION_SERVICE);

            gpsON = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            networkON = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (gpsON && networkON) {
                // Get location from Network
                if (networkON) {
                    new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message message) {
                            if (ActivityCompat.checkSelfPermission(a.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(a.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, TestLocation.this);
                        }
                    };

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // Get Location using GPS Services
                if (gpsON) {
                    if (location == null) {
                        new Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message message) {
                                if (ActivityCompat.checkSelfPermission(a.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(a.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, TestLocation.this);
                            }
                        };

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }

                Intent i = new Intent(a.getApplicationContext(), MapsActivity.class);
                i.putExtra("latitude", latitude);
                i.putExtra("longitude", longitude);
                i.putExtra("nome", nome);
                a.startActivity(i);
            } else {
                //Inform that Location is OFF!
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(String file_url) {
        if(progressDialog !=null) progressDialog.dismiss();
        if (file_url != null){
            Toast.makeText(a, file_url, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }
    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }
    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}