package com.fllo.co.line.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void seeSimpleRequest(View view) {
        startActivity(new Intent(this, SingleRequestActivity.class));
    }

    public void seeQueueRequest(View view) {
        startActivity(new Intent(this, MultipleRequestsActivity.class));
    }
}
