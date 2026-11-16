/*
Programmer (Team Novelty) {
    - Fulya Bilgin, CWU Student ID: 50593580
}
File: Settings.java
About: User Settings page. Loads and saves the user's preferences - username,
       theme (dark/light), font size, master volume, reminders, and
       "keep me logged in" - using SharedPreferences for local storage.

       When the user is signed in, the values are also synced to their account
       in Firestore through PreferencesSync (pulled when the page opens and
       pushed whenever a setting changes); guests keep using the local copy
       only. The existing sign in/out action is preserved.
*/

package com.example.riova.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.riova.R;
import com.example.riova.utilities.Constants;
import com.example.riova.utilities.PreferencesHandler;
import com.example.riova.utilities.PreferencesSync;
import com.example.riova.utilities.FontScaler;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsFragment extends Fragment {

    // SharedPreferences file and keys.
    private static final String PREFS = "riova_prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_THEME_DARK = "theme_dark";
    private static final String KEY_FONT_SIZE = "font_size";
    private static final String KEY_VOLUME = "master_volume";
    private static final String KEY_REMINDERS = "reminders_enabled";
    private static final String KEY_KEEP_LOGGED_IN = "keep_logged_in";

    PreferencesHandler preferencesHandler;
    PreferencesSync preferencesSync;
    Button sign_in_out_button;

    public SettingsFragment() {
        super(R.layout.settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferencesHandler = new PreferencesHandler(getContext());
        preferencesSync = new PreferencesSync(requireContext());

        SharedPreferences prefs =
                requireContext().getSharedPreferences(PREFS, 0);

        // Pull the signed-in user's saved preferences from the cloud first,
        // then bind the controls so they reflect the account's values. For a
        // guest this callback runs immediately with the local values.
        preferencesSync.pull(() -> {
            if (!isAdded()) {
                return;
            }
            setupUsername(view, prefs);
            setupTheme(view, prefs);
            setupFontSize(view, prefs);
            setupVolume(view, prefs);
            setupReminders(view, prefs);
            setupKeepLoggedIn(view, prefs);

            // Apply the saved font-size setting to this page.
            FontScaler.apply(view, prefs.getInt(KEY_FONT_SIZE, 1));
        });

        // Existing sign-out action from the scaffold.
        sign_in_out_button = view.findViewById(R.id.button_sign_in_out);
        updateSignInOutButton();
        sign_in_out_button.setOnClickListener(v -> {
            if (preferencesHandler.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
                preferencesHandler.clear();
            }
            showPage(Constants.TAG_LOGIN);
        });
    }

    /**
     * Loads the saved username and saves a new one when the user taps Save.
     */
    private void setupUsername(View view, SharedPreferences prefs) {
        EditText usernameField = view.findViewById(R.id.edit_username);
        usernameField.setText(prefs.getString(KEY_USERNAME, ""));

        Button saveButton = view.findViewById(R.id.button_save_username);
        saveButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            prefs.edit().putString(KEY_USERNAME, username).apply();
            preferencesSync.push();
            Toast.makeText(requireContext(),
                    R.string.settings_username_saved, Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Applies and persists the dark/light theme choice. Applying the saved
     * value is a no-op when it already matches, so it is safe on load.
     */
    private void setupTheme(View view, SharedPreferences prefs) {
        boolean dark = prefs.getBoolean(KEY_THEME_DARK, false);
        applyTheme(dark);

        MaterialSwitch themeSwitch = view.findViewById(R.id.switch_theme);
        themeSwitch.setChecked(dark);
        themeSwitch.setOnCheckedChangeListener((button, isChecked) -> {
            prefs.edit().putBoolean(KEY_THEME_DARK, isChecked).apply();
            preferencesSync.push();
            applyTheme(isChecked);
        });
    }

    /**
     * Switches between dark and light mode for the whole app.
     */
    private void applyTheme(boolean dark) {
        AppCompatDelegate.setDefaultNightMode(dark
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO);
    }

    /**
     * Loads and saves the font-size preference (0 = small, 1 = medium, 2 = large).
     */
    private void setupFontSize(View view, SharedPreferences prefs) {
        SeekBar fontBar = view.findViewById(R.id.seek_font);
        fontBar.setProgress(prefs.getInt(KEY_FONT_SIZE, 1));
        fontBar.setOnSeekBarChangeListener(new SimpleSeekListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                prefs.edit().putInt(KEY_FONT_SIZE, seekBar.getProgress()).apply();
                preferencesSync.push();
                // Update this page's text size immediately for live feedback.
                FontScaler.apply(view, seekBar.getProgress());
            }
        });
    }

    /**
     * Loads and saves the master-volume preference (0-100).
     */
    private void setupVolume(View view, SharedPreferences prefs) {
        SeekBar volumeBar = view.findViewById(R.id.seek_volume);
        volumeBar.setProgress(prefs.getInt(KEY_VOLUME, 50));
        volumeBar.setOnSeekBarChangeListener(new SimpleSeekListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                prefs.edit().putInt(KEY_VOLUME, seekBar.getProgress()).apply();
                preferencesSync.push();
            }
        });
    }

    /**
     * Loads and saves whether reminders are enabled.
     */
    private void setupReminders(View view, SharedPreferences prefs) {
        MaterialSwitch reminders = view.findViewById(R.id.switch_reminders);
        reminders.setChecked(prefs.getBoolean(KEY_REMINDERS, false));
        reminders.setOnCheckedChangeListener((button, isChecked) -> {
            prefs.edit().putBoolean(KEY_REMINDERS, isChecked).apply();
            preferencesSync.push();
        });
    }

    /**
     * Loads and saves the "keep me logged in" preference.
     */
    private void setupKeepLoggedIn(View view, SharedPreferences prefs) {
        MaterialSwitch keepLoggedIn = view.findViewById(R.id.switch_keep_logged_in);
        keepLoggedIn.setChecked(prefs.getBoolean(KEY_KEEP_LOGGED_IN, false));
        keepLoggedIn.setOnCheckedChangeListener((button, isChecked) -> {
            prefs.edit().putBoolean(KEY_KEEP_LOGGED_IN, isChecked).apply();
            preferencesSync.push();
        });
    }

    /**
     * Shows a Fragment.
     *
     * @param tag The tag of the fragment to be displayed.
     */
    private void showPage(String tag) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (String page_tag : Constants.page_tag_array) {
            transaction.hide(fragmentManager.findFragmentByTag(page_tag));
        }
        transaction.show(fragmentManager.findFragmentByTag(tag));
        transaction.commit();
    }

    /**
     * Updates button text depending on user's logged-in status.
     */
    private void updateSignInOutButton() {
        if (preferencesHandler.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            sign_in_out_button.setText("Sign Out");
        } else {
            sign_in_out_button.setText("Sign In");
        }
    }

    /**
     * Small adapter so each SeekBar only needs to override onStopTrackingTouch.
     */
    private abstract static class SimpleSeekListener
            implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            updateSignInOutButton();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
