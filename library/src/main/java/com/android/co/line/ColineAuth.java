package com.android.co.line;

/****************************************************
 * Co.lineAuth
 * -----------
 * @version 0.0.4
 * @author  Fllo (@Gitdefllo) 2015
 *
 * Repository: https://github.com/Gitdefllo/Co.line.git
 *
 *****************************************************
 * Description
 * ------------
 *
 * Set a header field (or api key in url) using:
 *
 *      ColineAuth.BASIC_AUTH   = 0x00001;
 *      ColineAuth.OAUTH_2      = 0x00002;
 *
 *****************************************************/
public class ColineAuth {
    // Authentication fields
    public static final int BASIC_AUTH = 0x00001;
    public static final int OAUTH_2    = 0x00002;

    // Constructor
    public ColineAuth() { }

    // Retrieve authentication header
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
