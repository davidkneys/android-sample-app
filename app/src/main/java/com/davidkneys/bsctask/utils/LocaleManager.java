package com.davidkneys.bsctask.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;

/**
 * Locale manager with util methods to change and load proper Application set locale.
 */
public class LocaleManager {

    public static final String LANG_CS = "cs";
    public static final String LANG_EN = "en";

    private static final String PREF_LANGUAGE_KEY = "prefLanguageKey";

    /**
     * Used in {@link android.app.Activity#attachBaseContext(Context)} for rewriting default
     * behavior and return proper Language resources based on Application set Locale.
     */
    public static ContextWrapper wrap(Context context) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(getPreferredLanguage(context)));
        return new ContextWrapper(context.createConfigurationContext(configuration));
    }

    /**
     * Helper method for changing language in run time. Note that Activity has to be properly
     * calling {@link android.app.Activity#attachBaseContext(Context)}
     * with {@link LocaleManager#wrap(Context)} method.
     */
    public static void changeLanguage(Activity activity, String languageCode) {
        persistLanguage(activity, languageCode);
        activity.recreate();
    }

    private static void persistLanguage(Context context, String language) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        //has to be commit(), asynchronous apply() is not enough, because we are usually immediately
        //recreating activity
        prefs.edit().putString(PREF_LANGUAGE_KEY, language).commit();
    }

    private static String getPreferredLanguage(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return prefs.getString(PREF_LANGUAGE_KEY, Locale.getDefault().getLanguage());
    }
}
