# Noveo Preferences

Android SharedPreferences wrapper for easy Read/Write/Remove operations.

Integrated with [RxJava2](https://github.com/ReactiveX/RxJava) & [java8 Stream Support](https://github.com/streamsupport/streamsupport)

## How to use

```!java
class DeveloperPreferences {
    Preference<Boolean> stethoEnabled;
    Preference<Boolean> leakCanaryEnabled;

    DeveloperPreferences(Context context) {
        final NoveoPreferences drawerPreferences = new NoveoPreferences(context, "debugdrawer");
        stethoEnabled = drawerPreferences.getBoolean("developer.key_stetho", false);
        leakCanaryEnabled = drawerPreferences.getBoolean("developer.key.leak_canary", false);
    }
}

class Presenter {    
    DeveloperPreferences prefs;
    Consumer<Optional<Boolean>> listener;
    
    Presenter(DeveloperPreferences prefs) {
        this.prefs = prefs;
        //Observe changes
        this.listener = prefs.stethoEnabled.provider().addListener(optionalValue -> {
            optionalValue.ifPresentedOrElse(
                value -> //handle value changed
                () -> //handle value removed
            );
        });
    }
    
    void loadParams() {
        //standard way
        if (prefs.stethoEnabled.read().orElse(false)) {
            //handle read (if not presented - false)
        }
        
        //or functional way
        prefs.stethoEnabled.read().ifPresented(enabled -> {
            //handle read (only if presented)
        });
    }
    
    void saveParams(boolean enabled) {
        try {
            prefs.stethoEnabled.save(enabled);
            //handle saved
        } catch (IOException exception) {
            //handle error save
        }
    }
    
    void onDestroy() {
        prefs.stethoEnabled.provider().removeListener(listener); //stop watching
    }
}
```

### ..with Rx


```!java
class DeveloperPreferences {
    RxPreference<Boolean> stethoEnabled;
    RxPreference<Boolean> leakCanaryEnabled;

    DeveloperPreferences(Context context) {
        final NoveoPreferences drawerPreferences = new NoveoPreferences(context, "debugdrawer");
        stethoEnabled = drawerPreferences.getBoolean("developer.key_stetho", false).rx();
        leakCanaryEnabled = drawerPreferences.getBoolean("developer.key.leak_canary", false).rx();
    }
}

class Presenter {    
    DeveloperPreferences prefs;
    Disposable disposable;
    
    Presenter(DeveloperPreferences prefs) {
        this.prefs = prefs;
        //Observe changes
        this.disposable = prefs.stethoEnabled.provider().observe(optionalValue -> {
            optionalValue.ifPresentedOrElse(
                value -> //handle value
                () -> //handle no value
            );
        });
    }
    
    void loadParams() {
        prefs.stethoEnabled.read().map(Optional::get).subscribe(
            value -> //handle,
            error -> //handle error
        );
    }
    
    void saveParams(boolean enabled) {
        prefs.stethoEnabled.save(enabled).subscribe(
            () -> //handle saved,
            error -> //handle error save
        );
    }
    
    void onDestroy() {
        disposable.dispose(); //stop watching
    }
}
```

## How to add

Add these dependencies in your build.gradle:

```!groovy
dependencies {
    implementation 'io.reactivex.rxjava2:rxjava:2.1.1'
    implementation 'net.sourceforge.streamsupport:streamsupport:1.5.6'
    implementation 'com.noveogroup:preferences:0.0.1'
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