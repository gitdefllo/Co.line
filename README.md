Co.line
=======

**REST connection lib in one line for Android.**

Co.line is a custom library to do a HttpURLConnection in REST. Main advantage: using only one and simple line declaration by chained methods. It creates automatically a new Thread and returns result in callbacks main Thread. You can also specify a BasicAuth/OAuth2.0 authorization key.

Download
--------

Via gradle
```java
compile 'com.fllo.co.line:co.line:1.0.9'
```
or maven
```xml
<dependency>
  <groupId>com.fllo.co.line</groupId>
  <artifactId>co.line</artifactId>
  <version>1.0.9</version>
</dependency>
```

Usage
------

Do a request and retrieve it as:
```java
Coline.init(this)
        // Set the HTTP method (GET, PUT..) and the URL, then get response in ColineResponse
        .url(ColineHttpMethod.GET, "http://api.url.com/").res(response)
        // Execute the request
        .exec();
```

You can do a request in BasicAuth or OAuth2.0:
```java
Coline.init(context)
        .url(ColineHttpMethod.GET, "http://api.url.com/username")
        .auth(ColineAuth.BASIC_AUTH, "eDzp2DA1ezD48S6LSfPdZCab0")
        .res(new ColineResponse() {
            @Override
            public void onSuccess(String s) { }

            @Override
            public void onError(String s) { }

            @Override
            public void onFail(String s) { }
        })
        .exec();
```

Request queue
-------

Set multiple requests in queue and launch all at one time:
```java
// Prepare a first request
Coline.init(this)
        .url(ColineHttpMethod.GET, "http://api.url.com/username")
        .res(response)
        .queue();

// Prepare another request
Coline.init(getActivity())
        .url(ColineHttpMethod.GET, "http://api.url.com/messages")
        .res(otherResponse)
        .queue();

// Finally, launch the queue
Coline.init(MainActivity.this).send();
```

Next features (Todo)
-------

- Personnalization of auth method;
- In callbacks, apply a custom model for the result string like `.res(myModel, mCallback)`;
- ~~Clear the current queue once it's used';~~ *(done)*
- ~~Using a queue for thread connection with a method in order to add a request to the current queue;~~ *(done)*

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
public Coline url(int method, String url)
```
Pass the HTTP method and the URL.
```java
Coline.url(ColineHttpMethod.POST, "http://api.url.com/user");
```
`ColineHttpMethod` class handles the following requests: `GET`, `POST`, `PUT`, `DELETE` and `HEAD`.

**Authenticate**

```java
public Coline auth(int auth, String token)
```
Add the Authorization header field.
```java
Coline.auth(ColineAuth.OAUTH_2, "e53rqEK0ydzH5kleR98t9r6Eim");
```
`ColineAuth` class handles the following authenticates: `Basic` and `Bearer`.

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
public Coline res(ColineResponse response)
```
Called by `res()`, the request response is a `String` and can be retrieved in `ColineResponse` interface, which implements three methods:
- `onSuccess`: everything went fine,
- `onError`: response contains an error,
- `onFail`: internal Coline connection error.
```java
Coline.res(new ColineResponse() {
    @Override
    public void onSuccess(String s) { }

    @Override
    public void onError(String s) { }

    @Override
    public void onFail(String s) { }
})
```

**Current queue**

```java
public void queue()
public void send()
```
These methods are used to fetch and batch multiple requests later at one time.
To add a request to the current queue, call `queue()` at the end:
```java
Coline.init(contextA).url(ColineHttpMethod.GET, urlA).res(responseA).queue();
...
Coline.init(contextB).url(ColineHttpMethod.POST, urlB).with(valuesB).res(responseB).queue();
...
```
Then, call `send()` in order to launch all request in the current queue:
```java
Coline.init(contextC).send();
```

**Execution**

*Note: it needs to be declared at the end.*
```java
public void exec()
```

Debugging
---------

Logs are disabled by default. If you want to enable it, just add the following method before `init()`:
```java
ColineLogs.activateLogs(true);
```

Version  
-------

######v.1.0.7-v.1.0.9:
- Using a queue to send requests;
- Update Javadoc (publishing Javadoc issue)

######v.1.0.6:
- License documentation;

######v.1.0.5:
- Co.line uses more logs;

<a href="https://github.com/Gitdefllo/Co.line/blob/master/VERSIONS.md">See older versions</a>

License
--------

    Florent Blot (Fllo, @Gitdefllo), all rights reserved - 2015
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
