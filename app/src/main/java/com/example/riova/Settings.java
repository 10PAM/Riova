package com.example.riova;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Settings extends Fragment {

    public Settings() {
        super(R.layout.settings);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_signout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogout();
            }
        });
    }


    /**
     * Changes the view following navigation button click.
     *
     */
    private void showLogout() {
        // Prevents reopening the same page
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragmentManager.findFragmentByTag("page_settings"));
        transaction.show(fragmentManager.findFragmentByTag("page_logout"));
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}