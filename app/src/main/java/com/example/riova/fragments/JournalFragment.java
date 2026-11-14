package com.example.riova.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riova.R;
import com.example.riova.adapters.JournalAdapter;
import com.example.riova.listeners.JournalListener;
import com.example.riova.models.Journal;
import com.example.riova.utilities.Constants;
import com.example.riova.utilities.PreferencesHandler;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the user journaling functionality.
 */
public class JournalFragment extends Fragment implements JournalListener {

    private PreferencesHandler preferencesHandler;
    public boolean isSelected = false;
    View mainView;

    public JournalFragment() {
        super(R.layout.journal);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        preferencesHandler = new PreferencesHandler(getContext());
        getJournals();
        setListeners();
    }

    /**
     * Initializes listeners.
     */
    private void setListeners() {
        Button addJournal = mainView.findViewById(R.id.button_addJournal);
        addJournal.setOnClickListener(v -> {
            showPage(Constants.TAG_JOURNAL_APPEND);
        });
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

    /*
    private void addJournal() {
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_high, new JournalAppendActivity(), Constants.TAG_JOURNAL_APPEND)
                .addToBackStack(null)
                .commit();
    }*/

    /**
     * Displays a Toast message with a specified string. Used for visual feedback on the user end.
     *
     * @param message The message being displayed to the user.
     */
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Gets users from Firebase excluding current user. Saves into List, and appends to Adapter.
     */
    private void getJournals() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_JOURNALS).get()
                .addOnCompleteListener(task ->{
                    loading(false);
                    String currentUserId = preferencesHandler.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Journal> journals = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            Journal journal = new Journal();
                            journal.title = queryDocumentSnapshot.getString(Constants.KEY_JOURNAL_TITLE);
                            journal.date = queryDocumentSnapshot.getString(Constants.KEY_JOURNAL_DATE);
                            journal.content = queryDocumentSnapshot.getString(Constants.KEY_JOURNAL_CONTENT);
                            journal.id = queryDocumentSnapshot.getId();
                            journals.add(journal);
                        }
                        if (!journals.isEmpty()) {
                            JournalAdapter usersAdapter = new JournalAdapter(journals, this);
                            RecyclerView journalRecycler = mainView.findViewById(R.id.journal_recycler_view);
                            journalRecycler.setAdapter(usersAdapter);
                            journalRecycler.setVisibility(View.VISIBLE);
                        } else {
                            //showToast("Error retrieving journals.");
                        }
                    } else {
                        //showToast("Error retrieving journals...");
                    }
                });
    }

    private void loading(boolean p_loading) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onJournalClicked(Journal p_journal) {

    }
}