/*
Programmer (Team Novelty) {
    - Fulya Bilgin, CWU Student ID: 50593580
}
File: Mindfulness.java
About: Mindfulness page. Shows one motivational quote and one encouragement
       quote, chosen at random from curated lists in res/values/arrays.xml.
       The chosen quotes are refreshed once per day: the selected indices and
       the date are stored in SharedPreferences, so the same two quotes show
       all day and new ones appear when a new day is detected.
*/

package com.example.riova.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.riova.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class MindfulnessFragment extends Fragment {

    // Reuse the app's preference file; keys specific to this page.
    private static final String PREFS = "riova_prefs";
    private static final String KEY_DATE = "mindfulness_date";
    private static final String KEY_MOTIVATIONAL = "mindfulness_motivational_index";
    private static final String KEY_ENCOURAGEMENT = "mindfulness_encouragement_index";

    public MindfulnessFragment() {
        super(R.layout.mindfullness);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] motivational = getResources().getStringArray(R.array.motivational_quotes);
        String[] encouragement = getResources().getStringArray(R.array.encouragement_quotes);

        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS, 0);
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Calendar.getInstance().getTime());

        int motivationalIndex;
        int encouragementIndex;

        // Pick new quotes only when the stored day differs from today.
        if (today.equals(prefs.getString(KEY_DATE, ""))) {
            motivationalIndex = prefs.getInt(KEY_MOTIVATIONAL, 0);
            encouragementIndex = prefs.getInt(KEY_ENCOURAGEMENT, 0);
        } else {
            Random random = new Random();
            motivationalIndex = random.nextInt(motivational.length);
            encouragementIndex = random.nextInt(encouragement.length);
            prefs.edit()
                    .putString(KEY_DATE, today)
                    .putInt(KEY_MOTIVATIONAL, motivationalIndex)
                    .putInt(KEY_ENCOURAGEMENT, encouragementIndex)
                    .apply();
        }

        // Guard indices in case a list changed size since the last save.
        motivationalIndex = Math.min(motivationalIndex, motivational.length - 1);
        encouragementIndex = Math.min(encouragementIndex, encouragement.length - 1);

        ((TextView) view.findViewById(R.id.text_motivational_quote))
                .setText(motivational[motivationalIndex]);
        ((TextView) view.findViewById(R.id.text_encouragement_quote))
                .setText(encouragement[encouragementIndex]);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
