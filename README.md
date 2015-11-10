Co.line
=======    
*v.1.0.4*
**REST connection lib in one line for Android.**

Co.line is a custom library to do a HttpURLConnection in REST.
The advantage: using only one line declaration by chained methods.
It creates automatically a new Thread and returns result in callbacks UI Thread. You can also specify a BasicAuth/OAuth2.0 authorization key.

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
- Using a queue for thread connection with a method to add a request to the current queue;
- In callbacks, apply a custom model for the result string like `.success(myModel, mCallback)`;
- ~~Add a value to des/activate logs in `init()` method;~~
- ~~Make a simple example with an API~~

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
Or you can pass an `ArrayMap` (only API 19 and higher):
```java
ArrayMap<String, String> values = new ArrayMap();
values.put("username", "Fllo");
Coline.with(values);
```
Finally, it's also possible to pass a String array with this following pattern:
```java
Coline.with("username", "Fllo", "github", "Gitdefllo");
```

**Callbacks**
Co.line let you handle two different callbacks `success` and `error`.  
Handle successful request with `Coline.Success()`:
```java
Coline.success(new Coline.Success() {
    @Override
    public void onSuccess(String s) {
        // Where "s" is the response server
        Log.v(CO_LINE, "Success - Server response: "+s);
    }
});
```
An error is returned a `String` which can be cast to `JSONObject`, and it retrieves as follows:
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
Coline.init(this);
```

Version
-------

######v.1.0.4:
- Package renamed;
- Publication with jcenter;

######v.1.0.3:
- Support ArrayMap;
- Javadoc generated;
- Internal error response into JSON format in String;

######v.1.0.2:
- Maven library modification;
- Logs method activation;

######v.1.0.1:
- Maven library creation;
- Creation of ColineHttpMethod class;
- ColineAuth improved and retrieve header fields;

######v.1.0.0:
- Deploy in library project;
- Handle OAuth2.0;
- Callbacks separation 'success' and 'error';
- Context handler;

Contribution
-------

Developed by Fllo (@Gitdefllo) 2015.  
Feel free to contribute, improve or use.
