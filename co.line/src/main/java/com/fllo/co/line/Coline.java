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

import com.fllo.co.line.builders.CoHttp;
import com.fllo.co.line.builders.CoLogs;
import com.fllo.co.line.callbacks.CoCallback;
import com.fllo.co.line.models.CoError;
import com.fllo.co.line.models.CoResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Co.line
 * -------
 * @version 2.0.0
 * @author Florent Blot (@Gitdefllo)
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
    private static final String CO_LINE  = "Co.line";

    // Configuration
    private WeakReference<Context> context;
    private Thread thread;
    private String method;
    private String route;
    private ContentValues values, headers;
    private StringBuilder body = null;
    private CoCallback callback;
    private boolean logs;

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
        coline.context = new WeakReference<>(context);
        coline.values  = new ContentValues();
        coline.headers = new ContentValues();
        coline.logs    = coline.getStatusLogs();
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
            Log.i(CO_LINE, "Please check the values sent in " +
                    "\"with(ContentValues)\".");
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
            Log.i(CO_LINE, "Please check the values sent in " +
                    "\"with(\"key1\",\"value1\",...)\".");
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
            Log.i(CO_LINE, "Please check the values sent in " +
                    "\"with(ArrayMap<String, Object>)\".");
        }

        for (int i = 0; i<values.size(); ++i) {
            this.values.put(String.valueOf(values.keyAt(i)),
                    String.valueOf(values.valueAt(i)));
        }
        return this;
    }

    /**
     * <p>
     * This method initiates the values to pass in header properties from
     * a ContentValues object.
     * </p>
     * <p>
     * See also: head(Object...) and head(ArrayMap&lt;String, Object&gt;)
     * </p>
     *
     * @param params (ContentValues) Values to pass in header properties
     * @return       The current instance of the class
     */
    public Coline head(final ContentValues params) {
        if (params.size() <= 0) {
            Log.i(CO_LINE, "Please check the properties sent in " +
                    "\"head(ContentValues)\".");
        }

        this.headers = params;
        return this;
    }

    /**
     * <p>
     * This method initiates the values to pass in header properties from
     * an Object array. It declared as follows:<br>
     * 'head("key1", value1, "key2", "value2", "key3", values3);'.
     * </p>
     * <p>
     * All the object in the array will be convert to a String.
     * </p>
     * <p>
     * See also: head(ContentValues) and head(ArrayMap&lt;String, Object&gt;)
     * </p>
     *
     * @param params (Object...) Values to pass in header properties
     * @return       The current instance of the class
     */
    public Coline head(final Object... params) {
        if (params.length <= 0 || params.length % 2 == 0) {
            Log.i(CO_LINE, "Please check the properties sent in " +
                    "\"head(\"key1\",\"value1\",...)\".");
        }

        for (int i=0; i<params.length; ++i) {
            this.headers.put( String.valueOf(params[i]),
                    String.valueOf(params[i + 1]));
            ++i;
        }
        return this;
    }

    /**
     * <p>
     * Only with KitKat version and higher:
     * </p>
     * <p>
     * This method initiates the values to pass in header properties from
     * an ArrayMap&lt;String, Object&gt;.
     * </p>
     * <p>
     * All the object in the array will be convert to a String.
     * </p>
     * <p>
     * See also: head(ContentValues) and head(Object...)
     * </p>
     *
     * @param params (ArrayMap) Values to pass into the request
     * @return       The current instance of the class
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public Coline head(final ArrayMap<String, Object> params) {
        if (params.size() <= 0) {
            Log.i(CO_LINE, "Please check the properties sent in " +
                    "\"head(ArrayMap<String, Object>)\".");
        }

        for (int i = 0; i<params.size(); ++i) {
            this.headers.put(String.valueOf(params.keyAt(i)),
                    String.valueOf(params.valueAt(i)));
        }

        return this;
    }

    /**
     * This method handles a callback when server returned response.
     *
     * @param callback (CoCallback) Interface of successful and errors requests
     * @return         The current instance of the class
     * @see            CoCallback
     */
    public Coline res(CoCallback callback) {
        this.callback = callback;
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

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                setValues();
                request();
            }
        });
        thread.start();
    }

    /**
     * This method creates a current queue to add multiple requests and launch it once.
     *
     * @see         Thread
     */
    public void queue() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CoQueue.init().add(Coline.this);
            }
        });
        thread.start();
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

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CoQueue.getInstance().start();
            }
        });
        thread.start();
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
                } catch (UnsupportedEncodingException e) {
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
        int MAX_READ_TIMEOUT = 3000;
        int MAX_CONNECT_TIMEOUT = 7000;
        int NO_STATUS = 0;

        String response = "";
        URL url;

        // Init URL
        try {
            if ( logs )
                Log.d(CO_LINE, "URL: " + route);

            url = new URL( route );
        } catch (MalformedURLException e) {
            if ( logs )
                Log.e(CO_LINE, "error in route: " + e.toString());

            setErrorResult("MalformedURLException", e.toString(),
                    "An error occurred when trying to get URL", NO_STATUS);
            return;
        }

        // Do connection
        if ( logs )
            Log.d(CO_LINE, "Do connection...");

        HttpURLConnection http;
        try {
            http = (HttpURLConnection) url.openConnection();

            // Adding header properties
            if (headers.size() > 0) {
                for (Map.Entry<String, Object> entry : headers.valueSet()) {
                    http.setRequestProperty(entry.getKey(), String.valueOf(entry.getValue()));
                }
            } else {
                http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                http.setRequestProperty("Charset", "UTF-8");
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
            if ( logs )
                Log.e(CO_LINE, "Error in http url connection: " + e.toString());

            setErrorResult("IOException", e.toString(),
                    "Error in http url connection", NO_STATUS);
            return;
        }

        if ( logs )
            Log.d(CO_LINE, "Connection etablished");

        // Get response
        InputStream inputStream;
        int status = 0;
        try {
            status = http.getResponseCode();
            if ( logs )
                Log.d(CO_LINE, "Status response: " + status);

            if (status >= 200 && status < 400) {
                inputStream = http.getInputStream();
            } else {
                inputStream = http.getErrorStream();
            }

        } catch (IOException e) {
            if ( logs )
                Log.e(CO_LINE, "Error when getting server response: " + e.toString());

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
                sb.append(line + "\n");
            }

            inputStream.close();
            response = sb.toString();
        } catch (Exception e) {
            if ( logs )
                Log.e(CO_LINE, "Error when parsing result: " + e.toString());

            setErrorResult("Exception", e.toString(),
                    "An error occurred when reading server response", status);
            return;
        }

        if ( logs )
            Log.d(CO_LINE, response);

        if ((status < 200 && status > 299) || response.length() == 0) {
            setErrorResult(null, null, response, status);
            return;
        }

        setResponseResult(response, status);
    }

    /**
     * Private: Create an error object to be handled by CoCallback.onResult()
     */
    private void setErrorResult(String exception, String stacktrace, String message, int status) {
        CoError err = new CoError(status, exception, stacktrace, message);
        returnResult(err, null);
    }

    /**
     * Private: Create an error object to be handled by CoCallback.onResult()
     */
    private void setResponseResult(String response, int status) {
        CoResponse res = new CoResponse(status, response);
        returnResult(null, res);
    }

    /**
     * Private: This method returns a String response in main Thread.
     *
     * @param err (CoError) error object
     * @param res (CoResponse) response object
     */
    private void returnResult(final CoError err, final CoResponse res) {
        if (callback == null) return;
        if ( logs ) Log.d(CO_LINE, "ReturnResult() called");
        
        Context c = context.get();
        if (c != null) {
            new Handler(c.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    callback.onResult(err, res);
                    clear();
                }
            });
        }
    }

    /**
     * This method interrupts the background thread.
     *
     * @see         Thread
     */
    public void cancel() {
        if ( logs )
            Log.d(CO_LINE, "Interrupt background treatment");

        if (thread == null) {
            Log.i(CO_LINE, "The background treatment is already null.");
            return;
        }
        thread.interrupt();
    }

    /**
     * Private: This method clear the current queue and calls Coline's
     * override finalize method.
     *
     */
    @SuppressWarnings("FinalizeCalledExplicitly")
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
    @SuppressWarnings("FinalizeCalledExplicitly")
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
     * Private: This destroys all reference, variable and element of Coline.
     *
     * @throws Throwable  Throw an exception when destroyed is compromised
     */
    public void destroyColine() throws Throwable {
        context = null;
        method = null;
        route = null;
        values = null;
        body = null;
        callback = null;
        logs = false;
    }

    /**
     * Protected: This overrides Object's finalize method.
     * This method destroy the instance.
     *
     * @throws Throwable  Throw an exception when destroyed is compromised
     */
    @Override
    protected void finalize() throws Throwable {
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
