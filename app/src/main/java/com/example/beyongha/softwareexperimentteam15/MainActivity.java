package com.example.beyongha.softwareexperimentteam15;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    public String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
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
}
