package com.fllo.co.line;

/****************************************************
 * Co.lineAuth
 * -----------
 * @author  Fllo (@Gitdefllo) 2015
 *
 * Repository: https://github.com/Gitdefllo/Co.line.git
 *
 *****************************************************
 * Description
 * ------------
 *
 * Set a header field (or api key in url) using:
 *      ColineAuth.BASIC_AUTH   = 0x00001;
 *      ColineAuth.OAUTH_2      = 0x00002;
 *
 *****************************************************/
public class ColineAuth {
    // Authentication fields
    public static final int BASIC_AUTH = 0x00001;
    public static final int OAUTH_2    = 0x00002;

    /**
     * Co.lineAuth's constructor: method not used.
     *
     * @see           ColineAuth
     */
    public ColineAuth() { }

    /**
     * This method gets the Authentication header field.
     *
     * @param auth (int) Value corresponding to static int in this class
     * @return     A String like "Basic " or "Bearer "
     * @see        ColineAuth
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
