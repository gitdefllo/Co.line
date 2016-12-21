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

import android.util.Log;

import com.fllo.co.line.builders.Logs;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Queue {

    private static final String CO_LINE_QUEUE  = "Queue";

    private boolean logs;
    private static Queue queue = null;
    private ArrayList<WeakReference<Coline>> requests;
    private boolean used = false;
    private int pendingRequests;

    /**
     * Initiate queue class and prepare to get multiple
     * Coline objects
     *
     * @return Instance of the class
     */
    public static Queue init() {
        if (queue == null) {
            synchronized (Queue.class) {
                if (queue == null) {
                    queue = new Queue();
                    queue.requests = new ArrayList<>();
                    queue.logs = Logs.getInstance().getStatus();
                    if (queue.logs) Log.d(CO_LINE_QUEUE, "Queue initialization");
                }
            }
        }
        return queue;
    }

    /**
     * Get the current queue without checking any errors
     * or nullable instance
     *
     * @return The Queue current's instance
     */
    public static Queue getInstance() {
        return queue;
    }

    /**
     * Save the current request into the current queue
     *
     * @param request (Coline) Coline's instance which will be
     *                added to the queue
     */
    public void add(Coline request) {
        if (this.requests == null) {
            if ( logs ) Log.e(CO_LINE_QUEUE, "Failed to add a request to the queue: list = null");
            return;
        }

        this.requests.add(new WeakReference<>(request));
        this.used = true;
        this.logs = request.getLogsStatus();
        this.pendingRequests += 1;
        if ( logs ) Log.d(CO_LINE_QUEUE, "New request added to the queue");
    }

    /**
     * Retrieve all requests in queue and execute them
     */
    public void start() {
        if (this.requests == null) {
            if ( logs ) Log.e(CO_LINE_QUEUE, "Failed to launch the queue: list = null");
            return;
        }

        if ( logs ) Log.d(CO_LINE_QUEUE, "Start execution of pending requests");

        for (final WeakReference<Coline> req : this.requests) {
            Coline c = req.get();
            if (c != null) {
                if ( logs ) Log.d(CO_LINE_QUEUE, "Execute request in current " +
                        "queue (rf. " + c.toString() + ")");

                c.exec();
            }
            queue.pendingRequests -= 1;
        }
    }

    /**
     * Get the number of pending requests in the current queue
     *
     * @return Number of pending requests
     */
    public boolean getPending() {
        return this.pendingRequests > 0;
    }

    /**
     * Get the current state of the queue: if it's used or not
     *
     * @return State of the current queue
     */
    // Get the number of pending request in the current queue
    public boolean getState() {
        return this.used;
    }

    /**
     * Private: destroy all references, variables and elements of Queue
     * only if Queue has every pending requests done and if the queue is used
     *
     * @throws Throwable Throw an exception when destroy is compromised
     */
    public void destroyCurrentQueue() throws Throwable {
        if ( getState() && pendingRequests == 0 ) {
            queue = null;
            requests = null;
            logs = false;
            used = false;
        }
    }

    /**
     * Protected: Override Object's finalize method, check if
     * Queue's instance is used and if not and destroy the current queue
     *
     * @throws Throwable Throw an exception when destroy is compromised
     */
    @Override
    protected void finalize() throws Throwable {
        if ( this.used ) {
            try {
                destroyCurrentQueue();
            }
            catch(Exception ex) {
                Log.e(CO_LINE_QUEUE, "Destroying the current queue not working: " + ex.toString());
            }
            finally {
                Log.d(CO_LINE_QUEUE, "Current queue is destroyed");
                super.finalize();
            }
        }
    }
}
