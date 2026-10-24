package com.example.riova;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Register extends Fragment {

    public Register() {
        super(R.layout.register_screen);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_registerbacktologin).setOnClickListener(new View.OnClickListener() {
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