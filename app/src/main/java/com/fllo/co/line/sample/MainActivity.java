package com.fllo.co.line.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.fllo.co.line.Coline;
import com.fllo.co.line.ColineHttpMethod;
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
    TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Event on success click
        TextView buttonSuccess = (TextView) findViewById(R.id.button_success);
        buttonSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRequest(true);
            }
        });

        // Event on error click
        TextView buttonError = (TextView) findViewById(R.id.button_error);
        buttonError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRequest(false);
            }
        });

        // Text for results
        textResult = (TextView) findViewById(R.id.result_text);
    }

    // Method to get request (true = successful example, false = failed example)
    private void getRequest(boolean request) {
        // Change URL according to our need
        String urlForRequest = WebUtils.URL_DISCOVER;
        if ( !request ) {
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
            textResult.setText("SUCCESS: " + s);
        }
    };

    // Success callback
    private Coline.Error errorCallback = new Coline.Error() {
        @Override
        public void onError(String s) {
            textResult.setText("ERROR: " + s);
        }
    };
}
