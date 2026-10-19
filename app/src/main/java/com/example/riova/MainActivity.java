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
import android.content.Intent;
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
    private ImageButton button_journal;
    private ImageButton button_meditation;
    private ImageButton button_genwell;
    private TextView header_active_title;

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
        setContentView(R.layout.dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_dashboard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_dashboard, new Dashboard(), "dashboard")
                    .add(R.id.fragment_journal, new Journal(), "journal")
                    .commitNow();
        }*/

        // Initialize navigation objects
        button_dashboard = findViewById(R.id.button_dashboard);
        button_mindful = findViewById(R.id.button_mindfullness);
        button_journal = findViewById(R.id.button_journal);
        button_meditation = findViewById(R.id.button_meditation);
        button_genwell = findViewById(R.id.button_genwell);
        header_active_title = findViewById(R.id.header_description);

        // Setup initial page
        viewChange(button_dashboard, R.layout.dashboard);

        // Define footer navigation button functionality
        button_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewChange(button_dashboard, R.layout.dashboard);
            }
        });
        button_mindful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewChange(button_mindful, R.layout.mindfullness);
            }
        });
        button_journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewChange(button_journal, R.layout.journal);
            }
        });
        button_meditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewChange(button_meditation, R.layout.meditation);
            }
        });
        button_genwell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewChange(button_genwell, R.layout.general_wellness);
            }
        });

    }

    /**
     * Changes the view following navigation button click.
     *
     * @param button the button clicked (active)
     * @param viewId the view to be displayed.
     */
    private void viewChange(ImageButton button, int viewId) {
        View view = findViewById(viewId);

        button_dashboard.setImageResource(R.drawable.inactive_button);
        button_mindful.setImageResource(R.drawable.inactive_button);
        button_journal.setImageResource(R.drawable.inactive_button);
        button_meditation.setImageResource(R.drawable.inactive_button);
        button_genwell.setImageResource(R.drawable.inactive_button);

        button.setImageResource(R.drawable.ic_action_name);

        header_active_title.setText(button.getContentDescription());

        /*
        Intent intent = new Intent(MainActivity.this, Journal.class);
        startActivity(intent);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment page = fragmentManager.findFragmentByTag("dashboard");
        transaction.hide(page);
        transaction.show(fragmentManager.findFragmentByTag("journal"));
        transaction.commit();

         */
    }


    private void showPage(int pageId) {
        //Show this page's Fragment and hide the other two. show/hide (instead of
        // replace) keeps each page alive, with its scroll position and selections.
        /*
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment page = fragmentManager.findFragmentByTag("dashboard");
        transaction.hide(page);
        transaction.show(fragmentManager.findFragmentByTag("journal"));
        transaction.commit();
*/
    }

}