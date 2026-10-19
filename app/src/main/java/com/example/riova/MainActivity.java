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
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Entry class.
 */
public class MainActivity extends AppCompatActivity {

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }
}