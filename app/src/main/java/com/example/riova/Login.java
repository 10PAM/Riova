package com.example.riova;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.riova.utilities.Constants;
import com.example.riova.utilities.PreferencesHandler;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class handles the user sign in functionality.
 * The preference manager saves the user's login
 * indefinitely until the user requests to sign out.
 */
public class Login extends Fragment {

    // Key UI Elements
    private EditText inputEmail;
    private EditText inputPassword;
    private Button buttonLogin;
    private Button buttonRegister;
    private Button buttonSkip;
    private Button buttonResetPassword;
    private TextView messagePrompt;

    private PreferencesHandler preferenceHandler;

    public Login() {
        super(R.layout.login_screen);
    }

    /**
     * Checks if user is logged in already, if so, sign up display is skipped.
     *
     * @param view The View returned by creation.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferenceHandler = new PreferencesHandler(getContext().getApplicationContext());

        if (preferenceHandler.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            showPage(Constants.TAG_DASHBOARD);
        }

        buttonLogin = view.findViewById(R.id.button_loginLogin);
        inputEmail = view.findViewById(R.id.input_loginEmail);
        inputPassword = view.findViewById(R.id.input_loginPassword);
        buttonSkip = view.findViewById(R.id.button_skip);
        buttonRegister = view.findViewById(R.id.button_loginRegister);
        buttonResetPassword = view.findViewById(R.id.button_resetpassword);
        messagePrompt = view.findViewById(R.id.message_login);

        buttonLogin.setOnClickListener(v -> {
            if (validLogin()) {
                loginUser();
            }
        });

        buttonSkip.setOnClickListener(v -> {
            showPage(Constants.TAG_DASHBOARD);
        });

        buttonRegister.setOnClickListener(v -> {
            showPage(Constants.TAG_REGISTER);
        });

        buttonResetPassword.setOnClickListener(v -> {
            showPage(Constants.TAG_RESETPASSWORD);
        });
    }

    /**
     * Searches database for valid user sign in credentials.
     */
    private void loginUser() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                        preferenceHandler.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceHandler.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceHandler.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));

                        showPage(Constants.TAG_DASHBOARD);
                        displayMessage("Created Account!");
                    } else {
                        loading(false);
                    }
                });
    }

    /**
     * Displays the loading icon over the button, indicating searching database process.
     *
     * @param isLoading Loading indicator boolean.
     */
    private void loading(Boolean isLoading) {
        if (isLoading) {
            buttonLogin.setVisibility(View.INVISIBLE);
        } else {
            buttonLogin.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Determines if the Sign in credentials are properly formatted prior to searching database
     *
     * @return Returns true if valid-credentials; false otherwise.
     */
    private boolean validLogin() {
        if (inputEmail.getText().toString().trim().isEmpty()) {
            displayMessage("Please Enter your Email");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString().trim()).matches()) {
            displayMessage("Please Enter valid Email");
            return false;
        }else if (inputPassword.getText().toString().trim().isEmpty()) {
            displayMessage("Please Enter your Password");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Displays a visible message temporarily.
     *
     * @param message The message to be displayed.
     */
    private void displayMessage(String message) {
        messagePrompt.setText(message);
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