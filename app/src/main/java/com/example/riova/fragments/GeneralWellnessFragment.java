package com.example.riova.fragments;

import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import com.example.riova.R;

import java.util.Random;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GeneralWellnessFragment extends Fragment {

    private static final String PREFS = "riova_wellness_daily";
    private static final String KEY_DATE = "date";
    private static final String KEY_COMPLETED_PREFIX = "completed_";

    public GeneralWellnessFragment(){
        super(R.layout.general_wellness);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displayDailySuggestions(view);
    }

    private void displayDailySuggestions(@NonNull View view) {

        int[] categories = {
                R.array.wellness_movement,
                R.array.wellness_food,
                R.array.wellness_calm,
                R.array.wellness_reflection
        };

        // Text inside the card. For example: Add a vegetable to a meal.
        int[] suggestionViews = {
                R.id.text_wellness_movement,
                R.id.text_wellness_food,
                R.id.text_wellness_calm,
                R.id.text_wellness_reflection
        };


        int[] completionViews = {
                R.id.check_wellness_movement,
                R.id.check_wellness_food,
                R.id.check_wellness_calm,
                R.id.check_wellness_reflection
        };

        // This will save the general wellness suggestions for the day
        SharedPreferences preferences = requireContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        String[] selectedSuggestions = new String[categories.length];

        // We will check todays date against the date that is saved
        // Once there is a new day, todays day won't match the date saved, so we will refresh suggestions
        boolean needsNewSuggestions = !today.equals(preferences.getString(KEY_DATE, ""));


        // This is starting out initially, since selectedSuggestion will be null.
        for (int i = 0; i < categories.length; i++) {
            selectedSuggestions[i] = preferences.getString("suggestion_" + i, null);
            if (selectedSuggestions[i] == null) {
                needsNewSuggestions = true;
            }
        }

        // Save one selection per local calendar day, including across app restarts.
        if (needsNewSuggestions) {
            SharedPreferences.Editor editor = preferences.edit().putString(KEY_DATE, today);

            // Pick random suggestion from category.
            Random random = new Random();
            for (int i = 0; i < categories.length; i++) {
                String[] suggestions = getResources().getStringArray(categories[i]);
                selectedSuggestions[i] = suggestions.length == 0
                        ? "" : suggestions[random.nextInt(suggestions.length)];
                editor.putString("suggestion_" + i, selectedSuggestions[i]);
                editor.remove(KEY_COMPLETED_PREFIX + i);
            }
            editor.apply();
        }


        TextView progress = view.findViewById(R.id.text_wellness_progress);

        // Loop through suggestions and set the suggested text for each category in the card.
        for (int i = 0; i < suggestionViews.length; i++) {
            TextView suggestion = view.findViewById(suggestionViews[i]);
            suggestion.setText(selectedSuggestions[i]);

            CheckBox completed = view.findViewById(completionViews[i]);
            String completionKey = KEY_COMPLETED_PREFIX + i;
            completed.setOnCheckedChangeListener(null);
            completed.setChecked(preferences.getBoolean(completionKey, false));
            completed.setOnCheckedChangeListener((button, isChecked) -> {
                preferences.edit().putBoolean(completionKey, isChecked).apply();
                updateProgress(progress, preferences, completionViews.length);
            });
        }
        updateProgress(progress, preferences, completionViews.length);
    }

    private void updateProgress(TextView progress, SharedPreferences preferences, int total) {
        int completedCount = 0;
        for (int i = 0; i < total; i++) {
            if (preferences.getBoolean(KEY_COMPLETED_PREFIX + i, false)) {
                completedCount++;
            }
        }
        progress.setText(getString(R.string.wellness_progress, completedCount, total));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden() && getView() != null) {
            displayDailySuggestions(getView());
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // Navigation shows and hides existing fragments rather than recreating them.
        if (!hidden && getView() != null) {
            displayDailySuggestions(getView());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
