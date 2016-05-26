package com.example.andre.MovieCalendar.utils;



import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
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

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.fragment_theaters, null))
                // Add action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        /*list = (ListView) builder.findViewById(R.id.movie_theaters_list);

        if(list == null){
            Log.d("Erro", "erro");
        }

        adapterList = new TheaterAdapterList(a.getApplicationContext(),setTheaters(theaters));
        list.setAdapter(adapterList);*/


        return builder.create();
    }

    private List<TheaterItem> setTheaters(List<String> theaters){
        List<TheaterItem> list = new ArrayList<TheaterItem>();

        for (int i = 0; i < theaters.size(); i++){
            String nome = theaters.get(i).split(" - ")[0];
            String location = theaters.get(i).split(" - ")[1];
            list.add(new TheaterItem(i, nome, location));
        }

        return list;
    }
}
