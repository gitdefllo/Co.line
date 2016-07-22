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

import com.fllo.co.line.builders.HttpMethod;
import com.fllo.co.line.builders.Logs;
import com.fllo.co.line.callbacks.Collback;
import com.fllo.co.line.results.Error;
import com.fllo.co.line.results.Response;

import java.lang.ref.WeakReference;
import java.util.Observable;
import java.util.Observer;

/**
 * Co.line
 * -------
 * @version 2.0.2
 * @author Florent Blot (@Gitdefllo)
 *
 * Android library for HttpURLConnection connections with automatic Thread
 * and Callbacks in chained methods in one line.
 *
 * Repository:    https://github.com/Gitdefllo/Co.line.git
 * Issue tracker: https://github.com/Gitdefllo/Co.line/issues
 *
 */
public class Coline implements Observer {

    // Tags
    private static final String CO_LINE  = "Co.line";

    // Configuration
    private WeakReference<Context> context;
    private Thread thread;
    private String method;
    private String route;
    private ContentValues headers, values;
    private Collback collback;
    private boolean logs;

    /**
     * <p>
     * Co.line's constructor: method to initiate Coline with actual Context.
     * It creates a new instance of class.
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
        coline.logs    = coline.getLogsStatus();
        return coline;
    }

    /**
     * <p>
     * This method initiates the request method and the URL to do the request.
     * </p>
     * <p>
     * The request method is a static int value from HttpMethod class like:<br>
     *      - HttpMethod.GET<br>
     *      - HttpMethod.POST<br>
     *      - HttpMethod.PUT<br>
     *      - HttpMethod.DELETE<br>
     *      - HttpMethod.HEAD<br>
     * </p>
     *
     * @param method (HttpMethod) Value from HttpMethod of request method
     * @param route  (String) Value for URL route (should be a String URL as
     *               "http://www.example.com")
     * @return       The current instance of the class
     * @see          HttpMethod
     */
    public Coline url(HttpMethod method, String route) {
        this.method = method.toString();
        this.route  = route;
        return this;
    }

    /**
     * <p>
     * This method initiates the values to pass in header properties from
     * a ContentValues object.
     * </p>
     * <p>
     * See also: head(ArrayMap&lt;String, Object&gt;)
     * </p>
     *
     * @param params (ContentValues) Values to pass in header properties
     * @return       The current instance of the class
     */
    public Coline head(final ContentValues params) {
        if (params.size() <= 0) {
            if ( logs ) Log.e(CO_LINE, "Please check the properties sent in " +
                        "\"head(ContentValues)\".");
        }

        this.headers = params;
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
     * See also: head(ContentValues)
     * </p>
     *
     * @param params (ArrayMap) Values to pass into the request
     * @return       The current instance of the class
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public Coline head(final ArrayMap<String, Object> params) {
        if (params.size() <= 0) {
            if ( logs ) Log.e(CO_LINE, "Please check the properties sent in " +
                        "\"head(ArrayMap<String, Object>)\".");
        }

        for (int i = 0; i<params.size(); ++i) {
            this.headers.put(String.valueOf(params.keyAt(i)),
                    String.valueOf(params.valueAt(i)));
        }

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
            if ( logs ) Log.e(CO_LINE, "Please check the values sent in " +
                    "\"with(ContentValues)\".");
        }

        this.values = values;
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
            if ( logs ) Log.e(CO_LINE, "Please check the values sent in " +
                    "\"with(ArrayMap<String, Object>)\".");
        }

        for (int i = 0; i<values.size(); ++i) {
            this.values.put(String.valueOf(values.keyAt(i)),
                    String.valueOf(values.valueAt(i)));
        }
        return this;
    }

    /**
     * This method handles a callback when server returned response.
     *
     * @param collback (Collback) Interface of successful and errors requests
     * @return         The current instance of the class
     * @see            Collback
     */
    public Coline res(Collback collback) {
        this.collback = collback;
        return this;
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
                Queue.init().add(Coline.this);
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
        if ( logs ) Log.d(CO_LINE, "Launch all request in the current queue");

        if (Queue.getInstance() == null) {
            if ( logs ) Log.e(CO_LINE, "The queue isn't created. Maybe you missed to add " +
                        "a request with 'queue()'.");
            return;
        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Queue.getInstance().start();
            }
        });
        thread.start();
    }

    /**
     * This method executes a request in a new Thread.
     *
     * @see         Thread
     */
    public void exec() {
        if ( logs ) Log.d(CO_LINE, "...Request execution...");

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Request req = new Request(method, route, headers, logs);
                req.addObserver(Coline.this);
                req.setValues(values);
                req.makeRequest();
            }
        });
        thread.start();
    }

    /**
     *
     *
     * @param obs (Observable)
     * @param obj (Object)
     */
    public void update(Observable obs, Object obj) {
        if (obs instanceof Request) {
            Request req = (Request) obs;
            returnResult(req.err, req.res);
        }
    }

    /**
     * Private: This method returns a String response in main Thread.
     *
     * @param err (Error) error object
     * @param res (Response) response object
     */
    private void returnResult(final Error err, final Response res) {
        if (collback == null) return;
        if ( logs ) Log.d(CO_LINE, "ReturnResult() called");
        
        Context c = context.get();
        if (c != null) {
            new Handler(c.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    collback.onResult(err, res);
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
        if ( logs ) Log.d(CO_LINE, "Interrupt background treatment");

        if (thread == null) {
            if ( logs ) Log.e(CO_LINE, "The background treatment is already null.");
            return;
        }
        thread.interrupt();
    }

    /**
     * This method get the status of current logs.
     *
     * @return       A boolean (true: activate, false: disable)
     * @see          Logs
     */
    public boolean getLogsStatus() {
        return Logs.getInstance().getStatus();
    }

    /**
     * This static method activates the logs.
     */
    public static void enableDebug() {
        Logs.getInstance().setStatus(true);
    }

    /**
     * This static method desactivates the logs.
     */
    public static void disableDebug() {
        Logs.getInstance().setStatus(false);
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
            if ( logs ) Log.e(CO_LINE, "Clear coline error: " + t.toString());
        }
    }

    /**
     * Private: Call override Object's finalize method for the current queue.
     *
     * @see     {@link Queue}
     */
    @SuppressWarnings("FinalizeCalledExplicitly")
    private void clearQueue() {
        if ( Queue.getInstance() == null )
            return;

        if ( logs ) Log.d(CO_LINE, "A current request queue found");

        if ( !Queue.getInstance().getPending() ) {
            if ( logs ) Log.d(CO_LINE, "No pending requests left, try to destroy the queue");

            try {
                Queue.getInstance().finalize();
            } catch(Throwable t) {
                if ( logs ) Log.e(CO_LINE, "Clear queue error: "+t.toString());
            }
        }
    }

    /**
     * Private: This destroys all reference, variable and element of Coline.
     *
     * @throws Throwable  Throw an exception when destroyed is compromised
     */
    private void destroyColine() throws Throwable {
        context = null;
        method = null;
        route = null;
        values = null;
        collback = null;
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
            if ( logs ) Log.e(CO_LINE, "Destroying the current coline not working");
        }
        finally {
            if ( logs ) Log.d(CO_LINE, "Current coline is destroyed");
            super.finalize();
        }
    }
}
