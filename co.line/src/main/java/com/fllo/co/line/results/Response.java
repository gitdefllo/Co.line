package com.fllo.co.line.results;

public class Response {

    public int status;
    public String body;

    /**
     * Response object handles string results from server
     *
     * @param status (int) HTTP response status
     * @param body (String) Body response of server
     */
    public Response(int status, String body) {
        this.status = status;
        this.body = body;
    }
}
