package com.android.co.line;

/****************************************************
 * Co.lineLogs
 * -----------
 * @version 0.0.5
 * @author  Fllo (@Gitdefllo) 2015
 *
 * Repository: https://github.com/Gitdefllo/Co.line.git
 *
 *****************************************************
 * Description
 * ------------
 *
 * Des/Activate logs in Co.line
 *
 *****************************************************/
public class ColineLogs {
    // Status
    public static boolean status = false;

    // Values
    public static final boolean enable = true;
    public static final boolean disable = false;

    // Set the status
    public static void setStatus(boolean status) {
        ColineLogs.status = status;
    }

    // Get the status
    public static boolean getStatus() {
        return status;
    }
}
