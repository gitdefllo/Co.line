package com.fllo.co.line;

/****************************************************
 * Co.lineHttpMethod
 * -----------------
 * @author  Fllo (@Gitdefllo) 2015
 *
 * Repository: https://github.com/Gitdefllo/Co.line.git
 *
 *****************************************************
 * Description
 * ------------
 *
 * Set a request method using:
 *      ColineHttpMethod.GET    = 0x00001;
 *      ColineHttpMethod.POST   = 0x00002;
 *      ColineHttpMethod.PUT    = 0x00003;
 *      ColineHttpMethod.DELETE = 0x00004;
 *      ColineHttpMethod.HEAD   = 0x00005;
 *
 *****************************************************/
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
     * @see           ColineHttpMethod
     */
    public ColineHttpMethod() { }

    /**
     * This method gets the method request in String header field.
     *
     * @param method (int) Value corresponding to static int in this class
     * @return       A String like "GET" or "POST"
     * @see          ColineHttpMethod
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
