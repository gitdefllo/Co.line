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
import com.fllo.co.line.callbacks.ObjCollback;
import com.fllo.co.line.results.Error;
import com.fllo.co.line.results.Response;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Observable;
import java.util.Observer;

/**
 * Co.line
 * -------
 * Android library for HttpURLConnection in threads
 * and retrieve callbacks in foreground's app context.
 *
 * @version 2.2.0
 * https://github.com/Gitdefllo/Co.line.git
 */
public class Coline implements Observer {

    private static final String CO_LINE  = "Co.line";

    private WeakReference<Context> context;
    private Thread thread;
    private String httpmethod;
    private String route;
    private ContentValues headers, 
            values;
    private Collback collback;
    private ObjCollback objcollback;
    private boolean logs;

    /**
     * Initiate class with current app Context
     * (an Activity for example)
     *
     * @param context (Context) Current context of client
     * @return Instance of the class
     */
    public static Coline init(Context context) {
        Coline coline  = new Coline();
        coline.context = new WeakReference<>(context);
        coline.logs    = coline.getLogsStatus();
        return coline;
    }

    /**
     * Initiates HTTP request and URL of server
     * HTTP request method is a static int value from HttpMethod class as:
     *      - HttpMethod.GET
     *      - HttpMethod.POST
     *      - HttpMethod.PUT
     *      - HttpMethod.DELETE
     *      - HttpMethod.HEAD
     *
     * @param httpmethod (HttpMethod) Value of HttpMethod
     * @param route (String) Value of URL route
     * @return Current instance of the class
     * @see HttpMethod
     */
    public Coline url(HttpMethod httpmethod, String route) {
        this.httpmethod = httpmethod.toString();
        this.route  = route;
        return this;
    }

    /**
     * Prepare the header properties from
     * a ContentValues object
     * See also: head(ArrayMap<String, Object>)
     *
     * @param params (ContentValues) Values of header properties
     * @return Current instance of the class
     */
    public Coline head(final ContentValues params) {
        this.headers = new ContentValues();
        if (params.size() <= 0) {
            if ( logs ) Log.e(CO_LINE, "Please check the properties sent in " +
                        "\"head(ContentValues)\".");
        }

        this.headers = params;
        return this;
    }

    /**
     * Only with KitKat version and higher:
     * Prepare the values for header properties from
     * an ArrayMap<String, Object>
     * See also: head(ContentValues)
     *
     * @param params (ArrayMap) Values of header properties
     * @return Current instance of the class
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
     * Convert the values for body request to String
     * See also: with(ArrayMap<String, Object>)
     *
     * @param values (ContentValues) Values of body request
     * @return Current instance of the class
     */
    public Coline with(final ContentValues values) {
        this.values  = new ContentValues();
        if (values.size() <= 0) {
            if ( logs ) Log.e(CO_LINE, "Please check the values sent in " +
                    "\"with(ContentValues)\".");
        }

        this.values = values;
        return this;
    }

    /**
     * Only with KitKat version and higher:
     * Prepare the values for body request from
     * an ArrayMap<String, Object>
     * See also: with(ContentValues)
     *
     * @param values (ArrayMap) Values of body request
     * @return Current instance of the class
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
     * Handle a callback with coline objects response
     *
     * @param collback (Collback) Interface of successful 
     *                 and errors requests
     * @return Current instance of the class
     * @see Collback
     */
    public Coline res(Collback collback) {
        this.collback = collback;
        return this;
    }

    /**
     * Handle a callback for a custom object
     *
     * @return Coline Current instance
     * @see Collback
     */
    public Coline res(ObjCollback objcollback) {
        this.objcollback = objcollback;
        return this;
    }

    /**
     * Create a current queue to add multiple
     * requests and execute them later
     *
     * @see Thread
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
     * Execute all Coline instances in
     * a thread
     *
     * @see Thread
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
     * Executes a request in a thread
     *
     * @see Thread
     */
    public void exec() {
        if ( logs ) Log.d(CO_LINE, "...Request execution...");

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Request req = new Request(httpmethod, route, headers, logs);
                req.addObserver(Coline.this);
                req.setValues(values);
                req.makeRequest();
            }
        });
        thread.start();
    }

    /**
     * Update the instance with server response
     *
     * @param obs (Observable)
     * @param obj (Object)
     */
    public void update(Observable obs, Object obj) {
        if (obs instanceof Request) {
            Request req = (Request) obs;
            returnResult(req.res, req.err);
        }
    }
    
    /**
     * Private: return the specific type used from interface
     *
     * @param cls (Class) Interface to find the parameter type
     */
    private Type typeFromClass(Class cls) {
        Type[] genericInterfaces = cls.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                return ((ParameterizedType) genericInterface)
                        .getActualTypeArguments()[0];
            }
        }
        return null;
    }

    /**
     * Private: return the response in current main thread
     *
     * @param res (Response) response object
     * @param err (Error) error object
     */
    @SuppressWarnings("unchecked")
    private void returnResult(final Response res, final Error err) {
        if (collback == null && objcollback == null) 
            return;
        
        if ( logs ) Log.d(CO_LINE, "ReturnResult() called");
        
        Context c = context.get();
        if (c != null) {
            new Handler(c.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (objcollback != null) {
                        Object obj = null;
                        if (err == null) {
                            Type type = typeFromClass(objcollback.getClass());
                            obj = new Gson().fromJson(res.body, type);
                        }
                        objcollback.onResult(obj, err);
                    } else {
                        collback.onResult(res, err);
                    }

                    clear();
                }
            });
        }
    }
    
    /**
     * Interrupt the background thread
     *
     * @see Thread
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
     * Get the current status of debug logs
     *
     * @return boolean (true: activate, false: disable)
     * @see Logs
     */
    public boolean getLogsStatus() {
        return Logs.getInstance().getStatus();
    }

    /**
     * Enable the debug logs
     */
    public static void enableDebug() {
        Logs.getInstance().setStatus(true);
    }

    /**
     * Disable the debug logs
     */
    public static void disableDebug() {
        Logs.getInstance().setStatus(false);
    }

    /**
     * Private: clear the current queue and
     * call finalize
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
     * Private: call overrided Object's finalize
     * for the current queue
     *
     * @see {@link Queue}
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
     * Private: destroy all references, variables and elements
     *
     * @throws Throwable Throw an exception when destroy
     * is compromised
     */
    private void destroyColine() throws Throwable {
        context = null;
        httpmethod = null;
        route = null;
        values = null;
        collback = null;
        objcollback = null;
        logs = false;
    }

    /**
     * Protected: override Object's finalize method
     * and set the current instance to null
     *
     * @throws Throwable Throw an exception when destroy
     * is compromised
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
