package com.fllo.co.line;

/*
 * Co.lineResponse
 * ---------------
 * @author  Fllo (@Gitdefllo) 2015
 *
 * Interface to return server response in three methods:
 *  - onSuccess
 *  - onError
 *  - onFail
 * <p>
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
     * is not a {@link #onSuccess} or contains 'error' word.
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
