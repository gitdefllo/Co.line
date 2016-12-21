package com.fllo.co.line.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fllo.co.line.Coline;
import com.fllo.co.line.builders.HttpMethod;
import com.fllo.co.line.callbacks.Collback;
import com.fllo.co.line.callbacks.ObjCollback;
import com.fllo.co.line.results.Error;
import com.fllo.co.line.results.Response;

import com.fllo.co.line.sample.models.MovieObject;
import com.fllo.co.line.sample.utils.UrlsUtils;

public class SingleRequestActivity extends AppCompatActivity {

    private Coline coline;
    private TextView textResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        coline = Coline.init(this);
        textResults = (TextView) findViewById(R.id.result_text);
        // enable debug mode
        Coline.enableDebug();
    }

    public void getSuccessulRequest(View view) {
        // set up one coline request and attach method with an URL
        coline.url(HttpMethod.GET, UrlsUtils.URL_SINGLE_MOVIE)
                // prepare the callback response
                .res(new Collback() {
                    @Override
                    public void onResult(Response res, Error err) {
                        // handle possible errors
                        if (err != null) {
                            handleError(err.exception, err.status, err.description);
                            return;
                        }

                        // handle successful response
                        handleSuccess(res.status, res.body);
                    }
                }).exec(); // execute the request
    }

    public void getNotFoundRequest(View view) {
        coline.url(HttpMethod.GET, UrlsUtils.URL_FAKE_ERROR)
                .res(new Collback() {
                    @Override
                    public void onResult(Response res, Error err) {
                        if (err != null) {
                            handleError(err.exception, err.status, err.description);
                            return;
                        }

                        handleSuccess(res.status, res.body);
                    }}
                ).exec();
    }

    public void getObjectResult(View view) {
        String urlString = UrlsUtils.URL_SINGLE_MOVIE;

        coline.url(HttpMethod.GET, urlString)
                .res(new ObjCollback<MovieObject>() {
                    @Override
                    public void onResult(MovieObject obj, Error err) {
                        if (err != null) {
                            handleError(err.exception, err.status, err.description);
                            return;
                        }

                        handleObject(obj);
                    }
                }).exec();
    }

    private void handleObject(MovieObject mo) {
        textResults.setText(
                String.format(
                        getResources().getString(R.string.label_res_object),
                                mo.getId(), mo.getTitle(), mo.getOverview(),
                                mo.getVote_average(),mo.getPoster_path()));
        textResults.setTextColor(Color.parseColor("#212121"));
    }

    private void handleSuccess(int status, String body) {
        textResults.setText(
                String.format(
                        getResources().getString(R.string.label_res_success),
                        status, body));
        textResults.setTextColor(Color.parseColor("#212121"));
    }

    private void handleError(String exception, int status, String description) {
        textResults.setText(
                String.format(
                        getResources().getString(R.string.label_res_error),
                        exception, status, description));
        textResults.setTextColor(Color.parseColor("#650000"));
    }

    private void clearFields() {
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
                clearFields();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        if (coline != null) {
            coline.cancel();
            coline = null;
        }
        super.onDestroy();
    }
}
