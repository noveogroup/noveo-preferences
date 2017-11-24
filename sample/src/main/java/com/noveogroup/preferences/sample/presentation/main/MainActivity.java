package com.noveogroup.preferences.sample.presentation.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.noveogroup.preferences.sample.R;
import com.noveogroup.preferences.sample.data.User;
import com.noveogroup.preferences.sample.presentation.support.SupportWatcher;

/**
 * Created by avaytsekhovskiy on 23/11/2017.
 */
public class MainActivity extends MvpAppCompatActivity implements MainView {

    @InjectPresenter(type = PresenterType.LOCAL)
    MainPresenter mainPresenter;
    private final CompoundButton.OnCheckedChangeListener onCheckedChangeListener = (buttonView, isChecked) -> mainPresenter.saveEnabled(isChecked);
    private final TextWatcher userNameWatcher = new SupportWatcher() {
        @Override
        public void afterTextChanged(final Editable text) {
            mainPresenter.saveUser(String.valueOf(text));
        }
    };
    private final TextWatcher userAgeWatcher = new SupportWatcher() {
        @Override
        public void afterTextChanged(final Editable text) {
            mainPresenter.saveUser(parseAge(text));
        }
    };
    private EditText userText;
    private EditText userAge;
    private CheckBox enabledBox;
    private TextView userLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userText = findViewById(R.id.user_name);
        userAge = findViewById(R.id.user_age);
        enabledBox = findViewById(R.id.enabled);
        userLabel = findViewById(R.id.user_label);

        installListeners();
    }

    @Override
    public void showUser(@NonNull final User user) {
        changeUserSafely(user.getName(), String.valueOf(user.getAge() == User.NOT_AN_AGE ? "" : user.getAge()));
    }

    @Override
    public void hideUser() {
        changeUserSafely("", "");
    }

    @Override
    public void switchEnabled(final boolean enabled) {
        userAge.setEnabled(enabled);
        userText.setEnabled(enabled);

        enabledBox.setOnCheckedChangeListener(null);
        enabledBox.setChecked(enabled);
        enabledBox.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private void installListeners() {
        userText.addTextChangedListener(userNameWatcher);
        userAge.addTextChangedListener(userAgeWatcher);
        enabledBox.setOnCheckedChangeListener(onCheckedChangeListener);

        findViewById(R.id.clear).setOnClickListener(view -> mainPresenter.clearAll());
    }

    private Long parseAge(final CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return User.NOT_AN_AGE;
        }

        try {
            return Long.parseLong(text.toString(), 10);
        } catch (NumberFormatException e) {
            return User.NOT_AN_AGE;
        }
    }

    @Override
    public void showLabel(@NonNull final String message) {
        userLabel.setText(message);
    }

    @Override
    public void hideLabel() {
        userLabel.setText("");
    }

    private void changeUserSafely(final String name, final String age) {
        if (!TextUtils.equals(userText.getText(), name)) {
            userText.removeTextChangedListener(userNameWatcher);
            userText.setText(name);
            userText.setSelection(userText.getText().length());
            userText.addTextChangedListener(userNameWatcher);
        }

        if (!TextUtils.equals(userAge.getText(), age)) {
            userAge.removeTextChangedListener(userAgeWatcher);
            userAge.setText(age);
            userAge.addTextChangedListener(userAgeWatcher);
        }
    }

}
