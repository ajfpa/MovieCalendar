package com.example.andre.MovieCalendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.Map;

/**
 * Created by ANDRE on 16/05/16.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    public double latitude, longitude;
    public GoogleMap g_map;
    public Map<Marker, Integer> hmap;
    public static final int RANGE = 50000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent i = getIntent();
        latitude = i.getDoubleExtra("latitude", 0);
        longitude = i.getDoubleExtra("longitude", 0);


        MapFragment mapFragment = (MapFragment)
                getFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        g_map = mapFragment.getMap();

        g_map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.i("guideMe", "Click on marker with ID = " + hmap.get(marker));
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
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 13));
        new getPOIs().execute();
    }


    private class getPOIs extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(MapsActivity.this);


        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Getting POIs from server...");
            dialog.show();
        }


        @Override
        protected String doInBackground(String... params)
        {
            String response = null;
            try {
                /*HttpClient httpclient = new DefaultHttpClient();
                Log.i("guideMe", "Asking = " + "http://192.168.1.113:3000/poi/
                        range/"+latitude+"/"+longitude+"/"+myRange);
                        HttpResponse httpResponse = httpclient.execute(new HttpGet("http://
                192.168.1.113:3000/poi/range/"+latitude+"/"+longitude+"/"+myRange));
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                response = reader.readLine();
                Log.i("guideMe", "Response = " + response);*/

            } catch(Exception e) {
                return "-ERR";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            try {


                MarkerOptions marker = new MarkerOptions()
                        .position(new LatLng(38.728082, -9.218374))
                        .title("Alegro Alfragide")
                        .snippet("Alegro Alfragide");

                marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_cinema_marker));

                g_map.addMarker(marker);



                // now, we have to handle all the necessary results and add them to the map
                /*JSONObject jobj = new JSONObject(result);
                if(jobj.get("status").toString().compareTo("OK")==0) {
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(MapsActivity.this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
