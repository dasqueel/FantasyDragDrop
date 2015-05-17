package com.fantasysmash.volleyjson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fantasysmash.volleyjson.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;


public class Home extends Activity {

    TextView userName;
    TextView bal;
    TextView setEdit;
    TextView findMatches;
    TextView potential;
    TextView myContests;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        userName   = (TextView)findViewById(R.id.userName);
        bal = (TextView)findViewById(R.id.bal);
        setEdit = (TextView)findViewById(R.id.setEdit);
        findMatches = (TextView)findViewById(R.id.findMatches);
        potential = (TextView)findViewById(R.id.potential);
        myContests = (TextView)findViewById(R.id.myContests);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userText = prefs.getString("nameKey", null);
        userName.setText(userText);

        //get request to get balance
        getBal("http://52.24.226.232/mgetBal?userName="+userText);

        myContests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Home.this, Contests.class);
                startActivity(homeIntent);
            }
        });

        setEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setIntent = new Intent(Home.this, TestBedDSLV.class);
                startActivity(setIntent);
            }
        });


    }
    private void getBal(String url) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("myTag", response.toString());

                try {
                    String balText = response.getString("bal");
                    bal.setText(balText);
                }
                catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("getbal", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
