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
 * Co.lineResponse
 * ---------------
 * @author  Florent Blot (@Gitdefllo)
 *
 * Interface to return server response in three methods:
 *  - onSuccess
 *  - onError
 *  - onFail
 *
 * Co.line handle and treat server response to retrieve it
 * in the specific response condition above.
 *
 */
public interface ColineResponse {

    /**
     * This method returns a String server response when Http Status
     * equals to 200.
     *
     * @param s (String) Success response from server side
     */
    void onSuccess(String s);

    /**
     * This method returns a String server response when Http response
     * is not a {@link #onSuccess(String s)} or contains 'error' word.
     *
     * @param s (String) Error response from server side
     */
    void onError(String s);

    /**
     * This method returns a String method response when an error occurred
     * in Coline class. It can be a wrong URL format, an open connection's
     * problem, a response not catch, etc.
     *
     * @param s (String) Error from Coline side
     */
    void onFail(String s);
}
