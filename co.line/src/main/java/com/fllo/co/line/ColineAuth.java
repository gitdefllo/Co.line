/*
 * Copyright 2015 Florent Blot
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

/*
 * Co.lineAuth
 * -----------
 * @author  Florent Blot (@Gitdefllo)
 *
 * Set a header field using:
 *      ColineAuth.BASIC_AUTH   = 0x00001;
 *      ColineAuth.OAUTH_2      = 0x00002;
 *
 */
public class ColineAuth {

    // Authentication fields
    public static final int BASIC_AUTH = 0x00001;
    public static final int OAUTH_2    = 0x00002;

    /**
     * Co.lineAuth's constructor: method not used.
     *
     */
    public ColineAuth() { }

    /**
     * This method gets the Authentication header field.
     *
     * @param auth (int) Value corresponding to static int in this class
     * @return     A String like "Basic " or "Bearer "
     */
    public String getAuthHeader(int auth) {
        switch (auth) {
            case BASIC_AUTH:
                return "Basic ";
            case OAUTH_2:
                return "Bearer ";
        }
        return null;
    }
}
