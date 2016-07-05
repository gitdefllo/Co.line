Co.line
=======

**REST connection lib in one line for Android.**

Co.line is a custom library to do a HttpURLConnection in REST. Main advantage: using only one and simple line declaration by chained methods. It creates automatically a new Thread and returns result in callbacks main Thread. You can also specify a BasicAuth/OAuth2.0 authorization key.

Download
--------

Via gradle
```java
compile 'com.fllo.co.line:co.line:1.0.15'
```
or maven
```xml
<dependency>
  <groupId>com.fllo.co.line</groupId>
  <artifactId>co.line</artifactId>
  <version>1.0.15</version>
</dependency>
```

Usage
------

Do a request and retrieve it as:
```java
Coline.init(this)
        // Set the HTTP method (GET, PUT..) and the URL
        .url(CoHttp.GET, "http://api.url.com/")
        // Then get response in CoResponse class
        .res(new CoResponse() {
            @Override
            public void onSuccess(String s) { /** Handle successful response */ }
            @Override
            public void onFail(String s) { /** Handle bad response */ }
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

Next features (Todo)
-------

- Adding other auth methods;
- Populate a custom model with `.res(myCustomModel)`;

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
`CoHttp` class handles the following requests: `GET`, `POST`, `PUT`, `DELETE` and `HEAD`.

**Authenticate**

```java
public Coline auth(CoAuth auth, String token)
```
Add the Authorization header field.
```java
Coline.auth(CoAuth.OAUTH_2, "e53rqEK0ydzH5kleR98t9r6Eim");
```
`CoAuth` class handles the following authenticates: `Basic` and `Bearer`.

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
public Coline res(CoResponse response)
```
Called by `res()`, the request response is a `String` and can be retrieved in `CoResponse` interface, which implements three methods:
- `onSuccess`: everything went fine,
- `onFail`: response contains an error.
```java
Coline.res(new CoResponse() {
    @Override
    public void onSuccess(String s) { }

    @Override
    public void onFail(String s) { }
});
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

Version  
-------

######v.1.0.14 - 1.0.15:
- Merging Manifest file resolved

######v.1.0.13:
- Minor app name changes

######v.1.0.12:
- Change classes names and methods
- Passing enums instead of String

<a href="https://github.com/Gitdefllo/Co.line/blob/master/VERSIONS.md">See older versions</a>

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
