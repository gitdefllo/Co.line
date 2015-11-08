package com.android.co.line;

/****************************************************
 * Co.lineHttpMethod
 * -----------------
 * @version 0.0.4
 * @author  Fllo (@Gitdefllo) 2015
 *
 * Repository: https://github.com/Gitdefllo/Co.line.git
 *
 *****************************************************
 * Description
 * ------------
 *
 * Set a request method using:
 *
 *      ColineMethod.GET    = 0x00001;
 *      ColineMethod.POST   = 0x00002;
 *      ColineMethod.PUT    = 0x00003;
 *      ColineMethod.DELETE = 0x00004;
 *      ColineMethod.HEAD   = 0x00005;
 *
 *****************************************************/
public class ColineHttpMethod {
    // Methods
    public static final int GET    = 0x00001;
    public static final int POST   = 0x00002;
    public static final int PUT    = 0x00003;
    public static final int DELETE = 0x00004;
    public static final int HEAD   = 0x00005;

    // Constructor
    public ColineHttpMethod() { }

    // Retrieve authentication header
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
