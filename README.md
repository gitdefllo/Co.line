Co.line
=======

**REST connection lib for Android**

Co.line is a custom library to do a HttpURLConnection in REST. It creates automatically a new Thread and returns result in a callback on the Main Thread. You can specify header's properties (@see `head()`), add params in the body request (@see `with()`) and even interrupt the background Thread (@see `cancel()`).

Usage
------

**A simple request:**

```java
Coline.init(this).url(HttpMethod.GET, "http://api.url.com/")
        .res(new Collback() {
            @Override
            public void onResult(Response res, Error err) {
                if (err != null) {
                    // handle error
                    return;
                }

                // handle successful response
                Log.i("Coline", "Status: " + res.status+" - Body: " + res.body);
            }
        }).exec();
```

**With custom model object:**

```java
Coline.init(this).url(HttpMethod.GET, "http://api.url.com/user")
        .res(new ObjCollback<UserModel>() {
            @Override
            public void onResult(UserModel user, Error err) {
                if (err != null) {
                    // handle error
                    return;
                }

                // handle user object
                Log.i("Coline", "User: " + user.name);
            }
        }).exec();
```

**Multiple request (queue):**

Set multiple requests in queue and launch all at one time:

```java
Coline.init(this).url(HttpMethod.GET, "http://api.url.com/user")
        .res(userResponse)
        .queue();
...
Coline.init(this).url(HttpMethod.GET, "http://api.url.com/user/messages")
        .res(msgResponse)
        .queue();
...
Coline.init(this).send();
```

Download
--------

Via gradle
```java
compile 'com.fllo.co.line:co.line:2.2.0'
```
or maven
```xml
<dependency>
  <groupId>com.fllo.co.line</groupId>
  <artifactId>co.line</artifactId>
  <version>2.2.0</version>
</dependency>
```

Documentation
--------

See the [docs](https://github.com/Gitdefllo/Co.line/blob/master/DOCS.md) for more information.


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
