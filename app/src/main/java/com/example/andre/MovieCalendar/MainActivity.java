package com.example.andre.MovieCalendar;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.andre.MovieCalendar.utils.CheckConnection;
import com.example.andre.MovieCalendar.utils.FutureFilmsScraper;
import com.example.andre.MovieCalendar.utils.Theater;
import com.example.andre.MovieCalendar.utils.TheatersScraper;
import com.example.andre.MovieCalendar.view.MovieAdapterGrid;
import com.example.andre.MovieCalendar.view.MovieAdapterList;
import com.example.andre.MovieCalendar.utils.CurrentMoviesScraper;
import com.example.andre.MovieCalendar.utils.DetailedMovieInfo;
import com.example.andre.MovieCalendar.utils.FavoriteMovieScraper;
import com.example.andre.MovieCalendar.utils.MovieDatasource;
import com.example.andre.MovieCalendar.view.Movie;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    protected MovieDatasource ds;
    protected ListView lvMovies;
    protected GridView grdMovies;
    private boolean grid_visible=false;
    protected MovieAdapterList adapterList;
    protected MovieAdapterGrid adapterGrid;
    // usado para manipular e saber que uma determinada posição da ListVIew corresponda a um determinado "id" na base de dados!
    protected Map<Integer, Long> hm, hmFav;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ArrayList<Movie> lcMovies,lcUpcoming,lcFav,lcCurrent,lcBackup;
    private ArrayList<Theater> lcTheaters;
    protected ListView mDrawerList;
    protected int idFav;

    protected static int DETAILS = 0;
    private int RESULT_FAVORITE_ADD = 1;
    private int RESULT_FAVORITE_REMOVE = 0;
    private String lastMovie;

    protected boolean access;
    String[] drawerOptions;
    Toolbar toolBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(lcBackup.size()==0)
                    lcBackup=(ArrayList)lcCurrent.clone();
                ArrayList<Movie> found = new ArrayList();
                for (Movie movie:lcCurrent) {
                    if(movie.getNome().toLowerCase().contains(query.toLowerCase())){
                        found.add(movie);
                    }
                }
                if(found.size()!=0){
                    lcCurrent.clear();
                    lcCurrent.addAll(found);
                    adapterList.notifyDataSetChanged();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        MenuItem menuSearch = menu.findItem(R.id.search);
        MenuItemCompat.setOnActionExpandListener(menuSearch, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if(lcCurrent!=null){
                    if(lcCurrent.size()!=0){
                        lcCurrent.clear();
                        lcCurrent.addAll(lcBackup);
                        lcBackup.clear();
                        adapterList.notifyDataSetChanged();
                    }
                }

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.list_grid_switcher:
                if(!grid_visible){
                    toolBar.getMenu().findItem(R.id.list_grid_switcher).setIcon(R.drawable.ic_list_black_24dp);
                    lvMovies.setVisibility(View.GONE);
                    grdMovies.setVisibility(View.VISIBLE);
                    grid_visible=true;
                }else {
                    grid_visible=false;
                    toolBar.getMenu().findItem(R.id.list_grid_switcher).setIcon(R.drawable.ic_grid_on_black_24dp);
                    grdMovies.setVisibility(View.GONE);
                    lvMovies.setVisibility(View.VISIBLE);
                }

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onResume() {
        super.onResume();
        addToListView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerOptions = getResources().getStringArray(R.array.side_bar_option);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapterList for the list view
        mDrawerList.setAdapter(new ArrayAdapter<>(getApplicationContext(),R.layout.drawer_list_item,drawerOptions));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemListener(this));

        lvMovies = (ListView) findViewById(R.id.lvMovies);
        grdMovies = (GridView) findViewById(R.id.grdMovies);


        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolBar,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);


        if(savedInstanceState !=null){
            lcCurrent =savedInstanceState.getParcelable("listM");
        }

        ds = new MovieDatasource(this);
        ds.open();

        addToListView();

        Intent i = getIntent();

        access = i.getBooleanExtra("access", false);

        if(access) {
            lvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    Context ctx = getApplicationContext();
                    Intent i = new Intent(ctx, DetailsActivity.class);

                    Movie c = (Movie)lvMovies.getItemAtPosition(position);

                    if(c.getData()==null){
                        try {
                            new DetailedMovieInfo().execute(c).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }

                    i.putExtra("movie",c);
                    i.putExtra("favorite", ds.isFavorite(c.getNome()));
                    lastMovie = c.getNome();
                    startActivityForResult(i, DETAILS);
                }
            });
            grdMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    Context ctx = getApplicationContext();
                    Intent i = new Intent(ctx, DetailsActivity.class);

                    Movie c = (Movie)lvMovies.getItemAtPosition(position);

                    if(c.getData()==null){
                        try {
                            new DetailedMovieInfo().execute(c).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }

                    i.putExtra("movie",c);
                    lastMovie = c.getNome();
                    startActivityForResult(i, DETAILS);
                }
            });

        } else {
            lvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), "Need to be logged in to see Movie Info", Toast.LENGTH_SHORT).show();

                }
            });


        }
    }


    public void addToListView(){


        if(lcMovies ==null) {
            lcMovies= new ArrayList<Movie>();
            if (new CheckConnection(MainActivity.this).isConnected()) {
                new CurrentMoviesScraper(lcMovies).execute();
            }
        }

        lcFav= new ArrayList<Movie>();
        if (new CheckConnection(MainActivity.this).isConnected()) {
            new FavoriteMovieScraper(lcFav, ds.getAll()).execute();
        }

        if(lcUpcoming==null){
            lcUpcoming=new ArrayList<Movie>();
            if (new CheckConnection(MainActivity.this).isConnected()) {
                new FutureFilmsScraper(lcUpcoming).execute();
            }
        }

        if(lcBackup==null){
            lcBackup=new ArrayList<Movie>();
        }

        if(lcCurrent==null) {
            lcCurrent=new ArrayList<Movie>();
        }

        lcCurrent.addAll(lcFav);
        adapterList = new MovieAdapterList(MainActivity.this, lcCurrent);
        lvMovies.setAdapter(adapterList);
        adapterGrid = new MovieAdapterGrid(MainActivity.this,lcCurrent);
        grdMovies.setAdapter(adapterGrid);

        mDrawerList.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.drawer_list_item, drawerOptions));

        mDrawerList.setOnItemClickListener(new DrawerItemListener(this));

        if(lcTheaters==null){
            lcTheaters=new ArrayList<Theater>();
            if (new CheckConnection(MainActivity.this).isConnected()) {
                new TheatersScraper(lcTheaters).execute();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Favorite", String.valueOf(resultCode));
        if(resultCode == RESULT_FAVORITE_ADD) {
            ds.addFavoriteMovieToDB(lastMovie);
        } else if(resultCode == RESULT_FAVORITE_REMOVE) {
            ds.removeFavorite(lastMovie);
        }
    }

    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("listM", lcCurrent);
    }

    public void notifyAdapterOfDataChanged(){
        if(grid_visible){
            adapterGrid.notifyDataSetChanged();
        }else
            adapterList.notifyDataSetChanged();
    }

    public ArrayList<Movie> getLcFav() {
        return lcFav;
    }

    public ArrayList<Movie> getLcUpcoming() {
        return lcUpcoming;
    }

    public ArrayList<Movie> getLcMovies() {
        return lcMovies;
    }

    public ArrayList<Movie> getLcCurrent() {
        return lcCurrent;
    }

    public ArrayList<Theater> getLcTheaters() {
        return lcTheaters;
    }
}
