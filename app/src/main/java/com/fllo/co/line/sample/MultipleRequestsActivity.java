package com.fllo.co.line.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fllo.co.line.Coline;
import com.fllo.co.line.builders.HttpMethod;
import com.fllo.co.line.callbacks.Collback;
import com.fllo.co.line.results.Error;
import com.fllo.co.line.results.Response;
import com.fllo.co.line.sample.utils.UrlsUtils;

public class MultipleRequestsActivity extends AppCompatActivity {

    private boolean isSuccessfulRequest = true;
    private int countRequests = 0;

    private TextView textResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        // Enable the debugs logs for Co.line
        Coline.enableDebug();

        textResults = (TextView) findViewById(R.id.result_text);
        textResults.setMovementMethod(new ScrollingMovementMethod());
    }

    public void addRequestToQueue(View view) {
        String urlForRequest = UrlsUtils.URL_DISCOVER;
        if ( !isSuccessfulRequest ) {
            urlForRequest = UrlsUtils.URL_FAKE_ERROR;
        }

        // Initialize Coline with a Queue
        Coline.init(this)
                // Prepare method and URL
                .url(HttpMethod.GET, urlForRequest)
                // Retrieve queue results in response callback
                .res(queueCallback)
                // Add the request to the current queue
                .queue();

        isSuccessfulRequest = !isSuccessfulRequest;
    }

    public void launchRequestQueue(View view) {
        countRequests = 0;
        Coline.init(this).send();
    }

    // General callback
    private Collback queueCallback = new Collback() {
        @Override
        public void onResult(Response res, Error err) {
            countRequests += 1;

            // Handle errors
            if (err != null) {
                String result = textResults.getText().toString();
                result += "Request n." + countRequests + " (Error):\n\tStatus: "+ err.status + "\n\tDescription: " + err.description + "\n-------------\n";
                textResults.setText(result);
                return;
            }

            // Handle successful response
            String result = textResults.getText().toString();
            result += "Request n." + countRequests + " (Success):\n\tStatus: "+ res.status + "\n\tBody: " + res.body.substring(0, 50) + "\n-------------\n";
            textResults.setText(result);
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
