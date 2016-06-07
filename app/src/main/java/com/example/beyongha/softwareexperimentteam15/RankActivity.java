package com.example.beyongha.softwareexperimentteam15;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class RankActivity extends AppCompatActivity {
    public TextView name1, point1;
    public TextView name2, point2;
    public TextView name3, point3;
    public TextView name4, point4;
    public TextView name5, point5;
    public TextView name6, point6;
    public TextView name7, point7;
    public TextView name8, point8;
    public TextView name9, point9;
    public TextView name10, point10;


    String urlString = "http://172.30.177.190:8080/test/GetInfo.jsp";
    final String TAG = "(InfoActivity)DEBUG:";
    String responseString;



    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name1 = (TextView) findViewById(R.id.text_name1);
        name2 = (TextView) findViewById(R.id.text_name2);
        name3 = (TextView) findViewById(R.id.text_name3);

        getTextFromServer();
    }


    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getTextFromServer() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    URI url = new URI(urlString);

                    HttpPost httpPost = new HttpPost();
                    httpPost.setURI(url);

                    HttpResponse response = httpClient.execute(httpPost);
                    responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

                    /////////////////////////////////////////////////////
                    JSONArray jsonArray = new JSONArray(responseString);

                    /*
                    for(int i = 0; i < 3 ; ++ i ) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Log.d(TAG, jsonObject.toString());

                    }
                    */


                    // what to do...
                    name1.setText(jsonArray.getJSONObject(0).getString("ID"));
                    name2.setText(jsonArray.getJSONObject(1).getString("ID"));
                    name3.setText(jsonArray.getJSONObject(2).getString("ID"));

                }   catch (URISyntaxException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                }   catch (ClientProtocolException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                }   catch (JSONException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                }   catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
