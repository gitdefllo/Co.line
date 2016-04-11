/*
 * Copyright 2016 Florent Blot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fllo.co.line;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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

/*
 * Co.line
 * -------
 * @version 1.0.13
 * @author  Florent Blot (@Gitdefllo)
 *
 * Android library for HttpURLConnection connections with automatic Thread
 * and Callbacks in chained methods in one line.
 *
 * Repository:    https://github.com/Gitdefllo/Co.line.git
 * Issue tracker: https://github.com/Gitdefllo/Co.line/issues
 *
 */
public class Coline {

    // Tags
    private static final String CO_LINE  = "-- Co.line";

    // Configuration
    private Context        context;
    private String         method;
    private String         route;
    private String         auth;
    private String         token;
    private ContentValues  values;
    private StringBuilder  body = null;
    private CoResponse     response;
    private boolean        used = false;
    private boolean        logs;

    /**
     * <p>
     * Co.line's constructor: method to initiate Coline with actual Context.
     * It creates a new instance of class if this one does not already exist.
     * The Context can be an Activity, a Fragment, a Service or anything. This
     * will be used to return the request response into the main Thread.
     * </p>
     * <p>
     * When this class is initiate, it attaches also a boolean value
     * to des/activate logs and creates a new ContentValues.
     * </p>
     * <p>
     * The ContentValues will be used to store the parameters to pass into the
     * request by {@link #with(ContentValues values)} method.
     * </p>
     *
     * @param context (Context) Actual Context from the call
     * @return        An instance of the class
     */
    public static Coline init(Context context) {
        Coline coline  = new Coline();
        coline.context = context;
        coline.values  = new ContentValues();
        coline.used    = true;
        coline.logs    = CoLogs.getInstance().getStatus();
        return coline;
    }

    /**
     * This method get the status of current logs.
     *
     * @return       A boolean (true: activate, false: disable)
     * @see          CoLogs
     */
    protected boolean getStatusLogs() {
        return CoLogs.getInstance().getStatus();
    }

    /**
     * <p>
     * This method initiates the request method and the URL to do the request.
     * </p>
     * <p>
     * The request method is a static int value from CoHttp class like:<br>
     *      - CoHttp.GET<br>
     *      - CoHttp.POST<br>
     *      - CoHttp.PUT<br>
     *      - CoHttp.DELETE<br>
     *      - CoHttp.HEAD<br>
     * </p>
     *
     * @param method (CoHttp) Value from CoHttp of request method
     * @param route  (String) Value for URL route (should be a String URL as
     *               "http://www.example.com")
     * @return       The current instance of the class
     * @see          CoHttp
     */
    public Coline url(CoHttp method, String route) {
        this.method = method.toString();
        this.route  = route;
        return this;
    }

    /**
     * <p>
     * This method initiates the values to pass into the request from
     * a ContentValues object.
     * </p>
     * <p>
     * See also: with(Object...) and with(ArrayMap&lt;String, Object&gt;)
     * </p>
     *
     * @param values (ContentValues) Values to pass into the request
     * @return       The current instance of the class
     */
    public Coline with(final ContentValues values) {
        if (values.size() <= 0) {
            prepareOnFail("Please check the values sent in \"with(\"ContentValues\")\".");
        }

        this.values = values;
        return this;
    }

    /**
     * <p>
     * This method initiates the values to pass into the request from
     * an Object array. It declared as follows:<br>
     * 'with("key1", value1, "key2", "value2", "key3", values3);'.
     * </p>
     * <p>
     * All the object in the array will be convert to a String.
     * </p>
     * <p>
     * See also: with(ContentValues) and with(ArrayMap&lt;String, Object&gt;)
     * </p>
     *
     * @param values (Object...) Values to pass into the request
     * @return       The current instance of the class
     */
    public Coline with(final Object... values) {
        if (values.length <= 0 || values.length % 2 == 0) {
            prepareOnFail("Please check the values sent in \"with(\"key1\",\"value1\",...)\".");
        }

        for (int i=0; i<values.length; ++i) {
            this.values.put( String.valueOf(values[i]),
                    String.valueOf(values[i + 1]));
            ++i;
        }
        return this;
    }

    /**
     * <p>
     * Only with KitKat version and higher:
     * </p>
     * <p>
     * This method initiates the values to pass into the request from
     * an ArrayMap&lt;String, Object&gt;.
     * </p>
     * <p>
     * All the object in the array will be convert to a String.
     * </p>
     * <p>
     * See also: with(ContentValues) and with(Object...)
     * </p>
     *
     * @param values (ArrayMap) Values to pass into the request
     * @return       The current instance of the class
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public Coline with(final ArrayMap<String, Object> values) {
        if (values.size() <= 0) {
            prepareOnFail("Please check the values sent in \"with(\"ArrayMap<String, Object>\")\".");
        }

        for (int i = 0; i<values.size(); ++i) {
            this.values.put(String.valueOf(values.keyAt(i)),
                    String.valueOf(values.valueAt(i)));
        }
        return this;
    }

    /**
     * This method prepares the header field to authenticate a request.
     *
     * @param auth  (enum) Key method for Authentication from
     *              CoAuth in header field
     * @param token (String) Value of Authentication (should be a String
     *              encoded in BASE64)
     * @return      The current instance of the class
     * @see         CoAuth
     */
    public Coline auth(CoAuth auth, String token) {
        this.auth  = auth.toString();
        this.token = token;
        return this;
    }

    /**
     * This method handles a callback when server returned response.
     *
     * @param response (CoResponse) Interface of successful and errors requests
     * @return         The current instance of the class
     * @see            CoResponse
     */
    public Coline res(CoResponse response) {
        this.response = response;
        return this;
    }

    /**
     * This method executes a request in a new Thread.
     *
     * @see         Thread
     */
    public void exec() {
        if ( logs )
            Log.d(CO_LINE, "Request execution...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                setValues();
                request();
            }
        }).start();
    }

    /**
     * This method creates a current queue to add multiple requests and launch it once.
     *
     * @see         Thread
     */
    public void queue() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CoQueue.init().add(Coline.this);
            }
        }).start();
    }

    /**
     * This method launches all Coline instance.
     *
     * @see         Thread
     */
    public void send() {
        if ( logs )
            Log.d(CO_LINE, "Launch all request in the current queue");

        if (CoQueue.getInstance() == null) {
            Log.i(CO_LINE, "The queue isn't created. Maybe you missed to add " +
                    "a request with 'queue()'.");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                CoQueue.getInstance().start();
            }
        }).start();
    }

    /**
     * Private: This method get all value from {@link #with(ContentValues values)},
     * {@link #with(Object... values)} or {@link #with(ArrayMap<String, Object> values)} and prepares
     * a StringBuilder with all parameter given to send it into the request.
     *
     * @see         StringBuilder
     */
    private void setValues() {
        if (this.values != null && this.values.size() > 0) {
            boolean first_value = true;
            body = new StringBuilder();
            for (Map.Entry<String, Object> entry : this.values.valueSet()) {
                if (!first_value) {
                    body.append('&');
                }
                try {
                    body.append(entry.getKey())
                            .append('=')
                            .append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                } catch(UnsupportedEncodingException e) {
                    if ( logs )
                        Log.e(CO_LINE, e.toString());
                }
                first_value = false;
            }
            if ( logs )
                Log.d(CO_LINE, "Values added to the request body");
        }
    }

    /**
     * Private: This method does a request by HttpURLConnection
     *
     * @see         HttpURLConnection
     */
    private void request() {
        // Prepare timeout variables
        int     MAX_READ_TIMEOUT    = 3000;
        int     MAX_CONNECT_TIMEOUT = 7000;

        String  response = "";
        URL     url = null;

        // Init URL
        try {
            if ( logs )
                Log.d(CO_LINE, "URL: " + route);
            url = new URL( route );
        } catch(MalformedURLException e) {
            if ( logs )
                Log.e(CO_LINE, "error in route: " + e.toString());
        }

        if (url == null) {
            prepareOnFail("An error occurred when trying to get URL");
            return;
        }

        // Do connection
        if ( logs )
            Log.d(CO_LINE, "Do connection...");
        HttpURLConnection http = null;
        try {
            http = (HttpURLConnection) url.openConnection();
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setRequestProperty("Charset", "UTF-8");
            if (auth != null && token != null)
                http.addRequestProperty("Authorization", auth + token);
            http.setReadTimeout(MAX_READ_TIMEOUT);
            http.setConnectTimeout(MAX_CONNECT_TIMEOUT);
            http.setUseCaches(false);
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
        } catch(IOException e) {
            if ( logs )
                Log.e(CO_LINE, "Error in http url connection: " + e.toString());
        }

        if (http == null) {
            prepareOnFail("An error occurred in URL connection");
            return;
        } else {
            if ( logs )
                Log.d(CO_LINE, "Connection etablished");
        }

        // Get response
        InputStream inputStream = null;
        int         status      = 0;
        try {
            status = http.getResponseCode();
            if ( logs )
                Log.d(CO_LINE, "Status response: " + status);
            if (status >= 200 && status < 400) {
                inputStream = http.getInputStream();
            } else {
                inputStream = http.getErrorStream();
            }
        } catch (IOException ex) {
            if ( logs )
                Log.e(CO_LINE, ex.toString());
        }

        if (inputStream == null) {
            prepareOnFail("An error occurred when trying to get server response");
            return;
        }

        // Parse responses
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb      = new StringBuilder();
            String line           = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            inputStream.close();
            response = sb.toString();
            if ( logs )
                Log.d(CO_LINE, response);
        } catch (Exception e) {
            if ( logs )
                Log.e(CO_LINE, "Error when parsing result: " + e.toString());
        }

        if (status != 200 || response.length() == 0 || response.contains("error")) {
            returnFail(response);
            return;
        }

        returnSuccess(response);
    }

    /**
     * Create a JSON to be handled by returnFail(String)
     *
     * @see         {@link #returnFail(String s)}
     */
    private void prepareOnFail(String s) {
        try{
            JSONObject jreturn = new JSONObject(s);
            s = jreturn.toString();
        } catch(JSONException e) {}
        returnFail(s);
    }

    /**
     * Private: This method returns a String response in main Thread.
     *
     * @param s (String) Response of request
     */
    private void returnSuccess(final String s) {
        if (response == null) return;
        if ( logs ) Log.d(CO_LINE, "OK, onSuccess() called");
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                response.onSuccess(s);
                clear();
            }
        });
    }

    /**
     * Private: This method returns a String response in main Thread.
     *
     * @param s (String) Response of request
     */
    private void returnFail(final String s) {
        if (response == null) return;
        if ( logs ) Log.d(CO_LINE, "Oops, onFail() called");
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                response.onFail(s);
                clear();
            }
        });
    }

    /**
     * Private: This method clear the current queue and calls Coline's
     * override finalize method.
     *
     */
    private void clear() {
        clearQueue();
        try {
            finalize();
        } catch (Throwable t) {
            Log.i(CO_LINE, "Clear coline error: " + t.toString());
        }
    }

    /**
     * Private: Call override Object's finalize method for the current queue.
     *
     * @see     {@link CoQueue}
     */
    private void clearQueue() {
        if ( CoQueue.getInstance() == null )
            return;

        if ( logs ) Log.d(CO_LINE, "A current request queue found");

        if ( !CoQueue.getInstance().getPending() ) {
            if ( logs ) Log.d(CO_LINE, "No pending requests left, try to destroy the queue");

            try {
                CoQueue.getInstance().finalize();
            } catch(Throwable t) {
                if ( logs )
                    Log.i(CO_LINE, "Clear queue error: "+t.toString());
            }
        }
    }

    /**
     * Private: This destroys all reference, variable and element of Coline only
     * if Coline is in {@link #used} state.
     *
     * @throws Throwable  Throw an exception when destroyed is compromised
     */
    public void destroyColine() throws Throwable {
        if ( used ) {
            context = null;
            method = null;
            route = null;
            auth = null;
            token = null;
            values = null;
            body = null;
            response = null;
            used = false;
            logs = false;
        }
    }

    /**
     * Protected: This overrides Object's finalize method.
     * This method checks if Coline instance is used and if not, destroy the instance.
     *
     * @throws Throwable  Throw an exception when destroyed is compromised
     */
    @Override
    protected void finalize() throws Throwable {
        if ( used ) {
            try {
                destroyColine();
            }
            catch(Exception ex) {
                Log.i(CO_LINE, "Destroying the current coline not working");
            }
            finally {
                Log.d(CO_LINE, "Current coline is destroyed");
                super.finalize();
            }
        }
    }
}
