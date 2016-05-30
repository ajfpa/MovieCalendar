package com.example.andre.MovieCalendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ANDRE on 16/05/16.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    public double latitude, longitude;
    private String nome;
    public GoogleMap g_map;
    public Map<Marker, Integer> hmap;
    public static final int RANGE = 50000;
    protected Toolbar toolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        latitude = i.getDoubleExtra("latitude", 0);
        longitude = i.getDoubleExtra("longitude", 0);
        nome = i.getStringExtra("nome");

        MapFragment mapFragment = (MapFragment)
                getFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        g_map = mapFragment.getMap();

        g_map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent i = new Intent(MapsActivity.this, CinemaActivity.class);
                i.putExtra("id", hmap.get(marker));
                startActivity(i);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap map) {
        LatLng myPos = new LatLng(latitude, longitude);
        map.setMyLocationEnabled(true);
        if(nome == null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 13));
        }
        new getPOIs().execute();
    }


    private class getPOIs extends AsyncTask<String, Void, String> {
        String result = "";
        private ProgressDialog dialog = new ProgressDialog(MapsActivity.this);


        @Override
        protected void onPreExecute()
        {
            dialog.setMessage(getResources().getString(R.string.cinemas_loading));
            dialog.show();
        }


        @Override
        protected String doInBackground(String... params)
        {
            String response = null;
            try {
                URL url = new URL("http://andregloria.com/daam/theater.php");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                result = responseOutput.toString();
                System.out.println(result);

            } catch(Exception e) {
                return "-ERR";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            try {



                // now, we have to handle all the necessary results and add them to the map
                JSONObject jobj = new JSONObject(this.result);
                JSONArray poi = jobj.getJSONArray("theaters");
                for(int i=0; i<poi.length(); i++) {
                    JSONObject t_poi = poi.getJSONObject(i);
                    MarkerOptions marker = new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(t_poi.getString("latitude")), Double.parseDouble(t_poi.getString("longitude"))))
                            .title(t_poi.getString("name"))
                            .snippet(t_poi.getString("location"));



                    if(t_poi.getString("name").equals(nome)) {
                        g_map.setMyLocationEnabled(true);
                        LatLng myPos = new LatLng(Double.parseDouble(t_poi.getString("latitude")), Double.parseDouble(t_poi.getString("longitude")));
                        g_map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 13));
                    }

                    marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_cinema_marker));

                    g_map.addMarker(marker);

                }



                /*if(jobj.get("status").toString().compareTo("OK")==0) {
                    JSONArray poi = jobj.getJSONArray("poi");
                    hmap = new HashMap<Marker, Integer>();
                    for(int i=0; i<poi.length(); i++) {
                        JSONObject t_poi = poi.getJSONObject(i);
                        Log.i("guideMe", "-> " + t_poi.getString("name"));
                        //add the marker to the map
                        Marker m = g_map.addMarker(new MarkerOptions()
                                .title(t_poi.getString("name"))
                                .snippet(t_poi.getString("address"))
                                .position(new LatLng(t_poi.getDouble("latt"),
                                        t_poi.getDouble("logt"))));
                        hmap.put(m, t_poi.getInt("id"));
                    }
                }*/
            } catch (Exception e) {
                Log.i("guideMe", "erro");
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
