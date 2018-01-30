# Noveo Preferences

[ ![Preferences-rx2](https://api.bintray.com/packages/noveo-nsk/maven/preferences-rx2/images/download.svg) ](https://bintray.com/noveo-nsk/maven/preferences-rx2/_latestVersion)

Android SharedPreferences wrapper for easy Read/Write/Remove operations.

+ [RxJava1 / RxJava2](https://github.com/ReactiveX/RxJava) integration.

## How to use

Create `SharedPreferences` wrapper

```java
NoveoRxPreferences rxPreferences = new NoveoRxPreferences(context, "developers");
```

Create your Preferences Objects

```java
RxPreference<Boolean> boolPref = rxPreferences.getBoolean("bool pref");
RxPreference<String> stringPref = rxPreferences.getString("string pref", "default");
```

> Create them once and store as strong references in @Singleton scoped object.

#### `RxPreferences` API

```java
Single<Optional<Boolean>> single = boolPref.read();
Completable completable = boolPref.save(true);
Completable completable = boolPref.remove();
```

> Optional.absent() will be returned in case of empty preference. Your preference can be null if `PreferenceStrategy` allows such behavior.

#### `RxPreferenceProvider` API

```java
Disposable disposable = boolPref.provider().observe(value -> //handle onNext); //observe changes
disposable.dispose(); //stop observing

Floable<Optional<Boolean>> flowable = boolPref.provider().asFlowable(); //react & combine in RxChain
```

#### If you don't want Rx. 

```java
// 1. Create
NoveoPreferences preferences = new NoveoPreferences(context, "developers");
Preference<Boolean> boolPref = preferences.getBoolean("bool pref");

// 2. Edit
Optional<Boolean> optionalValue = boolPref.read();
/* or */  boolean value         = boolPref.read().or(false);
boolPref.save(true);
boolPref.remove();

// 3. Observe changes
listener = boolPref.provider().addListener(optional -> optional.apply(
    value -> //handle value changed
    () -> //handle value removed
));
boolPref.provider().removeListener(listener); //stop observing
```

> Keep same Preference object to remove listener. Avoid duplicated instances.

### Bridge between Rx & Regular versions

Generally you should use rx preferences. If you need to use them as sync - use toBlocking().

```java
/* to sync */ Preference syncBoolPref = boolPref.toBlocking();
/* ro  rx  */ RxPreference rxBoolPref = RxPreference.wrap(syncBoolPref);

/* to sync */ NoveoPreferences preferences = rxPreferences.toBlocking();
/* ro  rx  */ NoveoRxPreferences rxPreferences = new NoveoRxPreferences(preferences);
```

## How to add

| Description | Dependencies |
| :--- | :--- |
| Core API | `implementation 'com.noveogroup:preferences:0.0.3'` | 
| RxJava1 Extension | `implementation 'com.noveogroup:preferences-rx1:0.0.3`<br>`implementation 'io.reactivex:rxjava:1.3.6'` | 
| RxJava2 Extension | `implementation 'com.noveogroup:preferences-rx2:0.0.3`<br>`implementation 'io.reactivex.rxjava2:rxjava:2.1.1'` | 

> Extensions includes Core API transitively. You don't need to include both

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