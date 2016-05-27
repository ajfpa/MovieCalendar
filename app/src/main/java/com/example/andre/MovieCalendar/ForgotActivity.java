package com.example.andre.MovieCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andre.MovieCalendar.utils.CheckConnection;
import com.example.andre.MovieCalendar.utils.ForgotLogin;
import com.example.andre.MovieCalendar.utils.TestLogin;

/**
 * Created by ANDRE on 27/05/16.
 */
public class ForgotActivity extends Activity {

    Button forgotButton, loginButton;
    AutoCompleteTextView email=null;
    private Toast toast;
    CheckConnection connection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.activity_forgot);
        email = (AutoCompleteTextView) findViewById(R.id.email);
        forgotButton = (Button) findViewById(R.id.newPassword);
        loginButton = (Button) findViewById(R.id.backLogin);

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check Connection
                connection = new CheckConnection(getApplicationContext());
                if(connection.isConnected()) {
                    String userText = email.getText().toString();

                    if(userText.equals("")){
                        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
                            toast = Toast.makeText(getApplicationContext(), "Enter all fields!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }else {
                        new ForgotLogin(ForgotActivity.this, userText).execute();
                    }
                }else{
                    if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
                        toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
                        toast.show();
                    }

                }
            }
        });

        // Enter as guest
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
    }
}


