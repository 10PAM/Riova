/*
Programmers (Team Novelty) {
    - Mario Aguilera Piceno, CWU Student ID: 49998581
    -
    -
    -
}
Date: 10/6/2026
Modified: 10/20/2026
File: MainActivity.java
About: Creates and initializes app functions once created.

 */

// Defines package
package com.example.riova;

// Required imports
// import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Entry class.
 */
public class MainActivity extends AppCompatActivity {

    /*
    Navigation Buttons
     */
    private ImageButton button_dashboard;
    private ImageButton button_mindful;
    private  ImageButton button_settings;
    private ImageButton button_journal;
    private ImageButton button_meditation;
    private ImageButton button_genwell;
    private TextView header_active_title;
    // private int currentPageId = R.id.fragment_dashboard;
    private String currentPage = TAG_DASHBOARD;


    private static final String KEY_CURRENT_PAGE = "current_page";
    // Tags to find and retrieve pages' fragments
    private static final String TAG_DASHBOARD = "page_dashboard";
    private static final String TAG_JOURNAL = "page_journal";
    private static final String TAG_GENWELL = "page_genwell";
    private static final String TAG_MEDITATION = "page_meditation";
    private static final String TAG_MINDFULNESS = "page_mindfulness";
    private static final String TAG_SETTINGS = "page_settings";

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

        // Sets intial layout view
        setContentView(R.layout.navigation_core);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new Dashboard(), TAG_DASHBOARD)
                    .add(R.id.fragment_container, new Journal(), TAG_JOURNAL)
                    .add(R.id.fragment_container, new GeneralWellness(), TAG_GENWELL)
                    .add(R.id.fragment_container, new Mindfulness(), TAG_MINDFULNESS)
                    .add(R.id.fragment_container, new Meditation(), TAG_MEDITATION)
                    .add(R.id.fragment_container, new Settings(), TAG_SETTINGS)
                    .commitNow();
        } //else {
          //  currentPageId = savedInstanceState.getInt(KEY_CURRENT_PAGE, R.id.fragment_dashboard);
        //}


        // Initialize navigation objects
        button_dashboard = findViewById(R.id.button_dashboard);
        button_mindful = findViewById(R.id.button_mindfullness);
        button_journal = findViewById(R.id.button_journal);
        button_meditation = findViewById(R.id.button_meditation);
        button_genwell = findViewById(R.id.button_genwell);
        button_settings = findViewById(R.id.button_settings);
        header_active_title = findViewById(R.id.header_description);

        // Setup initial page. NOTE: setInitialView() called once only before viewChange().
        setInitialView();
        viewChange(button_dashboard, TAG_DASHBOARD);

        // Define footer navigation buttons' functionality
        button_dashboard.setOnClickListener(v -> viewChange(button_dashboard, TAG_DASHBOARD));
        button_settings.setOnClickListener(v -> viewChange(button_settings, TAG_SETTINGS));
        button_mindful.setOnClickListener(v -> viewChange(button_mindful, TAG_MINDFULNESS));
        button_journal.setOnClickListener(v -> viewChange(button_journal, TAG_JOURNAL));
        button_meditation.setOnClickListener(v -> viewChange(button_meditation, TAG_MEDITATION));
        button_genwell.setOnClickListener(v -> viewChange(button_genwell, TAG_GENWELL));

    }

    /**
     *
     */
    private void setInitialView() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Hides all visible fragments, then shows specified fragment
        transaction.hide(fragmentManager.findFragmentByTag(TAG_JOURNAL));
        transaction.hide(fragmentManager.findFragmentByTag(TAG_SETTINGS));
        transaction.hide(fragmentManager.findFragmentByTag(TAG_GENWELL));
        transaction.hide(fragmentManager.findFragmentByTag(TAG_MINDFULNESS));
        transaction.hide(fragmentManager.findFragmentByTag(TAG_MEDITATION));
        transaction.show(fragmentManager.findFragmentByTag(TAG_DASHBOARD));
        transaction.commit();
    }

    /**
     * Changes the view following navigation button click.
     *
     * @param button the button clicked (active)
     */
    private void viewChange(ImageButton button, final String TAG_FRAGMENT) {

        // Prevents reopening the same page
        if (TAG_FRAGMENT.equals(this.currentPage)) {
            return;
        }

        button_dashboard.setImageResource(R.drawable.inactive_button);
        button_mindful.setImageResource(R.drawable.inactive_button);
        button_journal.setImageResource(R.drawable.inactive_button);
        button_meditation.setImageResource(R.drawable.inactive_button);
        button_genwell.setImageResource(R.drawable.inactive_button);

        if (button.getId() != button_settings.getId()) {
            button.setImageResource(R.drawable.ic_action_name);
        }

        header_active_title.setText(button.getContentDescription());

        // Shows a new page; Hides previous page.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragmentManager.findFragmentByTag(currentPage));
        transaction.show(fragmentManager.findFragmentByTag(TAG_FRAGMENT));
        currentPage = TAG_FRAGMENT;
        transaction.commit();

    }

/*
    private void showPage(int pageId) {
        //Show this page's Fragment and hide the other two. show/hide (instead of
        // replace) keeps each page alive, with its scroll position and selections.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment page = fragmentManager.findFragmentByTag("dashboard");
        transaction.hide(page);
        transaction.show(fragmentManager.findFragmentByTag("journal"));
        transaction.commit();
    } */

}