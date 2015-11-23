package com.fllo.co.line.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.fllo.co.line.Coline;
import com.fllo.co.line.ColineHttpMethod;
import com.fllo.co.line.ColineQueue;
import com.fllo.co.line.sample.utils.WebUtils;

/*
 * Co.line Sample
 * --------------
 * @author  Fllo (@Gitdefllo) 2015
 *
 * Get a movies list by using Co.line library with themoviedb (https://themoviedb.org/).
 * Repository: https://github.com/Gitdefllo/Co.line.git
 *
 */
public class MainActivity extends AppCompatActivity {

    // Init composants
    private boolean  isSuccess = true;
    private int      countRequests = 0;
    // Init layout
    private TextView textResult,
                     textMultipleResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                if ( isSuccess )
                    isSuccess = false;
                else
                    isSuccess = true;
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
        String urlForRequest = WebUtils.URL_SINGLE_MOVIE;
        if ( !type ) {
            urlForRequest = WebUtils.URL_FAKE_URL;
        }
        // Enable the logs
        Coline.activateLogs(true);
        // Initialize Coline
        Coline.init(this)
                // Prepare method and URL
                .url(ColineHttpMethod.GET, urlForRequest)
                // Retrieve result in success callback
                .success(successCallback)
                // Retrieve result in error callback
                .error(errorCallback)
                // Execute the request
                .exec();
    }

    // Success callback
    private Coline.Success successCallback = new Coline.Success() {
        @Override
        public void onSuccess(String s) {
            // Retrieve the datas in String
            textResult.setText("SUCCESS: " + s);
        }

        @Override
        public void onClear() { }
    };

    // Error callback
    private Coline.Error errorCallback = new Coline.Error() {
        @Override
        public void onError(String s) {
            // Retrieve the datas in String
             textResult.setText("ERROR: " + s);
        }
    };

    // Method to add request to the current queue
    private void addRequestToQueue(boolean type) {
        // Change URL according to our need
        String urlForRequest = WebUtils.URL_DISCOVER;
        if ( !type ) {
            urlForRequest = WebUtils.URL_FAKE_URL;
        }

        // Enable the logs
        ColineQueue.activateLogs(true);
        // Initialize Coline with a Queue
        Coline.init(this)
                // Prepare method and URL
                .url(ColineHttpMethod.GET, urlForRequest)
                // Retrieve result in success callback
                .success(successQueueCallback)
                // Retrieve result in error callback
                .error(errorQueueCallback)
                // Add the request to the current queue
                .queue();
    }

    private void launchQueue() {
        countRequests = 0;
        ColineQueue.init(getApplicationContext()).start();
    }

    // Success callback for multiple requests
    private Coline.Success successQueueCallback = new Coline.Success() {
        @Override
        public void onSuccess(String s) {
            countRequests += 1;
            // Retrieve the datas in String
            String sample = textMultipleResults.getText().toString();
            sample += "Request no." + countRequests + " \n"
                    + "SUCCESS:" + s.substring(0, 50) + "..."
                    + "\n\n ------------- \n\n";
            textMultipleResults.setText(sample);
        }

        @Override
        public void onClear() { }
    };

    // Error callback for multiple requests
    private Coline.Error errorQueueCallback = new Coline.Error() {
        @Override
        public void onError(String s) {
            countRequests += 1;
            // Retrieve the datas in String
            String sample = textMultipleResults.getText().toString();
            sample += "Request no." + countRequests + " \n"
                    + "ERROR:" + s.substring(0, 50) + "..."
                    + "\n------------\n\n";
            textMultipleResults.setText(sample);
        }
    };
}
