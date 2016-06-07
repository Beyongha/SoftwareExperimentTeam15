package com.example.beyongha.softwareexperimentteam15;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AnswerActivity extends AppCompatActivity {
    public TextView eTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        eTextView = (TextView) findViewById(R.id.etextview);

        Intent intent = getIntent();
        eTextView.setText(intent.getStringExtra("textin"));
    }
}
