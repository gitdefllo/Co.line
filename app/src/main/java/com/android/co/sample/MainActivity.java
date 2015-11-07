package com.android.co.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.co.line.Coline;
import com.android.co.sample.server.WebUtils;

/****************************************************
 * Co.line
 * -----------
 * @version 0.0.2
 * @author  Fllo (@Gitdefllo) 2015
 *
 * Repository: https://github.com/Gitdefllo/Co.line.git
 *
 *****************************************************
 * Sample project:
 * ---------------
 *
 * Display movies in list by using Co.line library
 * and with themoviedb api (cf. https://themoviedb.org/).
 *
 *****************************************************/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Do a request to api
        Coline.init(this).url("GET", WebUtils.URL_DISCOVER).exec();
    }
}
