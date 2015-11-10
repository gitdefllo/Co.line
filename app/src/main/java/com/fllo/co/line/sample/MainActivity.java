package com.fllo.co.line.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/****************************************************
 * Co.line Sample
 * --------------
 * @author  Fllo (@Gitdefllo) 2015
 *
 * Repository: https://github.com/Gitdefllo/Co.line.git
 *
 *****************************************************
 * Sample project:
 * ---------------
 *
 * Get a movies list by using Co.line library
 * with themoviedb api (cf. https://themoviedb.org/).
 *
 *****************************************************/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Event on success click
        TextView buttonSuccess = (TextView) findViewById(R.id.button_success);
        buttonSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSuccessRequest();
            }
        });

        // Event on error click
        TextView buttonError = (TextView) findViewById(R.id.button_error);
        buttonError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getErrorRequest();
            }
        });
    }

    // Method to get success example
    private void getSuccessRequest() {
//        // Enable the logs
//        Coline.activateLogs(true);
//        // Initialize Coline
//        Coline.init(this)
//                // Prepare method and URL
//                .url(ColineHttpMethod.GET, WebUtils.URL_DISCOVER)
//                // Execute the request
//                .exec();
    }

    // Method to get error example
    private void getErrorRequest() {
//        // Enable the logs
//        Coline.activateLogs(true);
//        // Initialize Coline
//        Coline.init(this)
//                // Prepare method and URL
//                .url(ColineHttpMethod.GET, WebUtils.URL_DISCOVER)
//                // Execute the request
//                .exec();
    }
}
