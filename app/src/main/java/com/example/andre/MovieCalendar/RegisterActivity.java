package com.example.andre.MovieCalendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andre.MovieCalendar.utils.CheckConnection;
import com.example.andre.MovieCalendar.utils.Utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ANDRE on 12/05/16.
 */
public class RegisterActivity extends AppCompatActivity {

    EditText username,password,confirmPassword, firstName, lastName;
    AutoCompleteTextView email;
    CheckBox terms;
    Boolean checkedTerms=false;
    Toast toast;
    CheckConnection connection;
    private ProgressDialog progressDialog =null;

    String usernameStr,emailStr,passwordStr,confirmPasswordStr,firstNameStr,lastNameStr;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        Button register = (Button) findViewById(R.id.bRegister);
        username=(EditText)findViewById(R.id.etUsername);
        email=(AutoCompleteTextView)findViewById(R.id.etEmail);
        password=(EditText)findViewById(R.id.etPassword);
        confirmPassword=(EditText)findViewById(R.id.etConfirmPassword);
        firstName=(EditText)findViewById(R.id.etFirstName);
        lastName=(EditText)findViewById(R.id.etLastName);
        terms = (CheckBox) findViewById(R.id.checkTerms);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedTerms = ((CheckBox) v).isChecked();
            }
        });
        terms.setLongClickable(true);

        register.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //checkConnection
                connection = new CheckConnection(getApplicationContext());
                if (!connection.isConnected()) {
                    createToast(getResources().getString(R.string.no_connection));
                    return;
                }

                if (!checkFields()) {
                    return;

                } else {
                    new TestRegister().execute();


                }
            }
        });


    }

    public Boolean checkFields(){

        usernameStr=username.getText().toString();
        emailStr=email.getText().toString();
        passwordStr=password.getText().toString();
        confirmPasswordStr=confirmPassword.getText().toString();
        firstNameStr=firstName.getText().toString();
        lastNameStr=lastName.getText().toString();

        // check if any of the fields are vaccant
        if(usernameStr.equals("")||passwordStr.equals("")||confirmPasswordStr.equals("")||
                emailStr.equals("") || firstNameStr.equals("") || lastNameStr.equals("")){
            createToast(getResources().getString(R.string.enter_all_fields));
            return false;
        }

        //See if username is valid
        String pattern= "^[a-zA-Z0-9]*$";
        if(!usernameStr.matches(pattern)){
            createToast("Invalid Username!");
            return false;
        }
        //check if email is valid
        if(!Utils.checkEmail(emailStr)){
            createToast("Invalid Email!");
            return false;
        }

        //check password lenght
        if(passwordStr.length()<6){
            createToast("Password needs at least 5 characters");
            return false;
        }else if(passwordStr.length()>15){
            createToast("Password must be under 15 characters");
            return false;
        }

        // check if both password matches
        if(!passwordStr.equals(confirmPasswordStr)){
            createToast("Passwords don't match");
            return false;
        }


        if(!checkedTerms){
            createToast("You have to accept Terms&Conditions");
            return false;
        }

        return true;
    }

    public void createToast(String s){
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public class TestRegister extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog =null;
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Attempting register...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {

            String passwordMD5 = Utils.MD5(passwordStr);
            try {

                URL url = new URL("http://andregloria.com/daam/register.php");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters = "user=" + usernameStr + "&email=" + emailStr + "&pass=" + passwordMD5 + "&first=" + firstNameStr + "&last=" + lastNameStr;
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
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    finish();
                    startActivity(i);
                }else{
                    createToast("Error!");
                }
            } catch (Exception e) {
                return null;
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
                Toast.makeText(getApplicationContext(), file_url, Toast.LENGTH_LONG).show();
            }
        }

    }
}
