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
 * Contains an ArrayList of ColineRequest with its properties.
 *
 */
public class ColineQueue {
    // Tags
    private static final String CO_LINE_QUEUE  = "-- ColineQueue";

    // Configuration
    private static ColineQueue       queue;
    private Context                  context;
    private ArrayList<ColineRequest> requests;
    private boolean                  logs;

    // Lifecycle
    private boolean used = false;
    private int     pendingRequests;

    // Create the queue
    public static ColineQueue init(Context context) {
        if (queue == null) {
            synchronized (ColineQueue.class) {
                if (queue == null) {
                    queue          = new ColineQueue();
                    queue.context  = context;
                    queue.logs     = ColineLogs.getStatus();
                    queue.requests = new ArrayList<>();
                    if (queue.logs)
                        Log.d(CO_LINE_QUEUE, "Request Queue is created");
                }
            }
        }
        return queue;
    }

    // Get current instance
    public static ColineQueue getInstance() {
        return queue;
    }

    // TODO: save the current request into the current queue.
    public void add(ColineRequest request) {
        if (this.requests == null) {
            if ( this.logs )
                Log.e(CO_LINE_QUEUE, "Failed to add a request to the queue: array = null");
            return;
        }

        this.requests.add(this.requests.size(), request);
        this.used = true;
        this.pendingRequests += 1;
    }


    // TODO: retrieve all requests in queue, and launch them.
    public void start() {
        if (this.requests == null) return;

        if ( logs )
            Log.d(CO_LINE_QUEUE, "Request execution for waiting requests");

        for (final ColineRequest r : this.requests) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    r.getColineBuilder().exec();
                    pendingRequests -= 1;
                }
            }).start();
        }
    }

    // Get the number of pending request in the current queue
    public boolean getPending() {
        return this.pendingRequests > 0;
    }

    // Get the number of pending request in the current queue
    public boolean getState() {
        return this.used;
    }

    public void destroyCurrentQueue() throws Throwable {
        queue.finalize();
        queue = null;
        if ( logs )
            Log.d(CO_LINE_QUEUE, "Current queue is destroyed");
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
        if ( status ) Log.d(CO_LINE_QUEUE, "Logs activation!");
        ColineLogs.setStatus(status);
    }
}
