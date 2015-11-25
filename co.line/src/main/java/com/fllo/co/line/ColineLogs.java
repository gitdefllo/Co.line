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

import android.util.Log;

/*
 * Co.lineLogs
 * -----------
 * @author  Fllo (@Gitdefllo) 2015
 *
 * Des/Activate logs in Co.line
 *
 */
public class ColineLogs {

    // Tags
    private static final String CO_LINE  = "Co.line";

    // Configuration
    private static ColineLogs instance = null;

    // Status
    public boolean status = false;

    /**
     * ColineLogs'empty constructor.
     *
     * @see          ColineLogs
     */
    public ColineLogs() { }

    /**
     * This method gets the current instance or creates a new one.
     *
     * @see          ColineLogs
     */
    public static ColineLogs getInstance() {
        if (instance == null) {
            ColineLogs.instance = new ColineLogs();
        }
        return instance;
    }

    /**
     * This method gets the method request in String header field.
     *
     * @param status (boolean) Value to set for logs
     * @see          ColineLogs
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * This method returns the status'logs for Co.line class.
     *
     * @return       A boolean
     * @see          ColineLogs
     */
    public boolean getStatus() {
        return this.status;
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
        if ( status )
            Log.i(CO_LINE, "Initialization, version " + BuildConfig.VERSION_NAME);
        ColineLogs.getInstance().setStatus(status);
    }
}
