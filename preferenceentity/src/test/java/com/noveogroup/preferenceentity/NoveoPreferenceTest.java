package com.noveogroup.preferenceentity;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static com.noveogroup.preferenceentity.Constants.KEY_STRING;
import static com.noveogroup.preferenceentity.Constants.VALUE_STRING;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

public class NoveoPreferenceTest {
    private TestSharedPreferences preferences;
    private Context context;

    @Before
    public void before() {
        preferences = new TestSharedPreferences();
        context = Mockito.mock(Context.class);
        Mockito.when(context.getPackageName()).thenReturn("com.noveogroup.preferenceentity");
        Mockito.when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(preferences);
    }

    @Test
    public void constructor() {
        final NoveoPreferences contextPrefs = new NoveoPreferences(context);
        final NoveoPreferences contextFilePrefs = new NoveoPreferences(context, "");
        final NoveoPreferences preferencePrefs = new NoveoPreferences(preferences);

        preferences.edit().putString(KEY_STRING, VALUE_STRING).commit();

        assertTrue(contextPrefs.getString(KEY_STRING, "").read().isPresent());
        assertTrue(contextFilePrefs.getString(KEY_STRING).read().isPresent());
        assertTrue(preferencePrefs.getString(KEY_STRING).read().isPresent());
    }
}
