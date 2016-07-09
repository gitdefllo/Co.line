package com.fllo.co.line.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fllo.co.line.CoHttp;
import com.fllo.co.line.CoLogs;
import com.fllo.co.line.CoResponse;
import com.fllo.co.line.Coline;

/*
 * Co.line Sample
 * --------------
 * @author  Fllo (@Gitdefllo) 2016
 *
 * Get a movies list by using Co.line library with themoviedb (https://themoviedb.org/).
 * Repository: https://github.com/Gitdefllo/Co.line.git
 * Issue tracker: https://github.com/Gitdefllo/Co.line/issues
 *
 */
public class MainActivity extends AppCompatActivity {

    // Tags
    private static final String COLINE_TAG = "-- MainActivity";

    // Base URL
    private static final String URL_BASE = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "?api_key=9e1143bf079e1ca43547f56fc41bb4dc";

    // URLs
    public static final String URL_DISCOVER = URL_BASE + "discover/movie" + API_KEY;
    public static final String URL_SINGLE_MOVIE = URL_BASE + "movie/9693" + API_KEY;
    public static final String URL_FAKE_URL = URL_BASE + "error/" + API_KEY;

    // Init composants
    private boolean  isSuccess = true;
    private int      countRequests = 0;

    // Init layout
    private TextView textResult, textMultipleResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable the debugs logs for Co.line
        CoLogs.activate();

        // Event on success click
        TextView buttonSuccess = (TextView) findViewById(R.id.button_success);
        buttonSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequest(true);
            }
        });

        // Event on error click
        TextView buttonError = (TextView) findViewById(R.id.button_error);
        buttonError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequest(false);
            }
        });

        // Event on adding request to queue
        TextView buttonAddToQueue = (TextView) findViewById(R.id.button_add_request);
        buttonAddToQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRequestToQueue(isSuccess);
                isSuccess = !isSuccess;
            }
        });

        // Event on adding request to queue
        TextView buttonLaunchQueue = (TextView) findViewById(R.id.button_launch_queue);
        buttonLaunchQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchQueue();
            }
        });

        // Text for results
        textResult = (TextView) findViewById(R.id.result_text);
        textResult.setMovementMethod(new ScrollingMovementMethod());
        textMultipleResults = (TextView) findViewById(R.id.info_queue_text);
        textMultipleResults.setMovementMethod(new ScrollingMovementMethod());
    }

    // Method to get request (true = successful example, false = failed example)
    private void setRequest(boolean type) {
        // Change URL according to our need
        String urlForRequest = URL_SINGLE_MOVIE;
        if ( !type ) {
            urlForRequest = URL_FAKE_URL;
        }

        // Initialize Coline
        Coline.init(this)
                // Prepare method and URL
                .url(CoHttp.GET, urlForRequest)
                // General callback
                .res(singleResponse)
                // Execute the request
                .exec();
    }

    // General callback
    private CoResponse singleResponse = new CoResponse() {
        @Override
        public void onSuccess(String s) {
            // Called when connection is successful
            textResult.setText("SUCCESS: " + s);
            textResult.setTextColor(Color.parseColor("#212121"));
        }

        @Override
        public void onFail(String s) {
            // Called when connection returns an error
            Log.e(COLINE_TAG, "FAIL: " + s);
            textResult.setText("ERROR: " + s);
            textResult.setTextColor(Color.parseColor("#650000"));
        }
    };

    // Method to add request to the current queue
    private void addRequestToQueue(boolean type) {
        // Change URL according to our need
        String urlForRequest = URL_DISCOVER;
        if ( !type ) {
            urlForRequest = URL_FAKE_URL;
        }

        // Initialize Coline with a Queue
        Coline.init(this)
                // Prepare method and URL
                .url(CoHttp.GET, urlForRequest)
                // Retrieve queue results in response callback
                .res(queueResponse)
                // Add the request to the current queue
                .queue();
    }

    private void launchQueue() {
        countRequests = 0;
        Coline.init(this).send();
    }

    // General callback
    private CoResponse queueResponse = new CoResponse() {
        @Override
        public void onSuccess(String s) {
            // Called when connection is successful
            countRequests += 1;
            // Retrieve the datas in String
            String sample = textMultipleResults.getText().toString();
            sample += "Request no." + countRequests + " \n"
                    + "SUCCESS:" + s.substring(0, 50) + "...\n"
                    + "-------------\n";
            textMultipleResults.setText(sample);
        }

        @Override
        public void onFail(String s) {
            // Called when connection returns an error in Co.line side
            countRequests += 1;
            // Retrieve the datas in String
            String sample = textMultipleResults.getText().toString();
            sample += "Request no." + countRequests + " \n"
                    + "FAIL:" + s.substring(0, 50) + "...\n"
                    + "------------\n";
            textMultipleResults.setText(sample);
        }
    };

    private void onClearForms() {
        textResult.setText("");
        textMultipleResults.setText("");
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
