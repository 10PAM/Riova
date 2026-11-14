package com.example.riova.activities;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.riova.R;

/**
 * This class handles the user password reset functionality.
 * In general, the user enters an email, the email is looked
 * for within the database, and if email is valid, a one-time
 * passcode is sent to reset the password for the account.
 * The new password is then saved to the database and a
 * notification is sent to the email.
 */
public class ResetPasswordActivity extends Fragment {

    public ResetPasswordActivity() {
        super(R.layout.reset_password);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_backtologin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogin();
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
        transaction.hide(fragmentManager.findFragmentByTag("page_resetpassword"));
        transaction.show(fragmentManager.findFragmentByTag("page_login"));
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}