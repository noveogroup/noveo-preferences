# Noveo Preferences

Android SharedPreferences wrapper for easy Read/Write/Remove operations.

Integrated with [RxJava2](https://github.com/ReactiveX/RxJava) & [java8 Stream Support](https://github.com/streamsupport/streamsupport)

## How to use

Create your Preferences API

```java
class DeveloperPreferences {
    
    Preference<Boolean> stethoEnabled;
    Preference<Boolean> leakCanaryEnabled;

    DeveloperPreferences(Context context) {
        NoveoPreferences drawerPreferences = new NoveoPreferences(context, "developers");
        
        stethoEnabled = drawerPreferences.getBoolean("developer.key_stetho");
        leakCanaryEnabled = drawerPreferences.getBoolean("developer.key.leak_canary");
    }
}
```

Use it synchronously

```java
void readSaveRemove() throws IOException {
    boolean result = getPreferences().stethoEnabled.read().orElse(false)
    getPreferences().stethoEnabled.save(true);
    getPreferences().stethoEnabled.remove();
}
```

Observe changes with callback listener

```java
void observe() {
    listener = getPreferences().stethoEnabled.provider().addListener(optionalValue -> {
        optionalValue.ifPresentedOrElse(
            value -> //handle value changed
            () -> //handle value removed
        );
    });
    
    //stop watching
    getPreferences().stethoEnabled.provider().removeListener(listener);
}

```

### ..with Rx

Or use rx asynchronous API. 

```java
void read() {
    getPreferences().stethoEnabled.rx().read().subscribe(       //Single   
            optionalValue -> // handle
            error ->         // unlikely, but..
    );     
    getPreferences().stethoEnabled.rx().save(true).subscribe(
            () ->            //saved
            error ->         //unlikely, but..
    ); //Completable
    getPreferences().stethoEnabled.rx().remove().subscribe(
            () ->            //removed
            error ->         //unlikely, but..
    );   //Completable
}
```

Observe changes

```java
void observe() {
    disposable = getPreferences().stethoEnabled.rx().provider().observe(optionalValue -> {
        optionalValue.ifPresentedOrElse(
            value -> //handle value changed
            () -> //handle value removed
        );
    });
    
    getPreferences().stethoEnabled.rx().provider().asFlowable().subscribe(
            value -> //handle value changed
    );
    
    //stop watching
    disposable.dispose();
}

```

## How to add

Add these dependencies to your build.gradle:

```!groovy
dependencies {
    implementation 'com.noveogroup:preferences:0.0.1'
    implementation 'io.reactivex.rxjava2:rxjava:2.1.1'
    implementation 'net.sourceforge.streamsupport:streamsupport:1.5.6'
}
```

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