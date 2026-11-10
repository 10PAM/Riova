package com.example.riova;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.riova.utilities.Constants;
import com.example.riova.utilities.PreferencesHandler;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * This class handles the user registration functionality.
 * In general, this class sends a registration code to
 * the email, if it matches with the user input, then
 * a new account is created and sent to the database.
 * An email is then sent to the registration email,
 * notifying of the account creation.
 */
public class Register extends Fragment {

    private View mainView;
    private PreferencesHandler preferenceHandler;

    public Register() {
        super(R.layout.register_screen);
    }

    // Key UI Elements
    private EditText inputName;
    private EditText inputEmail;
    private EditText inputPassword;
    private Button buttonRegister;
    private Button buttonBack;
    private TextView messagePrompt;


    /**
     * Initializes pre-ui data
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceHandler = new PreferencesHandler(getContext().getApplicationContext());
    }

    /**
     *
     *
     * @param view The View returned by creation.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;

        // Initialize all views
        inputName = mainView.findViewById(R.id.input_nickname);
        inputEmail = mainView.findViewById(R.id.input_email);
        inputPassword = mainView.findViewById(R.id.input_password);
        messagePrompt = mainView.findViewById(R.id.message);
        buttonRegister = mainView.findViewById(R.id.button_register);
        buttonBack = mainView.findViewById(R.id.button_registerbacktologin);

        setListeners();
    }

    /**
     * Initializes all listeners
     */
    private void setListeners() {
        buttonRegister.setOnClickListener(v -> {
            if (validRegistration()) {
                registerUser();
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(Constants.TAG_LOGIN);
            }
        });
    }

    /**
     * Register's a user into firebase. Checks if their email is already in Firestore, if not,
     * it allows for new account creation. One time passcode required for verification.
     */
    private void registerUser() {
        loading(true);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, String> user = new HashMap<>();
        user.put(Constants.KEY_NAME, inputName.getText().toString());
        user.put(Constants.KEY_EMAIL, inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, inputPassword.getText().toString());

        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener((documentReference -> {
                    loading(false);

                    preferenceHandler.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceHandler.putString(Constants.KEY_NAME, inputName.getText().toString());

                    showPage(Constants.TAG_DASHBOARD);
                    displayMessage("Created Account!");
                    resetFields();
                })).addOnFailureListener(exception -> {
                    loading(false);
                    displayMessage("Error Creating Account!");
                });
    }

    /**
     * Resets all input fields.
     */
    private void resetFields() {
        inputName.setText("");
        inputEmail.setText("");
        inputPassword.setText("");
        displayMessage("");
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

    /*
    private void showDashboard() {}
        /* // Prevents reopening the same page
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, new Dashboard(), Constants.TAG_DASHBOARD)
                .addToBackStack(null)
                .commit();
         */

    /**
     * Displays a visible message temporarily.
     *
     * @param message The message to be displayed.
     */
    private void displayMessage(String message) {
        messagePrompt.setText(message);
    }

    /**
     * Determines if the Signup credentials are properly formatted prior to searching database
     *
     * @return Returns true if valid-credentials; false otherwise.
     */
    private Boolean validRegistration() {
        String inputtedEmail = inputEmail.getText().toString().trim();
        String inputtedPassword = inputPassword.getText().toString().trim();
        if (inputtedPassword.isEmpty()) {
            displayMessage("Please Enter your Name");
            return false;
        } else if (inputtedEmail.isEmpty()) {
            displayMessage("Please Enter your Email");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(inputtedEmail).matches()) {
            displayMessage("Please Enter valid Email");
            return false;
        } else if(inputtedPassword.length() < 14) {
            displayMessage("Password Issue: Length < 14");
            return false;
        } else if (!validPasswordExtended(inputtedPassword)) {
            displayMessage("Password Issue: Needs 1 symbol (!,@,#,$,%,&,&), 1 digit (0-9), and 1 " +
                    "character (a-f/A-F)");
            return false;
        } else {
            displayMessage("Creating Account...");
            return true;
        }
    }

    /**
     * Checks if password contains valid amount of symbols and numbers.
     * Required: 1 number.
     *
     * @return Returns true=valid; false=invalid.
     */
    private boolean validPasswordExtended(String password) {
        messagePrompt.setVisibility(View.VISIBLE);
        char[] specialSymbols = {'!', '@', '#', '$', '%', '&', '*'};
        boolean hasChar = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;

        // Check for symbol o(n)
        for (int i = 0; i < password.length(); ++i) {
            if (password.charAt(i) == specialSymbols[0] || password.charAt(i) == specialSymbols[1]
                    || password.charAt(i) == specialSymbols[2] || password.charAt(i) == specialSymbols[3] ||
                    password.charAt(i) == specialSymbols[4] || password.charAt(i) == specialSymbols[5] ||
                    password.charAt(i) == specialSymbols[6]) {
                hasSymbol = true;
                break;
            }
        }

        // Check for digit o(n)
        for (int i = 0; i < password.length(); ++i) {
            if (Character.isDigit(password.charAt(i))) {
                hasDigit = true;
                break;
            }
        }

        // Check for char o(n)
        for (int i = 0; i < password.length(); ++i) {
            if (Character.isAlphabetic(password.charAt(i))) {
                hasChar = true;
                break;
            }
        }

        return hasChar && hasDigit && hasSymbol;
    }

    /**
     * Displays the loading icon over the button, indicating searching database process.
     *
     * @param isLoading Loading indicator boolean.
     */
    private void loading(Boolean isLoading) {
        if (isLoading) {
            buttonRegister.setVisibility(View.INVISIBLE);
        } else {
            buttonRegister.setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
