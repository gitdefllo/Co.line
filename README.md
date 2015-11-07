Co.line
=======    
*v.0.0.2*  
**REST connection class with chained methods for Android.**   

Co.line is a custom class to do a HttpURLConnection in REST.   
The advantage is using one line declaration by chained methods, it creates automatically a new Thread and returns results in callbacks UI Thread. You can also specify a BasicAuth authorization key.

Usage
------
```java
Coline.init(context)
        .url("GET", "http://api.url.com")
        .auth("eDzp2DA1ezD48S6LSfPdZCab0")
        .with(values)
        .success(successCallback)
        .error(errorCallback)
        .exec();
```

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
public Coline url(String method, String url)
```
*example:*
```java
Coline.url("GET", "https://api.github.com/users");
```

**Basic Auth**
*Note: it's only support BasicAuth.*
```java
public Coline auth(String authentification)
```
*example:*
```java
Coline.auth("e53rqEK0ydzH5kleR98t9r6Eim");
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
Or it's also possible to pass a String array with this following pattern:
```java
Coline.with("key1", "value1", "key2", "value2", "key3", "value3");
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
An error statement is based on errors in communication with server, a `String` containing the word `error` in response or by a `JSONObject` in your api response as `{ "error": "an error occurred." }`.  
It retrieves via `Coline.Error()` callback:
```java
Coline.error(new Coline.Error() {
    @Override
    public void onError(String s) {
        // Where "s" is the response server or the communication error
        Log.v(CO_LINE, "Error - Server response: "+s);
    }
});
```

**Execution**  
*Note: it should be declared at the end.*
```java
public void exec()
```

Next features (todo)
-------  
- ~~Add `with(ContentValues params)` method as an override of existing one;~~
- ~~Use a single callback for return: `success` and `error`, instead of combined `callbacks` method;~~
- ~~Use `context.getMainLooper()` in order to init and back elsewhere than activities/fragments;~~
- Add a value to des/activate logs in `init()` method;
- Modify `auth()` methods to integrate OAuth2.0, as a header's params `(String methodAuth, String tokenAuth)`;

Logs
----

######v.0.0.2:
- Callbacks separate in 'success' and 'error' (`back()` decrepeated);
- Context handler: calling from Activity, Fragment, Service or elsewhere;

######v.0.0.1:
- Basic implementation;

Contribution
-------

Developed by Fllo (@Gitdefllo) 2015.  
Feel free to contribute, customize or improve it.
