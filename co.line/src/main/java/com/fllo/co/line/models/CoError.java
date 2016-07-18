package com.fllo.co.line.models;

public class CoError {

    public int status;
    public String exception;
    public String stacktrace;
    public String description;

    public CoError(int status, String exception,
                   String stacktrace, String description) {
        this.status = status;
        this.exception = exception;
        this.stacktrace = stacktrace;
        this.description = description;
    }
}
