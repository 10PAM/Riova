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
                hideLogin();
            }
        });

        view.findViewById(R.id.button_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegister();
            }
        });

        view.findViewById(R.id.button_resetpassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetPassword();
            }
        });
    }


    /**
     * Changes the view following navigation button click.
     *
     */
    private void hideLogin() {
        // Prevents reopening the same page
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragmentManager.findFragmentByTag("page_login"));
        transaction.hide(fragmentManager.findFragmentByTag("page_resetpassword"));
        transaction.hide(fragmentManager.findFragmentByTag("page_register"));
        transaction.commit();
    }


    /**
     * Changes the view following navigation button click.
     *
     */
    private void showResetPassword() {
        // Prevents reopening the same page
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragmentManager.findFragmentByTag("page_login"));
        transaction.hide(fragmentManager.findFragmentByTag("page_register"));
        transaction.show(fragmentManager.findFragmentByTag("page_resetpassword"));
        transaction.commit();
    }


    /**
     * Changes the view following navigation button click.
     *
     */
    private void showRegister() {
        // Prevents reopening the same page
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragmentManager.findFragmentByTag("page_login"));
        transaction.show(fragmentManager.findFragmentByTag("page_register"));
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}