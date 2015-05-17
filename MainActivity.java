package com.fantasysmash.volleyjson;

import com.fantasysmash.volleyjson.R;
import com.fantasysmash.volleyjson.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class MainActivity extends Activity {

    Button btnLogin;
    EditText userName;
    EditText pwd;

    private static String TAG = MainActivity.class.getSimpleName();
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button)findViewById(R.id.btnlogin);
        userName   = (EditText)findViewById(R.id.userName);
        pwd = (EditText)findViewById(R.id.pwd);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = userName.getText().toString();
                final String password = pwd.getText().toString();
                final String url = "http://52.24.226.232/mlogin?userName="+username+"&pwd="+password;

                // making json object request
                makeJsonObjectRequest(url);
            }
        });

    }

    /**
     * Method to make json object request where json response starts wtih {
     * */
    private void makeJsonObjectRequest(String url) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String passed = response.getString("passed");
                    //String bal = response.getString("bal");

                    if (passed.equals("yes"))
                    {
                        //set session
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        String u = userName.getText().toString();
                        String p = pwd.getText().toString();
                        editor.putString("userName", u);
                        editor.putString("pwd", p);
                        //editor.putString("bal",bal);
                        editor.commit();
                        //switch to home screen
                        Intent myIntent = new Intent(MainActivity.this, Home.class);
                        startActivity(myIntent);


                    } else if (passed.equals("no un")){
                        Toast.makeText(getApplicationContext(),"username not registered",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"incorrect password",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    //checks to see if use
    @Override
    protected void onResume() {
        sharedpreferences=getSharedPreferences("smashPref",
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains("userName"))
        {
            if(sharedpreferences.contains("pwd")){
                Intent i = new Intent(this,
                        Home.class);
                startActivity(i);
            }
        }
        super.onResume();
    }
}