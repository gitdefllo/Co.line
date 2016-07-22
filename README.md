Co.line
=======

**REST connection lib for Android.**

Co.line is a custom library to do a HttpURLConnection in REST. It creates automatically a new Thread and returns result in a callback on the Main Thread. You can specify header's properties (@see `head()`), add params in the body request (@see `with()`) and even interrupt the background Thread (@see `cancel()`).

Download
--------

Via gradle
```java
compile 'com.fllo.co.line:co.line:2.1.0'
```
or maven
```xml
<dependency>
  <groupId>com.fllo.co.line</groupId>
  <artifactId>co.line</artifactId>
  <version>2.1.0</version>
</dependency>
```

Usage
------

A simple request:
```java
Coline.init(this)
        // set the HTTP method (GET, PUT, etc) and the URL
        .url(HttpMethod.GET, "http://api.url.com/")
        // then get the result in Collback class
        .res(new Collback() {
            @Override
            public void onResult(Error err, Response res) {
                // check for errors
                if (err != null) {
                    // handle error
                    return;
                }

                // handle successful server response
                Log.i("Coline.onResult()", "Response: status: " + res.status+", body: " + res.body);
            }
        })
        // execute the request
        .exec();
```

Request queue
-------

Set multiple requests in queue and launch all at one time:
```java
// prepare a first request
Coline.init(this)
        .url(HttpMethod.GET, "http://api.url.com/user")
        .res(userResponse)
        .queue();

// prepare another request
Coline.init(this)
        .url(HttpMethod.GET, "http://api.url.com/user/messages")
        .res(msgResponse)
        .queue();

// finally, launch the queue
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
public Coline url(HttpMethod method, String url)
```
Pass the HTTP method and the URL.
```java
Coline.url(HttpMethod.POST, "http://api.url.com/user");
```
`HttpMethod` class handles the following requests: `GET`, `POST`, `PUT`, `PATCH`, `DELETE` and `HEAD`.

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
It's possible to use an `ArrayMap<String, Object>` **(only API 19 and higher)**.  
If non-set, the default header properties are `content-type=application/x-www-form-urlencoded;charset=UTF-8`

**Body**

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
It's possible to use an `ArrayMap<String, Object>` **(only API 19 and higher)**.
```

**Callbacks**

```java
public Coline res(Collback collback)
```
Called by `res()`, the request response is a couple `Error` and `Response` objects:
```java
Coline.res(new Collback() {
    @Override
    public void onResult(Error err, Response res) { }
});
```

**All requests from below 200 and above 299 are considered as an `Error`.**
`Error` handles connection error. This object has these properties:
```java
Error.status (int) Status of server response - if non-set, equals to 0
Error.exception (String) Name of exeption if occurred (NPE, IEO, etc) - if non-set, equals to null
Error.stacktrace (String) Stacktrace of exeption if occurred - if non-set, equals to null
Error.description (String) Short readable description of error
```

`Response` returns server successful response and has these properties:
```java
Response.status (int) Status of response server
Response.body (String) Body of response server
```

**Current queue**

```java
public void queue()
public void send()
```
These methods are used to fetch and batch multiple requests later at one time.
To add a request to the current queue, call `queue()` at the end:
```java
Coline.init(context).url(HttpMethod.GET, urlA).res(responseA).queue();
...
Coline.init(context).url(HttpMethod.POST, urlB).with(valuesB).res(responseB).queue();
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

**Cancellation**

```java
public void cancel()
```

By making a global Coline variable, you can stop in safe mode the background treatment:
```java

Coline colineService;
...
colineService = Coline.init(this);

colineService.url(HttpMethod.GET, mUrl)
             .res(mResult)
             .exec();
...

@Override
public void onDestroy() {
    if (colineService != null) {
        colineService.cancel();
        colineService = null;
    }
    super.onDestroy();
}
```

Debugging
---------

Logs are disabled by default. If you want to enable it, just call the following method before `init()`:
```java
Coline.enableDebug();
```
To disable it, call the static method `disableDebug()`:
```java
Coline.disableDebug();
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
