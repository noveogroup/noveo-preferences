package com.noveogroup.preferences.sample.presentation.main;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.noveogroup.preferences.api.Preference;
import com.noveogroup.preferences.guava.Optional;
import com.noveogroup.preferences.rx.api.RxPreference;
import com.noveogroup.preferences.sample.data.User;
import com.noveogroup.preferences.sample.presentation.SampleApplication;

import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
@SuppressWarnings({"NumberEquality", "unused"})
@InjectViewState
@Slf4j
public class MainPresenter extends MvpPresenter<MainView> {
    private final CompositeDisposable disposable = new CompositeDisposable();

    private final Preference<User> userPref;
    private final Preference<Boolean> enabledPref;

    private final RxPreference<User> userRxPref;
    private final RxPreference<Boolean> enabledRxPref;
    private final RxPreference<Map<String, ?>> allRxPrefs;

    private Optional<User> user = Optional.absent();

    @SuppressWarnings("WeakerAccess")
    MainPresenter() {
        super();
        userPref = SampleApplication.commonPreferences.user.toBlocking();
        enabledPref = SampleApplication.commonPreferences.enabled.toBlocking();

        userRxPref = SampleApplication.commonPreferences.user;
        enabledRxPref = SampleApplication.commonPreferences.enabled;
        allRxPrefs = SampleApplication.commonPreferences.all;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        //load current values (synchronous style)
        handleEnabledChanges(enabledPref.read());
        handleUserChanges(userPref.read());

        //observe future changes (rx style)
        disposable.addAll(
                enabledRxPref.provider().observe(this::handleEnabledChanges),
                userRxPref.provider().observe(this::handleUserChanges)
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear(); //stop watching changes
    }

    void saveUser(final String name) {
        final Long currentAge = user.transform(User::getAge).or(User.NOT_AN_AGE);
        userRxPref.save(new User(name, currentAge)).subscribe(
                () -> log.debug("user name changed to {}", name));
    }

    void saveUser(final long age) {
        final String currentName = user.transform(User::getName).or("");
        userRxPref.save(new User(currentName, age)).subscribe(
                () -> log.debug("user age changed to {}", age));
    }

    void saveEnabled(final boolean enabled) {
        enabledRxPref.save(enabled).subscribe(
                () -> log.debug("editing {}", enabled ? "enabled" : "disabled"));
    }

    void clearAll() {
        allRxPrefs.remove().subscribe(
                () -> log.debug("preferences cleared"));
    }

    private void handleEnabledChanges(final Optional<Boolean> optionalEnabled) {
        getViewState().switchEnabled(optionalEnabled.or(false));
    }

    private void handleUserChanges(final Optional<User> userOptional) {
        user = userOptional;
        userOptional
                .applyPresent(getViewState()::showUser)
                .applyAbsent(getViewState()::hideUser)
                .transform(User::toString)
                .apply(getViewState()::showLabel, getViewState()::hideLabel);
    }

}
