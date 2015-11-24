package com.fllo.co.line;

/**
 * Interface to return server response, it has three methods:
 * <p>
 *      - onSuccess(String): returns a String server response when Http
 *      status is equals to 200.
 * <p>
 *      - onError(String): returns a String server response when
 *      Http response is not a success and contains 'error' characters
 *      chained.
 * <p>
 *      - onFail(String): returns a String method response when an error
 *      occurred in Coline class.
 *
 */
public interface ColineResponse {
    void onSuccess(String s);
    void onError(String s);
    void onFail(String s);
}
