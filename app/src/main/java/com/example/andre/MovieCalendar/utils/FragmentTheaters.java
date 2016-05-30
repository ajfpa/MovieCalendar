package com.example.andre.MovieCalendar.utils;



import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.andre.MovieCalendar.DetailsActivity;
import com.example.andre.MovieCalendar.R;
import com.example.andre.MovieCalendar.view.TheaterAdapterList;
import com.example.andre.MovieCalendar.view.TheaterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANDRE on 26/05/16.
 */
public class FragmentTheaters extends DialogFragment {

    private List<String> theaters;
    protected TheaterAdapterList adapterList;
    private ListView list;
    private Activity a;

    public FragmentTheaters(Activity a, List<String> theaters, ListView list){
        this.theaters = theaters;
        this.a = a;
        //this.list = list;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.fragment_theaters, null);
        builder.setView(view);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Cinemas", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        new TestLocation(a).execute();
                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        list = (ListView) view.findViewById(R.id.movie_theaters_list);

        /*list = (ListView) builder.findViewById(R.id.movie_theaters_list);*/

        if(list == null){
            Log.d("Erro", "erro");
        }

        adapterList = new TheaterAdapterList(builder.getContext().getApplicationContext(),setTheaters(theaters));
        list.setAdapter(adapterList);


        return builder.create();
    }

    private List<TheaterItem> setTheaters(List<String> theaters){
        List<TheaterItem> list = new ArrayList<TheaterItem>();

        for (int i = 0; i < theaters.size(); i++){
            String nome = theaters.get(i).split(" - ")[0];
            //String location = theaters.get(i).split(" - ")[1];
            list.add(new TheaterItem(i, nome, "Lisboa"));
        }

        return list;
    }
}
