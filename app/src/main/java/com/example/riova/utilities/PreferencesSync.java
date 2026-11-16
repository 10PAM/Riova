/*
Programmer (Team Novelty) {
    - Fulya Bilgin, CWU Student ID: 50593580
}
File: PreferencesSync.java
About: Syncs the user's settings (preferences) between local storage and the
       cloud. When the user is signed in, the User Settings values are saved
       to the user's document in Firestore under a "preferences" map, and can
       be pulled back on another device or after signing in again. When the
       user is a guest (not signed in) nothing is sent to the cloud and the
       app keeps using the local SharedPreferences copy only.

       This matches the design: USER_UNIQUE_ID -> PREFERENCES -> {theme,
       font size, volume, reminders, username}. The settings UI keeps reading
       and writing the same local SharedPreferences keys, so this class can be
       added without changing how the controls work.
*/

package com.example.riova.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class PreferencesSync {

    // Firestore field that holds the preferences map on the user's document.
    private static final String FIELD_PREFERENCES = "preferences";

    // Local SharedPreferences keys (match the keys used by SettingsFragment).
    private static final String KEY_USERNAME = "username";
    private static final String KEY_THEME_DARK = "theme_dark";
    private static final String KEY_FONT_SIZE = "font_size";
    private static final String KEY_VOLUME = "master_volume";
    private static final String KEY_REMINDERS = "reminders_enabled";
    private static final String KEY_KEEP_LOGGED_IN = "keep_logged_in";

    private final SharedPreferences prefs;
    private final FirebaseFirestore firestore;

    /**
     * @param context any valid Context (application context is used internally).
     */
    public PreferencesSync(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        firestore = FirebaseFirestore.getInstance();
    }

    /**
     * @return true if a user is currently signed in with a known user id.
     */
    private boolean isSignedIn() {
        return prefs.getBoolean(Constants.KEY_IS_SIGNED_IN, false)
                && prefs.getString(Constants.KEY_USER_ID, null) != null;
    }

    /**
     * Saves the current local settings to the signed-in user's Firestore
     * document. Does nothing for a guest. Uses a merge so other fields on the
     * user document (name, email, etc.) are left untouched.
     */
    public void push() {
        if (!isSignedIn()) {
            return;
        }
        String userId = prefs.getString(Constants.KEY_USER_ID, null);

        Map<String, Object> preferences = new HashMap<>();
        preferences.put(KEY_USERNAME, prefs.getString(KEY_USERNAME, ""));
        preferences.put(KEY_THEME_DARK, prefs.getBoolean(KEY_THEME_DARK, false));
        preferences.put(KEY_FONT_SIZE, prefs.getInt(KEY_FONT_SIZE, 1));
        preferences.put(KEY_VOLUME, prefs.getInt(KEY_VOLUME, 50));
        preferences.put(KEY_REMINDERS, prefs.getBoolean(KEY_REMINDERS, false));
        preferences.put(KEY_KEEP_LOGGED_IN, prefs.getBoolean(KEY_KEEP_LOGGED_IN, false));

        Map<String, Object> document = new HashMap<>();
        document.put(FIELD_PREFERENCES, preferences);

        firestore.collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)
                .set(document, SetOptions.merge());
    }

    /**
     * Loads the signed-in user's saved preferences from Firestore into local
     * storage, then runs onComplete so the UI can refresh. For a guest (or if
     * the user document has no saved preferences) the local values are kept and
     * onComplete still runs, so callers can treat it the same way every time.
     *
     * @param onComplete work to run after the pull finishes (e.g. bind the UI).
     */
    public void pull(Runnable onComplete) {
        if (!isSignedIn()) {
            onComplete.run();
            return;
        }
        String userId = prefs.getString(Constants.KEY_USER_ID, null);

        firestore.collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().exists()) {
                        Object raw = task.getResult().get(FIELD_PREFERENCES);
                        if (raw instanceof Map) {
                            writeToLocal((Map<?, ?>) raw);
                        }
                    }
                    onComplete.run();
                });
    }

    /**
     * Copies a preferences map from Firestore into local SharedPreferences,
     * being careful about types (Firestore returns whole numbers as Long).
     *
     * @param preferences the preferences map read from the user's document.
     */
    private void writeToLocal(Map<?, ?> preferences) {
        SharedPreferences.Editor editor = prefs.edit();

        Object username = preferences.get(KEY_USERNAME);
        if (username != null) {
            editor.putString(KEY_USERNAME, String.valueOf(username));
        }
        Object themeDark = preferences.get(KEY_THEME_DARK);
        if (themeDark instanceof Boolean) {
            editor.putBoolean(KEY_THEME_DARK, (Boolean) themeDark);
        }
        Object fontSize = preferences.get(KEY_FONT_SIZE);
        if (fontSize instanceof Number) {
            editor.putInt(KEY_FONT_SIZE, ((Number) fontSize).intValue());
        }
        Object volume = preferences.get(KEY_VOLUME);
        if (volume instanceof Number) {
            editor.putInt(KEY_VOLUME, ((Number) volume).intValue());
        }
        Object reminders = preferences.get(KEY_REMINDERS);
        if (reminders instanceof Boolean) {
            editor.putBoolean(KEY_REMINDERS, (Boolean) reminders);
        }
        Object keepLoggedIn = preferences.get(KEY_KEEP_LOGGED_IN);
        if (keepLoggedIn instanceof Boolean) {
            editor.putBoolean(KEY_KEEP_LOGGED_IN, (Boolean) keepLoggedIn);
        }

        editor.apply();
    }
}
