package com.example.riova;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Login extends Fragment {

    public Login() {
        super(R.layout.login_screen);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogin(false);
            }
        });
    }


    /**
     * Changes the view following navigation button click.
     *
     */
    private void showLogin(boolean show) {
        // Prevents reopening the same page
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (show == false) {
            // Shows a new page; Hides previous page.
            transaction.hide(fragmentManager.findFragmentByTag("page_login"));
        } else {
            // Shows a new page; Hides previous page.
            transaction.show(fragmentManager.findFragmentByTag("page_login"));
        }
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}