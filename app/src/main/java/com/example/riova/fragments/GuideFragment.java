/*
Programmer (Team Novelty) {
    - Fulya Bilgin, CWU Student ID: 50593580
}
File: GuideFragment.java
About: Full-screen "App Guide" page opened from the Dashboard. This replaces the
       old pop-up dialog (MaterialAlertDialog) so the guide matches the rest of
       the app: a themed full-screen overlay with a BACK button that returns to
       the Dashboard, using the same hide/show navigation as the other pages.

       The guide text lives in res/values/strings.xml (dashboard_guide_title and
       dashboard_guide_body) and in res/layout/guide.xml, so this class only has
       to wire the BACK button and apply the user's font-size setting.
*/

package com.example.riova.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.riova.R;
import com.example.riova.utilities.Constants;
import com.example.riova.utilities.FontScaler;

public class GuideFragment extends Fragment {

    public GuideFragment() {
        super(R.layout.guide);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // BACK returns to the Dashboard.
        view.findViewById(R.id.button_guide_back).setOnClickListener(v ->
                showPage(Constants.TAG_DASHBOARD));

        // Respect the user's saved font-size setting, like the other pages.
        FontScaler.applyFromPrefs(view, requireContext());
    }

    /**
     * Shows one page fragment by tag and hides all the others, matching the
     * hide/show navigation used across the app.
     *
     * @param tag the tag of the fragment to display.
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
