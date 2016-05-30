package com.example.andre.MovieCalendar.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.andre.MovieCalendar.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ANDRE on 27/05/16.
 */
public class ForgotLogin extends AsyncTask<String, String, String> {
    private static final String LOGIN_URL = "http://andregloria.com/daam/login.php";

    private String email;
    private Activity a=null;
    private ProgressDialog progressDialog =null;

    public ForgotLogin(Activity a, String email){
        this.email=email;
        this.a=a;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    boolean failure = false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog =null;
        progressDialog = new ProgressDialog(a);
        progressDialog.setMessage(a.getResources().getString(R.string.login_loading));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        try {

            URL url = new URL("http://andregloria.com/daam/forgot.php");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String urlParameters = "user=" + email;
            connection.setRequestMethod("POST");
            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
            connection.setDoOutput(true);
            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
            dStream.writeBytes(urlParameters);
            dStream.flush();
            dStream.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();

            while ((line = br.readLine()) != null) {
                responseOutput.append(line);
            }
            br.close();

            String result = responseOutput.toString();
            System.out.println(result);

            if (result.equals("true")) {
                a.finish();
            } else {
                a.finish();
            }


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String file_url) {
        // dismiss the dialog once product deleted
        if(progressDialog !=null) progressDialog.dismiss();
        if (file_url != null){
            Toast.makeText(a, file_url, Toast.LENGTH_LONG).show();
        }
    }

}

