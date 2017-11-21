package com.noveogroup.preferences.sample.ui;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.noveogroup.preferences.sample.SampleApplication;
import com.noveogroup.preferences.sample.data.CommonPreferences;
import com.noveogroup.preferences.sample.data.User;

import io.reactivex.disposables.CompositeDisposable;
import java8.util.Optional;

@SuppressWarnings("NumberEquality")
@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private final CommonPreferences preferences = SampleApplication.COMMON_PREFERENCES;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private Optional<User> user = Optional.empty();

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        disposable.addAll(
                //observe changes
                preferences.enabled.rx().provider().observe(this::handleEnabledChanges),
                preferences.user.rx().provider().asFlowable().subscribe(this::handleUserChanges),

                //initial values
                preferences.enabled.rx().read().subscribe(this::handleEnabledChanges),
                preferences.user.rx().read().subscribe(this::handleUserChanges)
        );
    }

    private void handleEnabledChanges(final Optional<Boolean> optionalEnabled) {
        getViewState().switchEnabled(optionalEnabled.orElse(false));
    }

    private void handleUserChanges(final Optional<User> userOptional) {
        user = userOptional;
        userOptional.map(User::toString).ifPresentOrElse(
                getViewState()::showLabel,
                getViewState()::hideLabel);
        userOptional.ifPresentOrElse(
                getViewState()::showUser,
                getViewState()::hideUser);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    void saveUser(final String name) {
        user.or(() -> Optional.of(new User(name, User.NOT_AN_AGE)))
                .ifPresent(user -> disposable.add(preferences.user.rx().save(new User(name, user.getAge())).subscribe()));
    }

    void saveUser(final long age) {
        user.or(() -> Optional.of(new User("", age)))
                .ifPresent(user -> disposable.add(preferences.user.rx().save(new User(user.getName(), age)).subscribe()));
    }

    void saveEnabled(final boolean enabled) {
        disposable.add(preferences.enabled.rx().save(enabled).subscribe());
    }

    void clearAll() {
        disposable.add(preferences.all.rx().remove().subscribe());
    }

}
