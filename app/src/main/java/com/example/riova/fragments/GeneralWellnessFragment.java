package com.example.riova.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import com.example.riova.R;

import java.util.Random;

public class GeneralWellnessFragment extends Fragment {

    private static final String KEY_SUGGESTIONS = "wellness_suggestions";
    private String[] selectedSuggestions;

    public GeneralWellnessFragment(){
        super(R.layout.general_wellness);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            selectedSuggestions = savedInstanceState.getStringArray(KEY_SUGGESTIONS);
        }

        int[] categories = {
                R.array.wellness_movement,
                R.array.wellness_food,
                R.array.wellness_calm,
                R.array.wellness_reflection
        };
        int[] suggestionViews = {
                R.id.text_wellness_movement,
                R.id.text_wellness_food,
                R.id.text_wellness_calm,
                R.id.text_wellness_reflection
        };

        // Pick once for this screen instance; keep the selection on rotation.
        if (selectedSuggestions == null || selectedSuggestions.length != categories.length) {
            selectedSuggestions = new String[categories.length];
            Random random = new Random();
            for (int i = 0; i < categories.length; i++) {
                String[] suggestions = getResources().getStringArray(categories[i]);
                selectedSuggestions[i] = suggestions.length == 0
                        ? "" : suggestions[random.nextInt(suggestions.length)];
            }
        }

        for (int i = 0; i < suggestionViews.length; i++) {
            TextView suggestion = view.findViewById(suggestionViews[i]);
            suggestion.setText(selectedSuggestions[i]);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(KEY_SUGGESTIONS, selectedSuggestions);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
