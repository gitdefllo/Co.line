package com.fllo.co.line.models;

public class CoResponse {

    public int status;
    public String body;

    public CoResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }
}
