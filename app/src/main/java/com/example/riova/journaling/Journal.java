package com.example.riova.journaling;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.riova.R;

/**
 * This class handles the user journaling functionality.
 */
public class Journal extends Fragment {

    public boolean isSelected = false;

    public Journal() {
        super(R.layout.journal);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
    }
}