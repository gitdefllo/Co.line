package com.fllo.co.line;

/****************************************************
 * Co.lineLogs
 * -----------
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

    /**
     * This method gets the method request in String header field.
     *
     * @param status (boolean) Value to set for logs
     * @see          ColineLogs
     */
    public static void setStatus(boolean status) {
        ColineLogs.status = status;
    }

    /**
     * This method returns the status'logs for Co.line class.
     *
     * @return       A boolean
     * @see          ColineLogs
     */
    public static boolean getStatus() {
        return status;
    }
}
