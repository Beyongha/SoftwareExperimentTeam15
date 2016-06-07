package com.example.beyongha.softwareexperimentteam15;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.content.SharedPreferences;

// 안드로이드 6.0버전부터 HttpResponse가 사라졌다
// 그래서 이것을 해결하기 위해 gradle파일을 변경하였다
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


import org.json.JSONObject;

public class InfoActivity extends AppCompatActivity {
    public TextView t_id;
    public TextView t_name;
    public TextView t_nickname;
    public TextView t_birthday;
    public TextView t_point;

    JSONObject jsonObject;
    final String TAG = "(InfoActivity)DEBUG:";
    String responseString;
    String urlString = "http://172.:8080/test/GetInfo.jsp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        t_id = (TextView)findViewById(R.id.t_id);
        t_nickname = (TextView)findViewById(R.id.t_nickname);
        t_birthday = (TextView)findViewById(R.id.t_birthday);
        t_name = (TextView)findViewById(R.id.t_name);
        t_point = (TextView)findViewById(R.id.t_point);

        getTextFromServer();
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

                    List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("userId", "ID1"));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);
                    responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

                    /////////////////////////////////////////////////////

                    //Log.d(TAG, responseString);
                    JSONArray jsonArray = new JSONArray(responseString);
                    jsonObject = jsonArray.getJSONObject(0);

                    //Log.d(TAG, jsonObject.toString());
                    setTexts();

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

    public void setTexts() {
        //Log.d(TAG, jsonObject.toString());
        try {
            t_id.setText(jsonObject.getString("ID"));
            t_birthday.setText(jsonObject.getString("BIRTHDAY"));
            t_name.setText(jsonObject.getString("NAME"));
            t_nickname.setText(jsonObject.getString("NICKNAME"));
            t_point.setText(jsonObject.getString("POINT"));
        }   catch (JSONException je) {
            je.printStackTrace();
        }   catch (Exception e) {
            e.printStackTrace();
        }

    }
}
