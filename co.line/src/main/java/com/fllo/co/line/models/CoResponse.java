package com.fllo.co.line.models;

public class CoResponse {

    public int status;
    public String body;

    /**
     * CoError object handles error which occured in connection
     *
     * @param status (int) HTTP response status
     * @param body (String) Body response of server
     */
    public CoResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }
}
