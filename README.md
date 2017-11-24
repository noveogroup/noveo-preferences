# Noveo Preferences

Android SharedPreferences wrapper for easy Read/Write/Remove operations.

Integrated with [RxJava2](https://github.com/ReactiveX/RxJava) & [java8 Stream Support](https://github.com/streamsupport/streamsupport)

## How to use

Create your Preferences API

```java
class DeveloperPreferences {
    Preference<Boolean> stethoPref;
    RxPreference<Boolean> stethoRxPref;

    DeveloperPreferences(Context context) {
        NoveoPreferences drawerPreferences = new NoveoPreferences(context, "developers");
        
        stethoPref = drawerPreferences.getBoolean("developer.key_stetho");
        stethoRxPref = stethoPref.rx();
    }
}
```

Use it synchronously

```java
void readSaveRemove() throws IOException {
    boolean value = stethoPref.read().orElse(false);
    stethoPref.save(true);
    stethoPref.remove();
}
```

Observe changes with callback listener

```java
void observe() {
    listener = canaryPref.provider().addListener(optionalValue -> optionalValue.ifPresentedOrElse(
        value -> //handle value changed
        () -> //handle value removed
    ));
    
    //stop watching
    canaryPref.provider().removeListener(listener);
}

```

### ..with Rx

Or use rx asynchronous API. 

```java
void read() {
    Single<Optional<Boolean>> single = stethoRxPref.read();
    Completable completable = stethoRxPref.save(true);
    Completable completable = stethoRxPref.remove();
}
```

Observe changes

```java
void observe() {
    Disposable disposable = stethoRxPref.provider().observe(
        value -> //handle onNext
    );
    
    Floable<Optional<Boolean>> flowable = stethoRxPref.provider().asFlowable();
    
    disposable.dispose(); //stop watching
}
```

## How to add

Add these dependencies to your build.gradle:

```!groovy
repositories {
    maven {
        url "http://dl.bintray.com/noveo-nsk/maven"
    }
}

dependencies {
    implementation 'com.noveogroup:preferences:0.0.1'
    implementation 'io.reactivex.rxjava2:rxjava:2.1.1'
    implementation 'net.sourceforge.streamsupport:streamsupport:1.5.6'
}
```

jCenter/mavenCentral publication coming soon.

## License

```!text
Copyright (c) 2017 Noveo Group

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

Except as contained in this notice, the name(s) of the above copyright holders
shall not be used in advertising or otherwise to promote the sale, use or
other dealings in this Software without prior written authorization.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```