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
```java
Coline.init(this).url(ColineHttpMethod.GET, "http://api.url.com/").exec();
```
You also can do request with BasicAuth and OAuth2.0. This example uses a BasicAuth and retrieves the request result in a success callback:  
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

Next features (Todo)
-------
- Using a queue for thread connection with a method in order to add a request to the current queue;
- In callbacks, apply a custom model for the result string like `.success(myModel, mCallback)`;
- ~~Add a value to des/activate logs in `init()` method;~~ *(done)*
- ~~Make a simple example with an API~~ *(done)*

Documentation
-------
**Initialization**
```java
public static Coline init(Context context)
```
*example:*
```java
Coline.init(MainActivity.this);
```

**Request methods**
```java
public Coline url(int method, String url)
```
*example:*
```java
Coline.url(ColineHttpMethod.POST, "http://api.url.com/user");
```
`ColineHttpMethod` class handles the following requests: `GET`, `POST`, `PUT`, `DELETE` and `HEAD`.

**Authenticate**
```java
public Coline auth(int auth, String token)
```
*example:*
```java
Coline.auth(ColineAuth.OAUTH_2, "e53rqEK0ydzH5kleR98t9r6Eim");
```
`ColineAuth` class handles the following authenticates: `Basic` and `Bearer`.

**Parameters**
```java
public Coline with(ContentValues values)
```
*example:*
```java
ContentValues values = new ContentValues();
values.put("username", "Fllo");
values.put("github",   "Gitdefllo");
Coline.with(values);
```
Or, it's possible to pass an Object array with this following pattern:  
```java
Coline.with("username", "Fllo", "github", "Gitdefllo");
```
And you can also pass an `ArrayMap<String, Object>` **(only API 19 and higher)**:
```java
ArrayMap<String, String> values = new ArrayMap();
values.put("username", "Fllo");
Coline.with(values);
```  

**Callbacks**

Co.line lets you handle two different callbacks `success` and `error`.
Handle successful requests with `Coline.Success()`:
```java
Coline.success(new Coline.Success() {
    @Override
    public void onSuccess(String s) {
        // Where "s" is the response server
        Log.v(CO_LINE, "Success - Server response: "+s);
    }
});
```
Errors return a `String` which can be cast to `JSONObject`, and it is built as follows:
```java
Coline.error(new Coline.Error() {
    @Override
    public void onError(String s) {
        // Where "s" is the response of server
        Log.v(CO_LINE, "Error - Server response: "+s);
    }
});
```

**Execution**

*Note: it should be declared at the end.*
```java
public void exec()
```

Debugging  
---------  
Logs are disabled by default. If you want to enable it, just add the following method before `init()`:  
```java
Coline.activateLogs(true);
Coline.init(this).url(ColineHttpMethod.GET, "http://api.url.com/").exec();
```

Version  
-------
######v.1.0.6:
- License documentation;

######v.1.0.5:
- Co.line uses more logs;

<a href="https://github.com/Gitdefllo/Co.line/blob/master/VERSIONS.md">See old versions</a>

Contribution  
------------  
Developed by Fllo (@Gitdefllo) 2015.  
Feel free to contribute, improve or use.

License
--------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
