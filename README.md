Co.line
=======

**REST connection lib in one line for Android.**

Co.line is a custom library to do a HttpURLConnection in REST. Main advantage: using only one and simple line declaration by chained methods. It creates automatically a new Thread and returns result in callbacks main Thread. You can also specify a BasicAuth/OAuth2.0 authorization key.

Download
--------

Via gradle
```java
compile 'com.fllo.co.line:co.line:2.0.0'
```
or maven
```xml
<dependency>
  <groupId>com.fllo.co.line</groupId>
  <artifactId>co.line</artifactId>
  <version>2.0.0</version>
</dependency>
```

Usage
------

A simple request:
```java
Coline.init(this)
        // set the HTTP method (GET, PUT..) and the URL
        .url(CoHttp.GET, "http://api.url.com/")
        // then get response in CoResponse class
        .res(new CoCallback() {
            @Override
            public void onResult(CoError err, CoResponse res) {
                // check error
                if (err != null) {
                    // handle error
                }

                // handle successful server response
                Log.i("Coline.onResult", "onResult(): status: " + res.status+", body: " + res.body);
            }
        })
        // Execute the request
        .exec();
```

Request queue
-------

Set multiple requests in queue and launch all at one time:
```java
// Prepare a first request
Coline.init(this)
        .url(CoHttp.GET, "http://api.url.com/user")
        .res(userResponse)
        .queue();

// Prepare another request
Coline.init(this)
        .url(CoHttp.GET, "http://api.url.com/user/messages")
        .res(msgResponse)
        .queue();

// Finally, launch the queue
Coline.init(this).send();
```

Documentation
-------

**Initialization**

```java
public static Coline init(Context context)
```
Initialise with the current Context.
```java
Coline.init(MainActivity.this);
```

**Request methods**

```java
public Coline url(CoHttp method, String url)
```
Pass the HTTP method and the URL.
```java
Coline.url(CoHttp.POST, "http://api.url.com/user");
```
`CoHttp` class handles the following requests: `GET`, `POST`, `PUT`, `PATCH`, `DELETE` and `HEAD`.

**Headers**

```java
public Coline head(ContentValues params)
```
Pass the header properties, usually keys/values pairs, into the request.
```java
ContentValues params = new ContentValues();
params.put("Content-Type", "application/json");
params.put("Charset", "UTF-8");
// Values to put in header properties
Coline.head(params);
```
Or, it's possible to pass an Object array with this following pattern:
```java
Coline.head("Content-Type", "application/json", "Charset", "UTF-8");
```
And you can also pass an `ArrayMap<String, Object>` **(only API 19 and higher)**:
```java
ArrayMap<String, Object> params = new ArrayMap();
params.put("Content-Type", "application/json");
params.put("Charset", "UTF-8");
Coline.head(params);
```
If non-set, the default header properties are `content-type=application/x-www-form-urlencoded` and `charset=UTF-8`

**Parameters**

```java
public Coline with(ContentValues values)
```
Pass the parameters, generally keys/values pairs, to the server.
```java
ContentValues values = new ContentValues();
values.put("username", "Fllo");
values.put("github",   "Gitdefllo");
// Values to send in body request
Coline.with(values);
```
Or, it's possible to pass an Object array with this following pattern:  
```java
Coline.with("username", "Fllo", "github", "Gitdefllo");
```
And you can also pass an `ArrayMap<String, Object>` **(only API 19 and higher)**:
```java
ArrayMap<String, Object> values = new ArrayMap();
values.put("userid",   123456);
values.put("username", "Fllo");
Coline.with(values);
```

**Callbacks**

```java
public Coline res(CoCallback callback)
```
Called by `res()`, the request response is a couple `CoError` and `CoResponse` objects:
```java
Coline.res(new CoCallback() {
    @Override
    public void onResult(CoError err, CoResponse res) { }
});
```

`CoError` handles connection error and has these properties:
```java
error.status (int) Status of server response - if non-set, equals to 0
error.exception (String) Name of exeption if occurred (NPE, IEO, etc) - if non-set, equals to null
error.stacktrace (String) Stacktrace of exeption if occurred - if non-set, equals to null
error.description (String) Short readable description of error
```

`CoResponse` returns server successful response and has these properties:
```java
res.status (int) Status of response server
res.body (String) Body of response server
```

**Current queue**

```java
public void queue()
public void send()
```
These methods are used to fetch and batch multiple requests later at one time.
To add a request to the current queue, call `queue()` at the end:
```java
Coline.init(context).url(CoHttp.GET, urlA).res(responseA).queue();
...
Coline.init(context).url(CoHttp.POST, urlB).with(valuesB).res(responseB).queue();
...
```
Then, call `send()` in order to launch all request in the current queue:
```java
Coline.init(context).send();
```

**Execution**

*Note: it needs to be declared at the end.*
```java
public void exec()
```

Debugging
---------

Logs are disabled by default. If you want to enable it, just call the following method:
```java
CoLogs.activate();
```
To disable it, call the static method `desactivate()`:
```java
CoLogs.desactivate();
```

License
--------

    Copyright 2016 Florent Blot
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
