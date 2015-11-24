/*
 * Fllo, All rights reserved - 2015 (@Gitdefllo)
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
 * @version 1.0.6
 * @author  Fllo (@Gitdefllo) 2015
 *
 * Android library for HttpURLConnection connections with automatic Thread
 * and Callbacks in chained methods in one line.
 * Repository: https://github.com/Gitdefllo/Co.line.git
 *
 */
public class Coline {

    // Tags
    private static final String CO_LINE  = "-- Co.line";

    // Configuration
    private static Coline  coline;
    private Context        context;
    private String 		   method;
    private String 		   route;
    private String 		   auth;
    private String         token;
    private ContentValues  values;
    private StringBuilder  body = null;
    private Response       response;
    private boolean        used = false;
    private boolean        logs;

    /**
     * Co.line's constructor: method to initiate Coline with actual Context.
     * It creates a new instance of class if this one does not already exist.
     * The Context can be an Activity, a Fragment, a Service or anything. This
     * will be used to return the request response into the main Thread.
     * <p>
     * When this class is initiate, it attaches also a boolean value
     * to des/activate logs and creates a new ContentValues.
     * <p>
     * The ContentValues will be used to store the parameters to pass into the
     * request by {@link #with(ContentValues)} method.
     *
     * @param context (Context) Actual Context from the call
     * @return        An instance of the class
     * @see           Coline
     */
    public static Coline init(Context context) {
        if (coline == null) {
            synchronized (Coline.class) {
                if (coline == null) {
                    coline         = new Coline();
                    coline.context = context;
                    coline.values  = new ContentValues();
                    coline.used    = true;
                    coline.logs    = ColineLogs.getInstance().getStatus();
                    if ( coline.logs )
                        Log.i(CO_LINE, "Initialization, version " + BuildConfig.VERSION_NAME);
                }
            }
        }
        return coline;
    }

    /**
     * Interface to return server response, it has three methods:
     * <p>
     *      - onSuccess(String): returns a String server response when Http
     *      status is equals to 200.
     * <p>
     *      - onError(String): returns a String server response when
     *      Http response is not a success and contains 'error' characters
     *      chained.
     * <p>
     *      - onFail(String): returns a String method response when an error
     *      occurred in Coline class.
     *
     */
    public interface Response {
        void onSuccess(String s);
        void onError(String s);
        void onFail(String s);
    }

    /**
     * This method initiates the request method and the URL to do the request.
     * <p>
     * The request method is a static int value from ColineHttpMethod class like:
     *      - ColineHttpMethod.GET
     *      - ColineHttpMethod.POST
     *      - ColineHttpMethod.PUT
     *      - ColineHttpMethod.DELETE
     *      - ColineHttpMethod.HEAD
     *
     * @param method (int) Value from ColineHttpMethod
     *               of request method
     * @param route  (String) Value for URL route (should be a String URL as
     *               "http://www.example.com")
     * @return       The current instance of the class
     * @see          Coline
     */
    public Coline url(int method, String route) {
        this.method = new ColineHttpMethod().getMethod(method);
        this.route  = route;
        return this;
    }

    /**
     * This method initiates the values to pass into the request from
     * a ContentValues object.
     * <p>
     * See also: {@link #with(Object...)} and {@link #with(ArrayMap)}
     *
     * @param values (ContentValues) Values to pass into the request
     * @return       The current instance of the class
     * @see          Coline
     */
    public Coline with(ContentValues values) {
        this.values = values;
        return this;
    }

    /**
     * This method initiates the values to pass into the request from
     * an Object array. It declared as follows: 'with("key1", value1, "key2",
     * "value2", "key3", values3);'.
     * <p>
     * All the object in the array will be convert to a String.
     * <p>
     * See also: {@link #with(ContentValues)} and {@link #with(ArrayMap)}
     *
     * @param values (Object...) Values to pass into the request
     * @return       The current instance of the class
     * @see          Coline
     */
    public Coline with(final Object... values) {
        for (int i=0; i<values.length; ++i) {
            this.values.put( String.valueOf(values[i]),
                    String.valueOf(values[i + 1]));
            ++i;
        }
        return this;
    }

    /**
     * Only with KitKat version and higher:
     * <p>
     * This method initiates the values to pass into the request from
     * an ArrayMap&lt;String, Object&gt;.
     * <p>
     * All the object in the array will be convert to a String.
     * <p>
     * See also: {@link #with(ContentValues)} and {@link #with(ContentValues)}
     *
     * @param values (ArrayMap) Values to pass into the request
     * @return       The current instance of the class
     * @see          Coline
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public Coline with(final ArrayMap<String, Object> values) {
        for (int i = 0; i<values.size(); ++i) {
            this.values.put(String.valueOf(values.keyAt(i)),
                    String.valueOf(values.valueAt(i)));
        }
        return this;
    }

    /**
     * This method prepares the header field to authenticate a request.
     *
     * @param auth  (int) Key method for Authentication from
     *              ColineAuth in header field
     * @param token (String) Value of Authentication (should be a String
     *              encoded in BASE64)
     * @return      The current instance of the class
     * @see         Coline
     */
    public Coline auth(int auth, String token) {
        this.auth  = new ColineAuth().getAuthHeader(auth);
        this.token = token;
        return this;
    }

    /**
     * This method handles a callback when server returned response.
     *
     * @param response (Response) Interface of successful and errors requests
     * @return         The current instance of the class
     * @see            Coline
     */
    public Coline res(Response response) {
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
                ColineRequest request = new ColineRequest(coline);
                ColineQueue.init(context.getApplicationContext()).add(request);
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

        if (ColineQueue.getInstance() == null) {
            Log.e(CO_LINE, "The queue isn't created. Maybe you missed to add " +
                    "a request with 'queue()'.");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                ColineQueue.getInstance().start();
            }
        }).start();
    }

    /**
     * Private: This method get all value from {@link #with(ContentValues)},
     * {@link #with(Object...)} or {@link #with(ArrayMap<String, Object)} and prepares
     * a StringBuilder with all parameter given to send it into the request.
     *
     * @see         StringBuilder
     */
    private void setValues() {
        if (this.values != null && this.values.size() > 0) {
            boolean first_value = true;
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
        int     MAX_READ_TIMEOUT    = 10000;
        int     MAX_CONNECT_TIMEOUT = 15000;

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
            String s = "An error occurred when trying to get URL";
            try{
                // Create a json to treat it in returnError(String)
                JSONObject jreturn = new JSONObject(s);
                s = jreturn.toString();
            } catch(JSONException e) {}
            returnFail(s);
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
            String s = "An error occurred in URL connection";
            try{
                // Create a json to treat it in returnError(String)
                JSONObject jreturn = new JSONObject(s);
                s = jreturn.toString();
            } catch(JSONException e) {}
            returnFail(s);
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
            String s = "An error occurred when trying to get server response";
            try{
                // Create a json to treat it in returnError(String)
                JSONObject jreturn = new JSONObject(s);
                s = jreturn.toString();
            } catch(JSONException e) {}
            returnFail(s);
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
            returnError(response);
            return;
        }

        returnSuccess(response);
    }

    /**
     * Private: This method returns a String response in main Thread.
     *
     * @param s (String) Response of request
     * @see     android.os.Looper
     */
    private void returnSuccess(final String s) {
        if (response == null) return;
        if ( logs ) Log.d(CO_LINE, "OK, onSuccess() called");
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                response.onSuccess(s);
            }
        });
    }

    /**
     * Private: This method returns a String response in main Thread.
     *
     * @param s (String) Response of request
     * @see     android.os.Looper
     */
    private void returnError(final String s) {
        if (response == null) return;
        if ( logs ) Log.d(CO_LINE, "Oops, onError() called");
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                response.onError(s);
            }
        });
    }

    /**
     * Private: This method returns a String from Coline connection in main Thread.
     *
     * @param s (String) Error on Coline connection
     * @see     android.os.Looper
     */
    private void returnFail(final String s) {
        if (response == null) return;
        if ( logs ) Log.d(CO_LINE, "Coline! We've got a problem, onFail() called");
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                response.onFail(s);
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
     * Private: Call override {@link #Object} finalize method for the current queue.
     *
     * @see     {@link ColineQueue}
     */
    private void clearQueue() {
        if ( ColineQueue.getInstance() == null )
            return;

        if ( logs ) Log.d(CO_LINE, "A current request queue found");

        if ( !ColineQueue.getInstance().getPending() ) {
            if ( logs ) Log.d(CO_LINE, "No pending requests left, try to destroy the queue");

            try {
                ColineQueue.getInstance().finalize();
            } catch(Throwable t) {
                if ( logs )
                    Log.i(CO_LINE, "Clear queue error: "+t.toString());
            }
        }
    }

    /**
     * Private: This destroys all reference, variable and element of Coline only
     * if Coline is in {@link #used} state.
     * <p>
     * Raised exception: Throwable
     *
     */
    public void destroyColine() throws Throwable {
        if ( used ) {
            coline = null;
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
     * Protected: This overrides {@link #Object} finalize method.
     * <p>
     * This method checks if Coline instance is used and if not, destroy the instance.
     * <p>
     * Exception: Throwable
     *
     * @see     java.lang.Object
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

    /**
     * This method activate or desactivate console logs in Coline request method.
     * By default: the logs are desactivate.
     *
     * @param status (boolean) Value to activate or not the console logs (true: activate,
     *               false: desactivate)
     * @see          ColineLogs
     */
    public static void activateLogs(boolean status) {
        if ( status ) Log.d(CO_LINE, "Logs activation!");
        ColineLogs.getInstance().setStatus(status);
    }
}
