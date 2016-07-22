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
package com.fllo.co.line.builders;

import android.util.Log;

import com.fllo.co.line.BuildConfig;

public class Logs {

    // Tags
    private static final String CO_LINE  = "Co.line";

    // Configuration
    private static Logs instance = null;

    // Status
    private boolean status = false;

    /**
     * Protected: Logs'empty constructor.
     *
     */
    protected Logs() { }

    /**
     * This method gets the current instance or creates a new one.
     *
     * @return       An instance of logs class
     */
    public static Logs getInstance() {
        if (instance == null) {
            Logs.instance = new Logs();
            Log.i(CO_LINE, "Initialization, version " + BuildConfig.VERSION_NAME);
        }
        return instance;
    }

    /**
     * This method sets the method request.
     *
     * @param status (boolean) Value to set for logs
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * This method returns the status'logs for Co.line class.
     *
     * @return       A boolean
     */
    public boolean getStatus() {
        return this.status;
    }
}
