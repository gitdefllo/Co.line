Co.line Docs
=======

Full documentation
------

**Safe usage**

By making a global Coline variable, you can stop in safe mode the background treatment:
```java

Coline coline;
...
coline = Coline.init(this);
coline.url(HttpMethod.GET, mUrl)
             .res(mResult)
             .exec();
...

@Override
public void onDestroy() {
    if (coline != null) {
        coline.cancel();
        coline = null;
    }
    super.onDestroy();
}
```

**Initialization**

```java
public static Coline init(Context context)
```
Initialize with the current Context.  
```java
coline.init(MainActivity.this);
```

**Request methods**

```java
public Coline url(HttpMethod method, String url)
```
Pass the HTTP method and the URL.
```java
coline.url(HttpMethod.POST, "http://api.url.com/user");
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
coline.head(params);
```
It's possible to use an `ArrayMap<String, Object>` **(only API 19 and higher)**.  
If non-set, the default header properties are `content-type=application/x-www-form-urlencoded;charset=UTF-8`

**Body**

```java
public Coline with(ContentValues values)
```
Pass the body parameters, generally keys/values pairs, to the server.  
```java
ContentValues values = new ContentValues();
values.put("username", "Fllo");
values.put("github",   "Gitdefllo");
// Values to send in body request
coline.with(values);
```
It's possible to use an `ArrayMap<String, Object>` **(only API 19 and higher)**.

**Callbacks**

Two callbacks are available:

**Collback** which retrieve two Coline objects:
```java
public Coline res(Collback collback)
```
Called by `res()`, the request response is a couple `Response` / `Error` objects:
```java
coline.res(new Collback() {
    @Override
    public void onResult(Response res, Error err) { }
});
```

Or **ObjCollback** which can handle any custom object, converting by [Gson](https://github.com/google/gson) library:
```java
public Coline res(Collback<T> collback)
```
Called by `res()`, the request response is a couple `T` / `Error` objects:
```java
coline.res(new ObjCollback<CustomModel>() {
    @Override
    public void onResult(CustomModel model, Error err) { }
});
```

**Coline Objects**

*All requests from below 200 to above 299 are considered as an `Error`.*

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
