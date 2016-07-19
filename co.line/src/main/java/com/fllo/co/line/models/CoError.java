package com.fllo.co.line.models;

public class CoError {

    public int status;
    public String exception;
    public String stacktrace;
    public String description;

    /**
     * CoError object handles error which occured in connection
     *
     * @param status (int) HTTP response status
     * @param exception (String) Raised exception (can be 'null')
     * @param stacktrace (String) Stacktrace of the exception (can be 'null')
     * @param description (String) Little description of the error
     */
    public CoError(int status, String exception,
                   String stacktrace, String description) {
        this.status = status;
        this.exception = exception;
        this.stacktrace = stacktrace;
        this.description = description;
    }
}
