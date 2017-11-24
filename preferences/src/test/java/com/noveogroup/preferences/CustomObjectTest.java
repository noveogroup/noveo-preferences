package com.noveogroup.preferences;

import android.content.SharedPreferences;

import com.noveogroup.preferences.api.Preference;
import com.noveogroup.preferences.mock.TestSharedPreferences;
import com.noveogroup.preferences.mock.User;
import com.noveogroup.preferences.mock.UserStrategy;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("PMD.AvoidFinalLocalVariable")
public class CustomObjectTest {

    private static final String KEY_USER = "user";
    private static final User DEFAULT_USER_VALUE = new User("default", 100);

    private NoveoPreferences noveoPreferencesWrapper;
    private TestSharedPreferences preferences;

    @Before
    public void before() {
        NoveoPreferences.setDebug(true);
        preferences = new TestSharedPreferences();
        noveoPreferencesWrapper = new NoveoPreferences(preferences);
    }

    @Test
    public void object() {
        assertEquals("initial set is empty", preferences.getAll().size(), 0);

        final Preference<User> userEntity = noveoPreferencesWrapper.getObject(KEY_USER, new UserStrategy(), DEFAULT_USER_VALUE);
        assertEquals("no user - got default value", userEntity.read().get(), DEFAULT_USER_VALUE);
        assertEquals("after 1 get there are still no records in preferences", preferences.getAll().size(), 0);

        final User newUser = new User("new", -100);
        userEntity.rx().save(newUser).subscribe();
        assertEquals("saved user is the same as get after", userEntity.read().get(), newUser);
        assertEquals("1 user stores 3 keys (according to custom strategy)", preferences.getAll().size(), 3);

        userEntity.rx().remove().subscribe();
        assertEquals("after remove user returns default value", userEntity.read().get(), DEFAULT_USER_VALUE);
        assertEquals("after remove preferences contains 0 records", preferences.getAll().size(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void strategyRead() {
        final PreferenceStrategy<Object> emptyStrategy = new PreferenceStrategy.Builder<>()
                .keyFilter(PreferenceStrategy.KeyFilter.EQUALS)
                .canBeNull(true)
                .build();
        final Preference<Object> emptyPreference = noveoPreferencesWrapper.getObject("empty", emptyStrategy, new CustomObjectTest());

        emptyPreference.read();
    }

    @Test(expected = IllegalArgumentException.class)
    public void strategySave() throws IOException {
        final PreferenceStrategy<Object> emptyStrategy = PreferenceStrategy.builder().canBeNull(true).build();
        final Preference<Object> emptyPreference = noveoPreferencesWrapper.getObject("empty", emptyStrategy);

        emptyPreference.save(new CustomObjectTest());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void customStrategy() throws IOException {
        final PreferenceStrategy.GetAction<String> getAction = SharedPreferences::getString;
        final PreferenceStrategy.SetAction<String> setAction = SharedPreferences.Editor::putString;
        final PreferenceStrategy.RemoveAction removeAction = (editor, key) -> {
            throw new UnsupportedOperationException("test");
        };
        PreferenceStrategy<String> customStrategy = PreferenceStrategy.<String>builder()
                .getAction(getAction)
                .setAction(setAction)
                .removeAction(removeAction)
                .canBeNull(true)
                .build();

        noveoPreferencesWrapper.getObject("key", customStrategy).remove();
    }

}
