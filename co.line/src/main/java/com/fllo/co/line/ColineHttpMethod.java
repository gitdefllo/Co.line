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
 * Co.lineHttpMethod
 * -----------------
 * @author  Florent Blot (@Gitdefllo)
 *
 * Set a request method using:
 *      ColineHttpMethod.GET    = 0x00001;
 *      ColineHttpMethod.POST   = 0x00002;
 *      ColineHttpMethod.PUT    = 0x00003;
 *      ColineHttpMethod.DELETE = 0x00004;
 *      ColineHttpMethod.HEAD   = 0x00005;
 *
 */
public class ColineHttpMethod {

    // Methods
    public static final int GET    = 0x00001;
    public static final int POST   = 0x00002;
    public static final int PUT    = 0x00003;
    public static final int DELETE = 0x00004;
    public static final int HEAD   = 0x00005;

    /**
     * Co.lineHttpMethod's constructor: method not used.
     *
     */
    public ColineHttpMethod() { }

    /**
     * This method gets the method request in String header field.
     *
     * @param method (int) Value corresponding to static int in this class
     * @return       A String like "GET" or "POST"
     */
    public String getMethod(int method) {
        switch (method) {
            case GET:
                return "GET";
            case POST:
                return "POST";
            case PUT:
                return "PUT";
            case DELETE:
                return "DELETE";
            case HEAD:
                return "HEAD";
        }
        return null;
    }
}
