# Noveo Preferences

Android SharedPreferences wrapper for easy Read/Write/Remove operations.

+ [RxJava1 / RxJava2](https://github.com/ReactiveX/RxJava) integration.

## How to use

#### Create your Preferences API

With `NoveoRxPreferences`

```java
class RxPreferences {
    RxPreference<Boolean> developerModeRxPref;

    RxPreferences(Context context) {
        NoveoRxPreferences noveoPreferences = new NoveoRxPreferences(context, "developers");
        
        this.developerModeRxPref = noveoRxPreferences.getBoolean("developerMode");
    }
}
```

Or use synchronous `NoveoPreferences`

```java
class SynchronousPreferences {
    Preference<Boolean> developerModePref;

    SynchronousPreferences(Context context) {
        NoveoPreferences noveoPreferences = new NoveoPreferences(context, "developers");
        
        this.developerModePref = noveoPreferences.getBoolean("developerMode");
    }
}
```

#### Use RxPreferences

Operations: `read()`, `save(T value)`, `remove()` 

```java
Single<Optional<Boolean>> single = developerModeRxPref.read();
Completable completable = developerModeRxPref.save(true);
Completable completable = developerModeRxPref.remove();
```

and Observe changes

```java
Disposable disposable = developerModeRxPref.provider().observe(
    value -> //handle onNext
);

//start watching
Floable<Optional<Boolean>> flowable = developerModeRxPref.provider().asFlowable();

//stop watching
disposable.dispose(); 
```

#### Or use synchronously API

```java
boolean value = developerModePref.read().or(false);
developerModePref.save(true);
developerModePref.remove();
```

```java
//start watching
listener = developerModePref.provider().addListener(optional -> optional.apply(
    value -> //handle value changed
    () -> //handle value removed
));

//stop watching
developerModePref.provider().removeListener(listener);
```

### Bridge between Rx & Synchronous versions

Generally you should use rx preferences. If you need to use them as sync - use toBlocking().

```java
/* to sync */ Preference developerModePref = developerModeRxPref.toBlocking();
/* ro  rx  */ RxPreference developerModeRxPref = RxPreference.wrap(developerModePref);

/* to sync */ NoveoPreferences preferences = rxPreferences.toBlocking();
/* ro  rx  */ NoveoRxPreferences rxPreferences = new NoveoRxPreferences(preferences);
```

## How to add

Add maven repository to your project (jCenter/mavenCentral publication coming soon.)

```groovy
repositories {
    maven {
        url "http://dl.bintray.com/noveo-nsk/maven"
    }
}
```

#### Dependencies

| Description | Dependencies |
| :--- | :--- |
| Synchronous API | `implementation 'com.noveogroup:preferences:0.0.2'` | 
| RxJava1 Extension | `implementation 'com.noveogroup:preferences:0.0.2'`<br>`implementation 'com.noveogroup:preferences-rx1:0.0.2`<br>`implementation 'io.reactivex:rxjava:1.3.6'` | 
| RxJava2 Extension | `implementation 'com.noveogroup:preferences:0.0.2'`<br>`implementation 'com.noveogroup:preferences-rx2:0.0.2`<br>`implementation 'io.reactivex.rxjava2:rxjava:2.1.1'` | 

## Guava Optional Extensions

Project uses `Optional` class extracted from popular [Guava](https://github.com/google/guava) library, extended with more functional methods. 

+ [`Optional` Official Guava Documentation](https://github.com/google/guava/wiki/UsingAndAvoidingNullExplained#optional)

#### New Optional API:

+ `Optional.applyPresented(Consumer presented)` - invoke lambda if value presented.
+ `Optional.applyAbsent(Runnable absent)` - invoke lambda if value absent.
+ `Optional.apply(Consumer presented, Runnable absent)` - invoke `presented` if value presented, invoke `absent` otherwise. 

## License

```text
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