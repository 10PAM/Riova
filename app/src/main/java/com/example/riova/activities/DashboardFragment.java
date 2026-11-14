/*
Programmer (Team Novelty) {
    - Fulya Bilgin, CWU Student ID: 50593580
}
File: Dashboard.java
About: Landing page shown after login. Displays the current date, a
       month-specific wellness quote, a user-guide dialog, and a list of
       featured recent app updates.

       Data (quotes + updates) currently lives in res/values/arrays.xml so it
       is easy to maintain. This is structured so the source can later be
       swapped for the cloud (Firebase) without changing the UI code.
*/

package com.example.riova.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.riova.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    public DashboardFragment() {
        super(R.layout.dashboard);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showCurrentDate(view);
        showMonthQuote(view);
        populateUpdates(view);
        setupGuideButton(view);
    }

    /**
     * Formats today's date (e.g. "Sunday, November 8, 2026") and shows it.
     * Uses Calendar/SimpleDateFormat for compatibility with minSdk 24.
     *
     * @param view the fragment's root view.
     */
    private void showCurrentDate(View view) {
        TextView dateView = view.findViewById(R.id.text_date);
        SimpleDateFormat formatter =
                new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault());
        dateView.setText(formatter.format(Calendar.getInstance().getTime()));
    }

    /**
     * Selects and displays the quote for the current month. The quote list is
     * indexed 0-11 to match Calendar.MONTH (January = 0 ... December = 11).
     *
     * @param view the fragment's root view.
     */
    private void showMonthQuote(View view) {
        TextView quoteView = view.findViewById(R.id.text_month_quote);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        String[] quotes = getResources().getStringArray(R.array.month_quotes);

        // Guard against an out-of-range index just in case the array changes.
        if (month >= 0 && month < quotes.length) {
            quoteView.setText(quotes[month]);
        }
    }

    /**
     * Fills the updates container with one row per recent app update, read from
     * res/values/arrays.xml.
     *
     * @param view the fragment's root view.
     */
    private void populateUpdates(View view) {
        LinearLayout container = view.findViewById(R.id.container_updates);
        container.removeAllViews();

        String[] updates = getResources().getStringArray(R.array.app_updates);
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (String update : updates) {
            // Reuse the shared list_item_update row layout for consistent styling.
            TextView row = (TextView) inflater.inflate(
                    R.layout.dashboard_update_item, container, false);
            row.setText(update);
            container.addView(row);
        }
    }

    /**
     * Wires the guide button to open a dialog explaining the basics of the app.
     *
     * @param view the fragment's root view.
     */
    private void setupGuideButton(View view) {
        view.findViewById(R.id.button_guide).setOnClickListener(v ->
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.dashboard_guide_title)
                        .setMessage(R.string.dashboard_guide_body)
                        .setPositiveButton(R.string.dashboard_guide_dismiss, null)
                        .show());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
