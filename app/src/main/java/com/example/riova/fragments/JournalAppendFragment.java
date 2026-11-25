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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class JournalAppendFragment extends Fragment {

    private PreferencesHandler preferencesHandler;
    private View mainView;
    private ImageView backButton;
    private ImageView doneButton;
    private EditText inputTitle;
    private EditText inputContent;
    private FirebaseFirestore database;
    private String journalId = "";

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
        database = FirebaseFirestore.getInstance();
        journalId = preferencesHandler.getString(Constants.KEY_JOURNAL_ID_APPEND);
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
            saveJournal();
            showPage(Constants.TAG_JOURNAL);
            resetJournalAppend();
            preferencesHandler.putString(Constants.KEY_JOURNAL_ID_APPEND, "");
        });
        doneButton.setOnClickListener(v -> {
            resetTextFocuses();
            showDoneButton(false);
            saveJournal();
        });
        inputTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                inputTitle.setCursorVisible(true);
                showDoneButton(true);
            } else {
                inputTitle.setCursorVisible(false);
            }
        });
        inputContent.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                inputContent.setCursorVisible(true);
                showDoneButton(true);
            }  else {
                inputContent.setCursorVisible(false);
            }
        });
    }

    /**
     * Resets JournalAppend Fragment
     */
    private void resetJournalAppend() {
        inputTitle.setText("");
        inputContent.setText("");
        resetTextFocuses();
    }

    /**
     * Resets all focuses and hides done button
     */
    private void resetTextFocuses() {
        inputTitle.clearFocus();
        inputContent.clearFocus();
        showDoneButton(false);
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
     * Handles showing and hiding complete button.
     *
     * @param show The show status.
     */
    private void showDoneButton(boolean show) {
        if (show) {
            doneButton.setVisibility(View.VISIBLE);
        } else {
            doneButton.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Saves all Journal data if journal entry has been modified.
     */
    private void saveJournal() {
        if (isModified()) {

            if (journalId.isEmpty()) {
                HashMap<String, Object> journalEntry = new HashMap<>();
                journalEntry.put(Constants.KEY_JOURNAL_OWNER_ID, preferencesHandler
                        .getString(Constants.KEY_USER_ID));
                journalEntry.put(Constants.KEY_JOURNAL_TITLE, inputTitle.getText().toString());
                journalEntry.put(Constants.KEY_JOURNAL_CONTENT, inputContent.getText().toString());
                journalEntry.put(Constants.KEY_JOURNAL_TIMESTAMP, new Date());
                database.collection(Constants.KEY_COLLECTION_JOURNALS).add(journalEntry)
                        .addOnSuccessListener(documentReference -> {
                    journalId = documentReference.getId();
                });

            } else {
                HashMap<String, Object> journalEntry = new HashMap<>();
                journalEntry.put(Constants.KEY_JOURNAL_OWNER_ID, preferencesHandler
                        .getString(Constants.KEY_USER_ID));
                journalEntry.put(Constants.KEY_JOURNAL_TITLE, inputTitle.getText().toString());
                journalEntry.put(Constants.KEY_JOURNAL_CONTENT, inputContent.getText().toString());
                journalEntry.put(Constants.KEY_JOURNAL_TIMESTAMP, new Date());
                database.collection(Constants.KEY_COLLECTION_JOURNALS)
                        .document(journalId).set(journalEntry);
            }
        }
    }

    /**
     * Retrieves all journal information from ID.
     */
    private void retrieveJournal() {
        journalId = preferencesHandler.getString(Constants.KEY_JOURNAL_ID_APPEND);
        if (!preferencesHandler.getString(Constants.KEY_JOURNAL_ID_APPEND).isEmpty()) {
            DocumentReference journalReference = database.collection(Constants.KEY_COLLECTION_JOURNALS)
                    .document(journalId);
            journalReference.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    inputTitle.setText(documentSnapshot.getString(Constants.KEY_JOURNAL_TITLE));
                    inputContent.setText(documentSnapshot.getString(Constants.KEY_JOURNAL_CONTENT));
                }
            });
        }
    }

    /**
     * Shows a specified Fragment.
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

    /**
     * Listens for this Fragment's hidden status change.
     *
     * @param hidden True if the fragment is now hidden, false otherwise.
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            resetJournalAppend();
            retrieveJournal();
        }
    }
}
