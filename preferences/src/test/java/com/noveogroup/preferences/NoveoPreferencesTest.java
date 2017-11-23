package com.noveogroup.preferences;

import android.content.Context;

import com.noveogroup.preferences.mock.TestSharedPreferences;
import com.noveogroup.preferences.param.Constants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@SuppressWarnings("PMD.AvoidFinalLocalVariable")
public class NoveoPreferencesTest {
    private TestSharedPreferences preferences;
    private Context context;

    @Before
    public void before() {
        preferences = new TestSharedPreferences();
        context = Mockito.mock(Context.class);
        Mockito.when(context.getPackageName()).thenReturn("com.noveogroup.preferences");
        Mockito.when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(preferences);
    }

    @Test
    public void constructor() {
        final NoveoPreferences contextPrefs = new NoveoPreferences(context);
        final NoveoPreferences contextFilePrefs = new NoveoPreferences(context, "");
        final NoveoPreferences preferencePrefs = new NoveoPreferences(preferences);

        preferences.edit().putString(Constants.KEY_STRING, Constants.VALUE_STRING).commit();

        assertTrue("NoveoPreferences uses preferences",
                contextPrefs.getString(Constants.KEY_STRING, "").read().isPresent());
        assertTrue("NoveoPreferences uses preferences",
                contextFilePrefs.getString(Constants.KEY_STRING).read().isPresent());
        assertTrue("NoveoPreferences uses preferences",
                preferencePrefs.getString(Constants.KEY_STRING).read().isPresent());
    }
}
