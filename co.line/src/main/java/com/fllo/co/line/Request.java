package com.fllo.co.line;

import android.content.ContentValues;
import android.util.Log;

import com.fllo.co.line.results.Error;
import com.fllo.co.line.results.Response;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Observable;

public class Request extends Observable {

    // Tags
    private static final String CO_LINE  = "Co.line";

    // Configuration
    protected Error err;
    protected Response res;
    private String method;
    private String route;
    private ContentValues headers;
    private StringBuilder body;
    private boolean logs;

    /**
     * <p>
     * Request's constructor will do the request to the server.
     * It needs the HTTP verb ({method}), the route URL ({route}),
     * the header properties ({headers}) and the current state of
     * Logs ({logs}).
     * </p>
     *
     * @param method (String) HttpMethod verb using by the request
     * @param route (String) URL of the request
     * @param headers (ContentValues) Header properties as "Content-type" or "Authorization"
     * @param logs (boolean) Current state of Logs
     */
    public Request(String method, String route,
                   ContentValues headers, boolean logs) {
        this.method = method;
        this.route = route;
        this.headers = headers;
        this.logs = logs;
    }

    /**
     * <p>
     * Protected: This method get all value from {ContentValues values} and prepares
     * a StringBuilder with all parameter given to send it into the request.
     * </p>
     *
     * @param values (ContentValues) Body parameters
     * @see          StringBuilder
     */
    protected void setValues(ContentValues values) {
        if (values != null && values.size() > 0) {
            boolean first_value = true;
            body = new StringBuilder();
            for (Map.Entry<String, Object> entry : values.valueSet()) {
                if (!first_value) {
                    body.append('&');
                }
                try {
                    body.append(entry.getKey())
                            .append('=')
                            .append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    if ( logs ) Log.e(CO_LINE, e.toString());
                }
                first_value = false;
            }
            if ( logs ) Log.d(CO_LINE, "Values added to the request body: " + body.toString());
        }
    }

    /**
     * Protected: This method does a request by HttpURLConnection
     *
     * @see         HttpURLConnection
     */
    protected void makeRequest() {
        // Prepare timeout variables
        int MAX_READ_TIMEOUT = 3000;
        int MAX_CONNECT_TIMEOUT = 7000;
        int NO_STATUS = 0;

        String response;
        URL url;

        // Init URL
        try {
            if ( logs ) Log.d(CO_LINE, "URL: " + route);

            url = new URL( route );
        } catch (MalformedURLException e) {
            if ( logs ) Log.e(CO_LINE, "error in route: " + e.toString());

            setErrorResult("MalformedURLException", e.toString(),
                    "An error occurred when trying to get URL", NO_STATUS);
            return;
        }

        // Do connection
        if ( logs ) Log.d(CO_LINE, "Do connection...");

        HttpURLConnection http;
        try {
            http = (HttpURLConnection) url.openConnection();

            // Adding header properties
            if (headers.size() > 0) {
                for (Map.Entry<String, Object> entry : headers.valueSet()) {
                    http.setRequestProperty(entry.getKey(), String.valueOf(entry.getValue()));
                }
            } else {
                http.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;Charset=UTF-8");
            }

            http.setReadTimeout(MAX_READ_TIMEOUT);
            http.setConnectTimeout(MAX_CONNECT_TIMEOUT);
            http.setUseCaches(true);
            http.setDoInput(true);

            // Send value when needed
            if ( body != null ) {
                http.setDoOutput(true);
                http.setRequestMethod( method );
                DataOutputStream wr = new DataOutputStream( http.getOutputStream() );
                wr.writeBytes( body.toString() );
                wr.flush();
                wr.close();
            }

        } catch (IOException e) {
            if ( logs ) Log.e(CO_LINE, "Error in http url connection: " + e.toString());

            setErrorResult("IOException", e.toString(),
                    "Error in http url connection", NO_STATUS);
            return;
        }

        if ( logs ) Log.d(CO_LINE, "Connection etablished");

        // Get response
        InputStream inputStream;
        int status = 0;
        try {
            status = http.getResponseCode();
            if ( logs ) Log.d(CO_LINE, "Status response: " + status);

            if (status >= 200 && status < 400) {
                inputStream = http.getInputStream();
            } else {
                inputStream = http.getErrorStream();
            }

        } catch (IOException e) {
            if ( logs ) Log.e(CO_LINE, "Error when getting server response: " + e.toString());

            setErrorResult("IOException", e.toString(),
                    "An error occurred when trying to get server response", status);
            return;
        }

        if (inputStream == null) {
            setErrorResult("NullPointerException", null,
                    "An error occurred when trying to get server response", status);
            return;
        }

        // Parse responses
        try {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            inputStream.close();
            response = sb.toString();
        } catch (Exception e) {
            if ( logs ) Log.e(CO_LINE, "Error when parsing result: " + e.toString());

            setErrorResult("Exception", e.toString(),
                    "An error occurred when reading server response", status);
            return;
        }

        // Handle server response
        if ( logs ) Log.d(CO_LINE, response);

        if (status >= 200 && status <= 299) {
            setResponseResult(response, status);
            return;
        }

        setErrorResult(null, null, response, status);
    }

    /**
     * Private: Create an error object to be handled by Collback.onResult()
     */
    private void setErrorResult(String exception, String stacktrace, String message, int status) {
        if (message == null || message.length() == 0) {
            message = "An error occurred when trying to contact the server";
        }
        this.err = new Error(status, exception, stacktrace, message);
        this.res = null;
        setChanged();
        notifyObservers();
    }

    /**
     * Private: Create an error object to be handled by Collback.onResult()
     */
    private void setResponseResult(String response, int status) {
        this.err = null;
        this.res = new Response(status, response);
        setChanged();
        notifyObservers();
    }
}
