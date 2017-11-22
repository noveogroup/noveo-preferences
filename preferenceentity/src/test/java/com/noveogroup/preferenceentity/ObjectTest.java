package com.noveogroup.preferenceentity;

import android.content.SharedPreferences;

import com.noveogroup.preferenceentity.api.PreferenceEntity;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ObjectTest {

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
        assertEquals(preferences.getAll().size(), 0);

        final PreferenceEntity<User> userEntity = noveoPreferencesWrapper.getObject(KEY_USER, new UserStrategy(), DEFAULT_USER_VALUE);
        assertEquals(userEntity.read().get(), DEFAULT_USER_VALUE);
        assertEquals(preferences.getAll().size(), 0);

        final User newUser = new User("new", -100);
        userEntity.rx().save(newUser).subscribe();
        assertEquals(userEntity.read().get(), newUser);
        assertEquals(preferences.getAll().size(), 3);

        userEntity.rx().remove().subscribe();
        assertEquals(userEntity.read().get(), DEFAULT_USER_VALUE);
        assertEquals(preferences.getAll().size(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void strategyRead() {
        final PreferenceStrategy<Object> emptyStrategy = new PreferenceStrategy.Builder<>()
                .keyFilter(PreferenceStrategy.KeyFilter.EQUALS)
                .canBeNull(true)
                .build();
        final PreferenceEntity<Object> emptyPreference = noveoPreferencesWrapper.getObject("empty", emptyStrategy, new ObjectTest());

        emptyPreference.read();
    }

    @Test(expected = IllegalArgumentException.class)
    public void strategySave() throws IOException {
        final PreferenceStrategy<Object> emptyStrategy = PreferenceStrategy.builder().build();
        final PreferenceEntity<Object> emptyPreference = noveoPreferencesWrapper.getObject("empty", emptyStrategy);

        emptyPreference.save(new ObjectTest());
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
