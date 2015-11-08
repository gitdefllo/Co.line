package com.android.co.line;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

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
 * @version 0.0.3
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
 *       .url(String, String)
 *       .auth(ColineAuth.int, String)
 *		 .with(ContentValues)
 *		 .success(Success())
 *       .error(Error())
 *       .exec();
 *
 *****************************************************/
public class Coline {

    // Tags
    private static final String CO_LINE = "-- Co.line";

    // Configuration
    private static Coline  coline;
    private Context        context;
    private String 		   method;
    private String 		   route;
    private String 		   auth;
    private String         token;
    private ContentValues  values;
    private StringBuilder  body = null;
    private Success	   	   successback;
    private Error		   errorback;

    // Public constructor
    public static Coline init(Context context) {
        if (coline == null) {
            synchronized (Coline.class) {
                if (coline == null) {
                    coline         = new Coline();
                    coline.context = context;
                    coline.values  = new ContentValues();
                }
            }
        }
        return coline;
    }

    // Public interfaces
    public interface Success {
        void onSuccess(String s);
    }

    public interface Error {
        void onError(String s);
    }

    // Public methods
    public Coline url(String method, String route) {
        this.method = method;
        this.route  = route;
        return this;
    }

    public Coline with(ContentValues values) {
        this.values = values;
        return this;
    }

    public Coline with(final Object... values) {
        for (int i=0; i<values.length; ++i) {
            this.values.put( values[i].toString(),
                    values[i+1].toString() );
            ++i;
        }
        return this;
    }

    public Coline auth(int auth, String token) {
        switch (auth) {
            case ColineAuth.BASIC_AUTH:
                this.auth  = "Basic ";
                break;
            case ColineAuth.OAUTH_2:
                this.auth  = "Bearer ";
                break;
        }
        this.token = token;
        return this;
    }

    public Coline success(Success successback) {
        this.successback = successback;
        return this;
    }

    public Coline error(Error errorback) {
        this.errorback = errorback;
        return this;
    }

    public void exec() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setValues();
                request();
            }
        }).start();
    }

    // Private methods
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
                    Log.e(CO_LINE, e.toString());
                }
                first_value = false;
            }
        }
    }

    private void request() {
        // Prepare variables
        int     MAX_READ_TIMEOUT    = 10000;
        int     MAX_CONNECT_TIMEOUT = 15000;

        String  response = "";
        URL     url = null;

        // Init URL
        try {
            Log.d(CO_LINE, "URL: " + route);
            url = new URL( route );
        } catch(MalformedURLException e) {
            Log.e(CO_LINE, "error in route: " + e.toString());
        }

        if (url == null) {
            returnError("error: url not found");
            return;
        }

        // Do connection
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
            Log.e(CO_LINE, "error in http url connection: " + e.toString());
        }

        if (http == null) {
            returnError("error: url connection");
            return;
        }

        // Get response
        InputStream inputStream = null;
        int         status      = 0;
        try {
            status = http.getResponseCode();
            Log.d(CO_LINE, "status response: " + status);
            if (status >= 200 && status < 400) {
                inputStream = http.getInputStream();
            } else {
                inputStream = http.getErrorStream();
            }
        } catch (IOException ex) {
            Log.e(CO_LINE, ex.toString());
        }

        if (inputStream == null) {
            returnError("error: inputstream not found");
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
            Log.d(CO_LINE, response);
        } catch (Exception e) {
            Log.e(CO_LINE, "error when parsing result: " + e.toString());
        }

        if (status != 200 || response == null || response.contains("error")) {
            returnError(response);
            return;
        }

        returnSuccess(response);
    }

    // Result handlers in Main Thread
    private void returnSuccess(final String s) {
        if (successback == null) {
            return;
        }

        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                successback.onSuccess(s);
            }
        });
    }

    private void returnError(final String s) {
        if (errorback == null) {
            return;
        }

        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                errorback.onError(s);
            }
        });
    }
}