package com.android.co.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.co.line.Coline;
import com.android.co.line.ColineHttpMethod;
import com.android.co.sample.utils.WebUtils;

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

        // Enable the logs
        Coline.activateLogs(true);
        // Initialize Coline
        Coline.init(this)
                // Prepare method and URL
                .url(ColineHttpMethod.GET, WebUtils.URL_DISCOVER)
                // Execute the request
                .exec();
    }
}
