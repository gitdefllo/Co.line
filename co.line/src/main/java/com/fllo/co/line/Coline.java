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

/****************************************************
 * Co.line
 * -----------
 * @version 1.0.5
 * @author  Fllo (@Gitdefllo) 2015
 *
 * Repository: https://github.com/Gitdefllo/Co.line.git
 *
 *****************************************************
 * Ready-to-use
 * ------------
 *
 * This is an open-source ready-to-use class.
 * Just copy/paste it in your project.
 *
 *
 * Usage
 * -----
 *
 * You need to initiate Co.line with the context and
 * optionaly set different parameters to do a request:
 *
 * Coline.init(Context)
 *       .url(ColineHttpMethod.int, String)
 *       .auth(ColineAuth.int, String)
 *		 .with(ContentValues)
 *		 .success(new Success())
 *       .error(new Error())
 *       .exec();
 *
 *****************************************************/
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
    private Success	   	   success;
    private Error		   error;
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
                    coline.logs    = ColineLogs.getStatus();
                    if ( coline.logs )
                        Log.e(CO_LINE, "initialized");
                }
            }
        }
        return coline;
    }

    /**
     * Interface when the class should return a successful response.
     *
     */
    public interface Success {
        void onSuccess(String s);
    }

    /**
     * Interface when the class should return an error from response.
     *
     */
    public interface Error {
        void onError(String s);
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
     * This method prepares a callback when the request is successful.
     *
     * @param success (Success) Interface of successful request
     * @return        The current instance of the class
     * @see           Coline
     */
    public Coline success(Success success) {
        this.success = success;
        return this;
    }

    /**
     * This method prepares a callback when the request is failed.
     *
     * @param error (Error) Interface of failed request
     * @return      The current instance of the class
     * @see         Coline
     */
    public Coline error(Error error) {
        this.error    = error;
        return this;
    }

    /**
     * This method executes a request in a new Thread.
     *
     * @see         Thread
     */
    public void exec() {
        if ( logs )
            Log.e(CO_LINE, "request execution...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                setValues();
                request();
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
        }
        if ( logs )
            Log.e(CO_LINE, "values added to request body");
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
            returnError(s);
            return;
        }

        // Do connection
        if ( logs )
            Log.e(CO_LINE, "do connection...");
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
                Log.e(CO_LINE, "error in http url connection: " + e.toString());
        }

        if (http == null) {
            String s = "An error occurred in URL connection";
            try{
                // Create a json to treat it in returnError(String)
                JSONObject jreturn = new JSONObject(s);
                s = jreturn.toString();
            } catch(JSONException e) {}
            returnError(s);
            return;
        }

        // Get response
        InputStream inputStream = null;
        int         status      = 0;
        try {
            status = http.getResponseCode();
            if ( logs )
                Log.d(CO_LINE, "status response: " + status);
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
            returnError(s);
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
                Log.e(CO_LINE, "error when parsing result: " + e.toString());
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
        if (success == null) return;
        if ( logs ) Log.e(CO_LINE, "OK, onSuccess called");
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                success.onSuccess(s);
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
        if (error == null) return;
        if ( logs ) Log.e(CO_LINE, "Oops, onError called");
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                error.onError(s);
            }
        });
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
        if ( status ) Log.e(CO_LINE, "Logs activation!");
        ColineLogs.setStatus(status);
    }
}