package com.fllo.co.line.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fllo.co.line.Coline;
import com.fllo.co.line.builders.CoHttp;
import com.fllo.co.line.builders.CoLogs;
import com.fllo.co.line.callbacks.CoCallback;
import com.fllo.co.line.models.CoError;
import com.fllo.co.line.models.CoResponse;
import com.fllo.co.line.sample.utils.UrlsConstants;

public class SimpleRequestActivity extends AppCompatActivity {

    private TextView textResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        // Enable the debugs logs for Co.line
        CoLogs.activate();

        textResults = (TextView) findViewById(R.id.result_text);
    }

    public void getSuccessRequest(View view) {
        // Initialize Coline
        Coline.init(this)
                // Prepare method and URL
                .url(CoHttp.GET, UrlsConstants.URL_SINGLE_MOVIE)
                // General callback
                .res(simpleResponse)
                // Execute the request
                .exec();
    }

    public void getErrorRequest(View view) {
        // Initialize Coline
        Coline.init(this)
                // Prepare method and URL
                .url(CoHttp.GET, UrlsConstants.URL_FAKE_ERROR)
                // General callback
                .res(simpleResponse)
                // Execute the request
                .exec();
    }

    // General callback
    private CoCallback simpleResponse = new CoCallback() {
        @Override
        public void onResult(CoError err, CoResponse res) {
            // Handle errors
            if (err != null) {
                String result = "(Error):\n\tStatus: "+ err.status + "\n\tException: " + err.exception + "\n\tError: " + err.description;
                textResults.setText(result);
                textResults.setTextColor(Color.parseColor("#650000"));
                return;
            }

            // Handle successful response
            String result = "(Success):\n\tStatus: "+ res.status + "\n\tBody: " + res.body;
            textResults.setText(result);
            textResults.setTextColor(Color.parseColor("#212121"));
        }
    };

    private void onClearForms() {
        textResults.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                onClearForms();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
