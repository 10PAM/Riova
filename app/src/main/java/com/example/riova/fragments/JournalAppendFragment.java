package com.example.riova.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.riova.R;
import com.example.riova.utilities.PreferencesHandler;

public class JournalAppendFragment extends Fragment  {

    PreferencesHandler preferencesHandler;
    View mainView;

    public JournalAppendFragment() {
        super(R.layout.journal_append);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        preferencesHandler = new PreferencesHandler(getContext());
    }

}
