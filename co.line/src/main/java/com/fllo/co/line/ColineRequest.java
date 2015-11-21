/*
 * Fllo, All rights reserved - 2015 (@Gitdefllo)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fllo.co.line;

/*
 * Co.lineRequest
 * -----------------
 * @author  Fllo (@Gitdefllo) 2015
 *
 * Model of request http method.
 *
 */
public class ColineRequest {

    private static Coline builder;
    private int requestId;
    private String url;
    private ColineHttpMethod http;
    private ColineAuth auth;
    private ColineLogs logs;

    public ColineRequest(Coline builder) {
        this.builder = builder;
    }

    public static void setColineBuilder(Coline builder) {
        ColineRequest.builder = builder;
    }

    public static Coline getColineBuilder() {
        return ColineRequest.builder;
    }

    public void setRequestId(int requestId) {
        this.builder = builder;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setHttp(ColineHttpMethod http) {
        this.http = http;
    }

    public ColineHttpMethod getHttp() {
        return this.http;
    }

    public void setAuth(ColineAuth auth) {
        this.auth = auth;
    }

    public ColineAuth getAuth() {
        return this.auth;
    }

    public void setLogs(ColineLogs logs) {
        this.logs = logs;
    }

    public ColineLogs getLogs() {
        return this.logs;
    }
}
