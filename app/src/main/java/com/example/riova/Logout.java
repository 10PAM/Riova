package com.example.riova;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Logout extends Fragment {

    public Logout() {
        super(R.layout.logout);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_hidelogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLogout();
            }
        });

        // If user has account; Skips.
        view.findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showLogin();
                hideLogout();
            }
        });
    }

    /**
     * Changes the view following navigation button click.
     *
     */
    private void showLogin() {
        // Prevents reopening the same page
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragmentManager.findFragmentByTag("page_logout"));
        transaction.show(fragmentManager.findFragmentByTag("page_login"));
        transaction.commit();
    }

    /**
     * Changes the view following navigation button click.
     *
     */
    private void hideLogout() {
        // Prevents reopening the same page
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragmentManager.findFragmentByTag("page_logout"));
        transaction.show(fragmentManager.findFragmentByTag("page_settings"));
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}