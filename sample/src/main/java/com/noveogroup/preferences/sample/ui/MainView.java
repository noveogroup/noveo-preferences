package com.noveogroup.preferences.sample.ui;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.noveogroup.preferences.sample.data.User;

public interface MainView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showUser(@NonNull User user);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideUser();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void switchEnabled(boolean enabled);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showLabel(@NonNull String message);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideLabel();
}
