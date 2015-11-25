/*
 * Florent Blot (Fllo, @Gitdefllo), all rights reserved - 2015
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

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/*
 * Co.lineQueue
 * -----------------
 * @author  Fllo (@Gitdefllo) 2015
 *
 * Add multiple requests on a queue and launch one time:
 * Create a new queue or an instance of the current one if it exists yet. Then, save
 * the state of each request, wait the last one and send it to the server.
 * Contains an ArrayList of Coline's request with its properties.
 *
 */
public class ColineQueue {

    // Tags
    private static final String CO_LINE_QUEUE  = "-- ColineQueue";

    // Configuration
    private static ColineQueue queue = null;
    private Context            context;
    private ArrayList<Coline>  requests;
    private boolean            logs;

    // Lifecycle
    private boolean used = false;
    private int     pendingRequests;

    /**
     * Co.lineQueue's constructor: method to initiate ColineQueue with actual Context.
     * It creates a new instance of class if this one does not already exist.
     * The Context can be an Activity, a Fragment, a Service or anything and it
     * assigns by Coline's instance. This is used to create a current queue of
     * request and launch it at one time.
     *
     * @param context (Context) Actual Context from the call in Coline
     * @return        An instance of the class
     */
    public static ColineQueue in(Context context) {
        if (queue == null) {
            synchronized (ColineQueue.class) {
                if (queue == null) {
                    queue          = new ColineQueue();
                    queue.context  = context;
                    queue.requests = new ArrayList<>();
                    queue.logs     = ColineLogs.getInstance().getStatus();
                    if (queue.logs)
                        Log.d(CO_LINE_QUEUE, "Queue initialization");
                }
            }
        }
        return queue;
    }

    /**
     * Get the current queue without checking any errors or nullable instance.
     *
     * @return       The ColineQueue current's instance.
     */
    public static ColineQueue getInstance() {
        return queue;
    }

    /**
     * Save the current request into the current queue.
     *
     * @param request (Coline) The Coline's instance which will be
     *                added to the queue.
     */
    public void add(Coline request) {
        if (this.requests == null) {
            if ( logs )
                Log.e(CO_LINE_QUEUE, "Failed to add a request to the queue: list = null");
            return;
        }

        this.requests.add(this.requests.size(), request);
        this.used = true;
        this.logs = request.getStatusLogs();
        this.pendingRequests += 1;
        if ( logs )
            Log.d(CO_LINE_QUEUE, "New request added to the queue");
    }

    /**
     * Retrieve all requests in queue, and launch them.
     *
     */
    public void start() {
        if (this.requests == null) {
            if ( logs )
                Log.e(CO_LINE_QUEUE, "Failed to launch the queue: list = null");
            return;
        }

        if ( logs )
            Log.d(CO_LINE_QUEUE, "Start execution of pending requests");

        for (final Coline r : this.requests) {
            if ( logs )
                Log.d(CO_LINE_QUEUE, "Execute request in current queue (rf. " + r.toString() + ")");
            r.exec();
            queue.pendingRequests -= 1;
        }
    }

    /**
     * Get the number of pending requests in the current queue.
     *
     * @return       The number of pending requests
     */
    public boolean getPending() {
        return this.pendingRequests > 0;
    }

    /**
     * Get the current state of the queue: if it's used or not.
     *
     * @return       The state of the current queue
     */
    // Get the number of pending request in the current queue
    public boolean getState() {
        return this.used;
    }

    /**
     * Private: This destroys all reference, variable and element of ColineQueue
     * only if ColineQueue is every pending requests are done and if the queue is used.
     *
     * @throws  Throwable Throw an exception when destroyed is compromised
     */
    public void destroyCurrentQueue() throws Throwable {
        if ( getState() && pendingRequests == 0 ) {
            queue = null;
            context = null;
            requests = null;
            logs = false;
            used = false;
        }
    }

    /**
     * <p>
     * Protected: This overrides Object's finalize method.
     * </p>
     * <p>
     * This method checks if ColineQueue instance is used and if not,
     * destroy the current queue.
     * </p>
     *
     * @throws  Throwable Throw an exception when destroyed is compromised
     */
    @Override
    protected void finalize() throws Throwable {
        if ( this.used ) {
            try {
                destroyCurrentQueue();
            }
            catch(Exception ex) {
                Log.d(CO_LINE_QUEUE, "Destroying the current queue not working: " + ex.toString());
            }
            finally {
                Log.d(CO_LINE_QUEUE, "Current queue is destroyed");
                super.finalize();
            }
        }
    }
}
