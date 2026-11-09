/*
Programmer (Team Novelty) {
    - Fulya Bilgin, CWU Student ID: 50593580
}
File: Settings.java
About: User Settings page. Loads and saves the user's preferences - username,
       theme (dark/light), font size, master volume, reminders, and
       "keep me logged in" - using SharedPreferences for local storage.

       Storage is kept behind simple get/save helpers so the same values can
       later be synced to the account (Firebase) when the user is signed in,
       without changing the UI code. The existing Sign Out action is preserved.
*/

package com.example.riova;

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

import com.google.android.material.materialswitch.MaterialSwitch;

public class Settings extends Fragment {

    // SharedPreferences file and keys.
    private static final String PREFS = "riova_prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_THEME_DARK = "theme_dark";
    private static final String KEY_FONT_SIZE = "font_size";
    private static final String KEY_VOLUME = "master_volume";
    private static final String KEY_REMINDERS = "reminders_enabled";
    private static final String KEY_KEEP_LOGGED_IN = "keep_logged_in";

    public Settings() {
        super(R.layout.settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs =
                requireContext().getSharedPreferences(PREFS, 0);

        setupUsername(view, prefs);
        setupTheme(view, prefs);
        setupFontSize(view, prefs);
        setupVolume(view, prefs);
        setupReminders(view, prefs);
        setupKeepLoggedIn(view, prefs);

        // Existing sign-out action from the scaffold.
        view.findViewById(R.id.button_signout).setOnClickListener(v -> showLogout());
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
            }
        });
    }

    /**
     * Loads and saves whether reminders are enabled.
     */
    private void setupReminders(View view, SharedPreferences prefs) {
        MaterialSwitch reminders = view.findViewById(R.id.switch_reminders);
        reminders.setChecked(prefs.getBoolean(KEY_REMINDERS, false));
        reminders.setOnCheckedChangeListener((button, isChecked) ->
                prefs.edit().putBoolean(KEY_REMINDERS, isChecked).apply());
    }

    /**
     * Loads and saves the "keep me logged in" preference.
     */
    private void setupKeepLoggedIn(View view, SharedPreferences prefs) {
        MaterialSwitch keepLoggedIn = view.findViewById(R.id.switch_keep_logged_in);
        keepLoggedIn.setChecked(prefs.getBoolean(KEY_KEEP_LOGGED_IN, false));
        keepLoggedIn.setOnCheckedChangeListener((button, isChecked) ->
                prefs.edit().putBoolean(KEY_KEEP_LOGGED_IN, isChecked).apply());
    }

    /**
     * Shows the logout page (unchanged from the original scaffold).
     */
    private void showLogout() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragmentManager.findFragmentByTag("page_settings"));
        transaction.show(fragmentManager.findFragmentByTag("page_logout"));
        transaction.commit();
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
    public void onDestroyView() {
        super.onDestroyView();
    }
}
