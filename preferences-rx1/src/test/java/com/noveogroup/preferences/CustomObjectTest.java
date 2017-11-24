package com.noveogroup.preferences;

import com.noveogroup.preferences.api.RxPreference;
import com.noveogroup.preferences.mock.TestSharedPreferences;
import com.noveogroup.preferences.mock.User;
import com.noveogroup.preferences.mock.UserStrategy;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("PMD.AvoidFinalLocalVariable")
public class CustomObjectTest {

    private static final String KEY_USER = "user";
    private static final User DEFAULT_USER_VALUE = new User("default", 100);

    private NoveoRxPreferences noveoPreferencesWrapper;
    private TestSharedPreferences preferences;

    @Before
    public void before() {
        NoveoPreferences.setDebug(true);
        preferences = new TestSharedPreferences();
        noveoPreferencesWrapper = new NoveoRxPreferences(preferences);
    }

    @Test
    public void object() {
        assertEquals("initial set is empty", preferences.getAll().size(), 0);

        final RxPreference<User> userEntity = noveoPreferencesWrapper.getObject(KEY_USER, new UserStrategy(), DEFAULT_USER_VALUE);
        assertEquals("no user - got default value", userEntity.toBlocking().read().get(), DEFAULT_USER_VALUE);
        assertEquals("after 1 get there are still no records in preferences", preferences.getAll().size(), 0);

        final User newUser = new User("new", -100);
        userEntity.save(newUser).subscribe();
        assertEquals("saved user is the same as get after", userEntity.toBlocking().read().get(), newUser);
        assertEquals("1 user stores 3 keys (according to custom strategy)", preferences.getAll().size(), 3);

        userEntity.remove().subscribe();
        assertEquals("after remove user returns default value", userEntity.toBlocking().read().get(), DEFAULT_USER_VALUE);
        assertEquals("after remove preferences contains 0 records", preferences.getAll().size(), 0);
    }
}
