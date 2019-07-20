package com.example.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
{
    EditText MycityName;
    TextView resultText;
    Button btn;
    String cityName;
    public void findWeather(View view)
    {
        InputMethodManager img= (InputMethodManager )getSystemService ( Context.INPUT_METHOD_SERVICE );  // this code for hide the keyword when user enter city name
        img.hideSoftInputFromWindow ( MycityName.getWindowToken (),0 );

        try {
            String encodeCityName= URLEncoder.encode ( MycityName.getText().toString(), "UTF-8");
            download dn = new download ();
            dn.execute ( "http://api.openweathermap.org/data/2.5/weather?q=" + encodeCityName + "&APPID=ea574594b9d36ab688642d5fbeab847e" );

        } catch (UnsupportedEncodingException e)
        {
            Toast.makeText ( getApplicationContext () , "Could Not Find The Weather" , Toast.LENGTH_SHORT ).show ();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        MycityName=(EditText) findViewById ( R.id.editText );
        resultText = (TextView)findViewById ( R.id.resulttextview );
        btn=(Button)findViewById ( R.id.btn );

        MycityName.addTextChangedListener ( new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s , int start , int count , int after) {

            }

            @Override
            public void onTextChanged(CharSequence s , int start , int before , int count)
            {
                String cityname=MycityName.getText ().toString ().trim ();
                btn.setEnabled ( !cityname.isEmpty () );

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );


    }
    public class download extends AsyncTask <String ,  Void , String >
    {
        @Override
        protected String doInBackground(String... strings)
        {
            try {
                String result = "";
                URL url = new URL ( strings[0] );
                HttpURLConnection httpURLConnection = ( HttpURLConnection ) url.openConnection ();
                InputStream in = httpURLConnection.getInputStream ();
                InputStreamReader readr = new InputStreamReader ( in );

                int data = readr.read ();
                while (data != -1) {
                    char current = ( char ) data;
                    result += current;
                    data = readr.read ();

                }

                return  result;
            }
            catch (Exception e)
            {
                Toast.makeText ( getApplicationContext () , "Could Not Find The Weather" , Toast.LENGTH_SHORT ).show ();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String result)
        {

                String message = "";
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject ( result );
                    String wether = jsonObject.getString ( "weather" );
                    Log.i ( "content" , wether );

                    JSONArray jsonArray = new JSONArray ( wether );

                    for (int i = 0; i <= jsonArray.length (); i++) {
                        JSONObject jpartObj = jsonArray.getJSONObject ( i );


                        String main = jpartObj.getString ( "main" );
                        String des = des = jpartObj.getString ( "description" );

                        Log.i ( "main" , jpartObj.getString ( "main" ) );
                        Log.i ( "des" , jpartObj.getString ( "description" ) );

                        if (main != "" && des != "") {
                            message = main + " : " + des + "\r\n";
                            resultText.setText ( message );
                        } else {
                            Toast.makeText ( MainActivity.this , "Can't find Weather" , Toast.LENGTH_SHORT ).show ();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace ();
                }
            }


        }


    }

