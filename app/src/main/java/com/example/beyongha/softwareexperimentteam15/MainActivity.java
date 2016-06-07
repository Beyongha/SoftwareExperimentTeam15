package com.example.beyongha.softwareexperimentteam15;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public String Title1;
    public String Title2;
    public String Title3;

    // 데이터 원본
    ArrayList<String> askList = new ArrayList<String>();


    JSONObject jsonObject;
    final String TAG = "(InfoActivity)DEBUG:";
    String responseString;
    String urlString = "http://172.30.1.49:8080/test/GetTitle.jsp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getTextFromServer();



        //어댑터 준비
        ArrayAdapter<String> Adapter;
        Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, askList);

        //어댑터 연결
        ListView askListView = (ListView)findViewById(R.id.askListView);
        askListView.setAdapter(Adapter);


        //리스트 뷰 속성
        askListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // 항목사이 구분선 지정
        askListView.setDivider(new ColorDrawable(Color.BLACK));

        //구분선 높이 지정
        askListView.setDividerHeight(2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, Menu.NONE, "질문하기");
        menu.add(0, 2, Menu.NONE, "랭킹보기");
        menu.add(0, 3, Menu.NONE, "개인정보");
        menu.add(0, 4, Menu.NONE, "Draw test");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Intent questionIntent = new Intent(MainActivity.this, QuestionActivity.class);
                startActivity(questionIntent);
                break;
            case 2:
                Intent rankIntent = new Intent(MainActivity.this, RankActivity.class);
                startActivity(rankIntent);
                break;
            case 3:
                Intent infoIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(infoIntent);
                break;
            case 4:
                Intent drawIntent = new Intent(MainActivity.this, DrawActivity.class);
                startActivity(drawIntent);
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

                    List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(2);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);
                    responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

                    /////////////////////////////////////////////////////

                    Log.d(TAG, responseString);
                    JSONArray jsonArray = new JSONArray(responseString);
                    jsonObject = jsonArray.getJSONObject(0);
                    Title1 = jsonObject.getString("TITLE");
                    askList.add(Title1);

                    jsonObject = jsonArray.getJSONObject(1);
                    Title2 = jsonObject.getString("TITLE");
                    askList.add(Title2);

                    jsonObject = jsonArray.getJSONObject(2);
                    Title3 = jsonObject.getString("TITLE");
                    askList.add(Title3);


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
