Co.line
=======

**REST connection lib in one line for Android.**

Co.line is a custom library to do a HttpURLConnection in REST. Main advantage: using only one and simple line declaration by chained methods. It creates automatically a new Thread and returns result in callbacks main Thread. You can also specify a BasicAuth/OAuth2.0 authorization key.

Download
--------

Via gradle
```java
compile 'com.fllo.co.line:co.line:1.0.6'
```
or maven
```xml
<dependency>
  <groupId>com.fllo.co.line</groupId>
  <artifactId>co.line</artifactId>
  <version>1.0.6</version>
</dependency>
```

Usage
------

Do a request and retrieve it as:
```java
Coline.init(this)
        // Set the HTTP method (GET, PUT..) and the URL, then retrieve it in a callback
        .url(ColineHttpMethod.GET, "http://api.url.com/").success(getSuccess)
        // Execute the request
        .exec();
```

You can do a request in BasicAuth or OAuth2.0:
```java
Coline.init(context)
        .url(ColineHttpMethod.GET, "http://api.url.com/username")
        .auth(ColineAuth.BASIC_AUTH, "eDzp2DA1ezD48S6LSfPdZCab0")
        .success(new Coline.Success() {
            @Override
            public void onSuccess(String s) {
                ...
            }
        })
        .exec();
```

Request queue
-------

Set multiple requests in queue and launch one time:
```java
Coline.init(MainActivity.this)
        .url(ColineHttpMethod.GET, "http://api.url.com/username")
        // Add to a current queue this request
        .queue();

// In another class
Coline.init(getActivity())
        .url(ColineHttpMethod.GET, "http://api.url.com/messages")
        .success(new Success...)
        .error(new Error...)
        .queue();

// Finally, launch the queue (in other Context, why not?)
Coline.init(getActivity())
        .url(ColineHttpMethod.GET, "http://api.url.com/contacts")
        // Execute the current queue
        .exec();
```

Next features (Todo)
-------

- Personnalization of auth method;
- In callbacks, apply a custom model for the result string like `.success(myModel, mCallback)`;
- ~~Using a queue for thread connection with a method in order to add a request to the current queue;~~ *(done)*

Documentation
-------

**Initialization**

```java
public static Coline init(Context context)
```
Initialise with the current Context.
```java
ex: Coline.init(MainActivity.this);
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

**Execution**

*Note: it needs to be declared at the end.*
```java
public void exec()
```

Debugging
---------

Logs are disabled by default. If you want to enable it, just add the following method before `init()`:
```java
Coline.activateLogs(true);
Coline.init(this)
        .url(ColineHttpMethod.GET, "http://api.url.com/").exec();
```

Version  
-------

######v.1.0.7:
- Using a queue to send requests;

######v.1.0.6:
- License documentation;

######v.1.0.5:
- Co.line uses more logs;

<a href="https://github.com/Gitdefllo/Co.line/blob/master/VERSIONS.md">See older versions</a>

License
--------

    Fllo, All rights reserved - 2015 (@Gitdefllo)
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
