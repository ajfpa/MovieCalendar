package com.example.andre.MovieCalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andre.MovieCalendar.utils.DownloadImageTask;
import com.example.andre.MovieCalendar.utils.FragmentTheaters;
import com.example.andre.MovieCalendar.utils.FragmentMovieNotificationPicker;
import com.example.andre.MovieCalendar.utils.ScheduleMovie;
import com.example.andre.MovieCalendar.view.Movie;

public class DetailsActivity extends AppCompatActivity {

    protected TextView tvNome, tvData, tvDirector, tvStarring, tvIntro;
    protected ImageView ivCover;
    protected ListView listView;
    protected long id;
    protected Movie movie;
    Toolbar toolBar;
    private boolean favorite;
    private ScheduleMovie scheduleMovie;

    protected static int RESULT_NOT_CHANGED = 3;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie, menu);

        if(favorite)
            toolBar.getMenu().findItem(R.id.fav).setIcon(R.mipmap.ic_favorite);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.fav:
                addFavorite();

                if(favorite){
                    toolBar.getMenu().findItem(R.id.fav).setIcon(R.mipmap.ic_favorite);
                    if(!movie.isOnDisplay()){
                        DialogFragment dateFragment = new FragmentMovieNotificationPicker();
                        FragmentMovieNotificationPicker m = (FragmentMovieNotificationPicker) dateFragment;
                        m.setDateScheduleMovie(scheduleMovie,movie.getNome());
                        dateFragment.show(getSupportFragmentManager(), "timePicker");
                    }else{
                    DialogFragment theaterFragment = new FragmentTheaters(this, movie.getTheaters(), listView);
                        theaterFragment.show(getSupportFragmentManager(), "theaters");
                    }
                }else {
                    toolBar.getMenu().findItem(R.id.fav).setIcon(R.mipmap.ic_not_favorite);
                }
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_view);

        scheduleMovie = new ScheduleMovie(this);
        scheduleMovie.bindService();

        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvNome = (TextView) findViewById(R.id.tv_movieTitle);
        tvData = (TextView) findViewById(R.id.tv_movieReleaseDate);
        tvDirector = (TextView) findViewById(R.id.tv_movieDirector);
        tvStarring = (TextView) findViewById(R.id.tv_starring);
        tvIntro = (TextView) findViewById(R.id.tv_description);
        ivCover = (ImageView) findViewById(R.id.iv_movieCover);
        //listView = (ListView) findViewById(R.id.movie_theaters_list);

        Intent i = getIntent();
        movie = i.getParcelableExtra("movie");
        favorite = i.getBooleanExtra("favorite", false);

        tvNome.setText(movie.getNome());
        tvData.setText(movie.getData());
        tvDirector.setText("Director: "+ movie.getDirector());
        tvStarring.setText("Starring: " + movie.getStarring());
        tvIntro.setText("Description:\n " + movie.getIntro());
        tvStarring.setMovementMethod(new ScrollingMovementMethod());
        tvIntro.setMovementMethod(new ScrollingMovementMethod());
        if(movie.getImageCover()==null){
            String cover = i.getStringExtra("cover");
            new DownloadImageTask(ivCover,movie).execute(cover);
        }
        else
            ivCover.setImageBitmap(movie.getImageCover());

       /* id = i.getLongExtra("id", 0);
        tvNome.setText(i.getStringExtra("nome"));
        tvData.setText(i.getStringExtra("data"));
        tvDirector.setText("Director: " + i.getStringExtra("director"));
        tvStarring.setText("Starring: " + i.getStringExtra("starring"));
        tvIntro.setText("Description: " + i.getStringExtra("intro"));
        String cover = i.getStringExtra("cover");
        new DownloadImageTask(ivCover).execute(cover);*/

        setResult(RESULT_NOT_CHANGED);
    }

    // Adiciona o contacto e adiciona-o Ã  ListView
    public void addFavorite() {
        if(!favorite){
            favorite = true;
            Intent i = new Intent();
            i.putExtra("recordID", movie.getNome());
            setResult(1, i);
            Toast.makeText(getApplicationContext(), "Movie added to favorites", Toast.LENGTH_SHORT).show();
        }

        else{
            favorite = false;
            setResult(0);
            Toast.makeText(getApplicationContext(), "Movie remove from favorites", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onStop() {
        scheduleMovie.unbindService();
        super.onStop();
    }

    @Override
    protected void onPause() {
        scheduleMovie.unbindService();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        scheduleMovie.unbindService();
        super.onBackPressed();
    }

}
