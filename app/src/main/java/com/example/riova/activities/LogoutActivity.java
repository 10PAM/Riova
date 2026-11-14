package com.example.riova.activities;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.riova.R;
import com.example.riova.utilities.Constants;
import com.example.riova.utilities.PreferencesHandler;

/**
 * This class handles the user sign out functionality.
 */
public class LogoutActivity extends Fragment {

    public LogoutActivity() {
        super(R.layout.logout);
    }

    private PreferencesHandler preferenceHandler;

    /**
     * Memory management from
     *
     * @param view The View returned by created View.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferenceHandler = new PreferencesHandler(getContext().getApplicationContext());

        view.findViewById(R.id.button_hidelogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(Constants.TAG_SETTINGS);
            }
        });

        // If user has account; Skips.
        view.findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showLogin();
                logoutUser();
            }
        });
    }

    /**
     * Clear out local preferences to require sign-in.
     */
    private void logoutUser() {
        preferenceHandler.clear();
        showPage(Constants.TAG_LOGIN);
    }

    /**
     * Shows a Fragment.
     *
     * @param tag The tag of the fragment to be displayed.
     */
    private void showPage(String tag) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = 0; i < Constants.page_tag_array.length; ++i) {
            transaction.hide(fragmentManager.findFragmentByTag(Constants.page_tag_array[i]));
        }
        transaction.show(fragmentManager.findFragmentByTag(tag));
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}