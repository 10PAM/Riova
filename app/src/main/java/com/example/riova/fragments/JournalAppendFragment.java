package com.example.riova.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.riova.R;
import com.example.riova.utilities.Constants;
import com.example.riova.utilities.PreferencesHandler;

public class JournalAppendFragment extends Fragment  {

    private PreferencesHandler preferencesHandler;
    private View mainView;
    private ImageView backButton;
    private ImageView doneButton;
    private EditText inputTitle;
    private EditText inputContent;

    /**
     * Constructs the main Fragment.
     */
    public JournalAppendFragment() {
        super(R.layout.journal_append);
    }

    /**
     *
     *
     * @param view The View returned by main.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        preferencesHandler = new PreferencesHandler(getContext());
        setListeners();
    }

    /**
     * Sets up all views.
     *
     * @param view The main View.
     */
    private void initializeViews(View view) {
        mainView = view;
        backButton = mainView.findViewById(R.id.journal_append_back);
        doneButton = mainView.findViewById(R.id.journal_append_done);
        inputTitle = mainView.findViewById(R.id.journal_append_title);
        inputContent = mainView.findViewById(R.id.journal_append_content);
    }

    /**
     * Initializes listeners.
     */
    private void setListeners() {
        backButton.setOnClickListener(v -> {
            resetJournalAppend();
            showPage(Constants.TAG_JOURNAL);
        });
        doneButton.setOnClickListener(v -> {
            saveJournal();
        });
    }

    /**
     * Resets JournalAppend Fragment
     */
    private void resetJournalAppend() {
        saveJournal();
        inputTitle.setText("");
        inputContent.setText("");
        doneButton.setVisibility(View.INVISIBLE);
    }

    /**
     * Checks if Journal was modified. If so, it saves the content.
     *
     * @return Returns true if entry was modified; false otherwise.
     */
    private boolean isModified() {
        return !(inputTitle.getText().toString().isEmpty()) ||
                !(inputContent.getText().toString().isEmpty());
    }

    /**
     * Saves all Journal data if journal entry has been modified..
     */
    private void saveJournal() {
        if (isModified()) {

        }
    }

    /**
     * Shows a Fragment.
     *
     * @param tag The tag of the fragment to be displayed.
     */
    private void showPage(String tag) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (String page_tag : Constants.page_tag_array) {
            transaction.hide(fragmentManager.findFragmentByTag(page_tag));
        }
        transaction.show(fragmentManager.findFragmentByTag(tag));
        transaction.commit();
    }

}
