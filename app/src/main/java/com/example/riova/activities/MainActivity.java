// Declare package
package com.example.riova.activities;

// Required imports
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.riova.R;
import com.example.riova.utilities.Constants;

/**
 * Entry class.
 */
public class MainActivity extends AppCompatActivity {

    // Navigation components
    private ImageButton button_dashboard;
    private ImageButton button_mindful;
    private ImageButton button_settings;
    private ImageButton button_journal;
    private ImageButton button_meditation;
    private ImageButton button_genwell;
    private TextView header_active_title;
    private String currentPage = Constants.TAG_DASHBOARD;

    /**
     * Program creation start; Initializes and connects all files and methods together.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Calls parent class with this.
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Sets initial layout view
        setContentView(R.layout.navigation_core);

        // Initiates fragments
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new DashboardFragment(), Constants.TAG_DASHBOARD)
                    .add(R.id.fragment_container, new JournalFragment(), Constants.TAG_JOURNAL)
                    .add(R.id.fragment_container, new GeneralWellnessFragment(), Constants.TAG_GENWELL)
                    .add(R.id.fragment_container, new MindfulnessFragment(), Constants.TAG_MINDFULNESS)
                    .add(R.id.fragment_container, new MeditationFragment(), Constants.TAG_MEDITATION)
                    .add(R.id.fragment_container, new SettingsFragment(), Constants.TAG_SETTINGS)
                    .add(R.id.fragment_container_high, new ResetPasswordFragment(), Constants.TAG_RESETPASSWORD)
                    .add(R.id.fragment_container_high, new RegisterFragment(), Constants.TAG_REGISTER)
                    .add(R.id.fragment_container_high, new LogoutFragment(), Constants.TAG_LOGOUT)
                    .add(R.id.fragment_container_high, new LoginFragment(), Constants.TAG_LOGIN)
                    .add(R.id.fragment_container_high, new JournalAppendActivity(), Constants.TAG_JOURNAL_APPEND)
                    .commitNow();
        }

        // Initialize navigation objects
        button_dashboard = findViewById(R.id.button_dashboard);
        button_mindful = findViewById(R.id.button_mindfullness);
        button_journal = findViewById(R.id.button_journal);
        button_meditation = findViewById(R.id.button_meditation);
        button_genwell = findViewById(R.id.button_genwell);
        button_settings = findViewById(R.id.button_settings);
        header_active_title = findViewById(R.id.header_description);

        // Setup initial page. NOTE: setInitialView() called once only before viewChange().
        setInitialView(button_dashboard, currentPage);
        header_active_title.setText(button_dashboard.getContentDescription());

        // Define footer navigation buttons' functionality
        button_dashboard.setOnClickListener(v -> viewChange(button_dashboard, Constants.TAG_DASHBOARD));
        button_settings.setOnClickListener(v -> viewChange(button_settings, Constants.TAG_SETTINGS));
        button_mindful.setOnClickListener(v -> viewChange(button_mindful, Constants.TAG_MINDFULNESS));
        button_journal.setOnClickListener(v -> viewChange(button_journal, Constants.TAG_JOURNAL));
        button_meditation.setOnClickListener(v -> viewChange(button_meditation, Constants.TAG_MEDITATION));
        button_genwell.setOnClickListener(v -> viewChange(button_genwell, Constants.TAG_GENWELL));
    }

    /**
     * Sets the initial view of the app.
     *
     * @param button the button clicked.
     * @param PAGE_TAG the tag of the initial page.
     */
    private void setInitialView(ImageButton button, final String PAGE_TAG) {
        // Resets active button images
        button_dashboard.setImageResource(R.drawable.inactive_button);
        button_mindful.setImageResource(R.drawable.inactive_button);
        button_journal.setImageResource(R.drawable.inactive_button);
        button_meditation.setImageResource(R.drawable.inactive_button);
        button_genwell.setImageResource(R.drawable.inactive_button);

        // Sets active button image on clicked button if not settings
        if (button.getId() != button_settings.getId()) {
            button.setImageResource(R.drawable.ic_action_name);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Hides all visible fragments, then shows specified fragment
        transaction.hide(fragmentManager.findFragmentByTag(Constants.TAG_JOURNAL));
        transaction.hide(fragmentManager.findFragmentByTag(Constants.TAG_SETTINGS));
        transaction.hide(fragmentManager.findFragmentByTag(Constants.TAG_GENWELL));
        transaction.hide(fragmentManager.findFragmentByTag(Constants.TAG_MINDFULNESS));
        transaction.hide(fragmentManager.findFragmentByTag(Constants.TAG_MEDITATION));
        transaction.hide(fragmentManager.findFragmentByTag(Constants.TAG_RESETPASSWORD));
        transaction.hide(fragmentManager.findFragmentByTag(Constants.TAG_REGISTER));
        transaction.hide(fragmentManager.findFragmentByTag(Constants.TAG_LOGOUT));
        transaction.hide(fragmentManager.findFragmentByTag(Constants.TAG_JOURNAL_APPEND));
        transaction.show(fragmentManager.findFragmentByTag(PAGE_TAG));
        transaction.commit();
    }

    /**
     * Changes the view following navigation button click.
     *
     * @param button the button clicked (active)
     * @param TAG_FRAGMENT the TAG of page being shown.
     */
    private void viewChange(ImageButton button, final String TAG_FRAGMENT) {
        // Prevents reopening the same page
        if (TAG_FRAGMENT.equals(this.currentPage)) {
            return;
        }

        // Resets active button images
        button_dashboard.setImageResource(R.drawable.inactive_button);
        button_mindful.setImageResource(R.drawable.inactive_button);
        button_journal.setImageResource(R.drawable.inactive_button);
        button_meditation.setImageResource(R.drawable.inactive_button);
        button_genwell.setImageResource(R.drawable.inactive_button);

        // Sets active button image on clicked button if not settings
        if (button.getId() != button_settings.getId()) {
            button.setImageResource(R.drawable.ic_action_name);
        }

        // Sets text for navigation header
        header_active_title.setText(button.getContentDescription());

        // Shows a new page; Hides previous page.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragmentManager.findFragmentByTag(currentPage));
        transaction.show(fragmentManager.findFragmentByTag(TAG_FRAGMENT));
        currentPage = TAG_FRAGMENT;
        transaction.commit();
    }

    /**
     * Retrieves the current page.
     *
     * @return Returns current page.
     */
    private String getPreviousPage() {
        return this.currentPage;
    }
}
