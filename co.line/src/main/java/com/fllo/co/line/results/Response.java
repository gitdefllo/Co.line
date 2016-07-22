package com.fllo.co.line.results;

public class Response {

    public int status;
    public String body;

    /**
     * Error object handles error which occured in connection
     *
     * @param status (int) HTTP response status
     * @param body (String) Body response of server
     */
    public Response(int status, String body) {
        this.status = status;
        this.body = body;
    }
}
