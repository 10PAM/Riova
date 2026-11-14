package com.example.riova.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This class handles the user journaling functionality.
 */
public class JournalFragment extends Fragment implements JournalListener {

    private PreferencesHandler preferencesHandler;
    private List<Journal> journals;
    private JournalAdapter journalAdapter;
    private RecyclerView journalRecycler;
    View mainView;
    private FirebaseFirestore database;

    public JournalFragment() {
        super(R.layout.journal);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        initialize();
    }

    /**
     * Initializes Journal Fragment.
     */
    private void initialize() {
        journals = new ArrayList<Journal>();
        preferencesHandler = new PreferencesHandler(getContext());
        database = FirebaseFirestore.getInstance();
        journalAdapter = new JournalAdapter(journals, this);
        journalRecycler = mainView.findViewById(R.id.journal_recycler_view);
        journalRecycler.setAdapter(journalAdapter);
        //getJournals();
        setListeners();
        listenForJournals();
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

    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = journals.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    Journal journal = new Journal();
                    journal.ownerId = documentChange.getDocument()
                            .getString(Constants.KEY_JOURNAL_OWNER_ID);
                    journal.title = documentChange.getDocument()
                            .getString(Constants.KEY_JOURNAL_TITLE);
                    journal.content = documentChange.getDocument()
                            .getString(Constants.KEY_JOURNAL_CONTENT);
                    journal.dateTime = getReadableDataTime(documentChange.getDocument()
                            .getDate(Constants.KEY_JOURNAL_TIMESTAMP));
                    journal.date = documentChange.getDocument()
                            .getDate(Constants.KEY_JOURNAL_TIMESTAMP);
                    journals.add(journal);
                }
            }
            //Collections.sort(journals, (obj1, obj2) -> obj1.date.compareTo(obj2.date));
            journals.sort(Collections.reverseOrder((obj1, obj2) -> obj1.date.compareTo(obj2.date)));
            if (count == 0) {
                journalAdapter.notifyDataSetChanged();
            } else {
                journalAdapter.notifyItemRangeChanged(journals.size(), journals.size());
                journalRecycler.smoothScrollToPosition(0);
            }
            journalRecycler.setVisibility(View.VISIBLE);
        }
        //progressbar.setVisibility(View.GONE);
    });

    /*
     * Gets users from Firebase excluding current user. Saves into List, and appends to Adapter.

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
                            journal.dateTime = queryDocumentSnapshot.getString(Constants.KEY_JOURNAL_DATE);
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
    }*/

    /**
     * Tests
     * @param p_loading
     */
    private void loading(boolean p_loading) {

    }


    /**
     * Listens for any incoming User messages.
     */
    private void listenForJournals() {
        database.collection(Constants.KEY_COLLECTION_JOURNALS)
                .whereEqualTo(Constants.KEY_JOURNAL_OWNER_ID, preferencesHandler.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    /**
     * Formats message Time and Data in format: Month day, Year - Hour, Minute
     *
     * @param p_date The Date object.
     * @return Returns the formatted date.
     */
    private String getReadableDataTime(Date p_date) {
        if (p_date != null) {
            return new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())
                    .format(p_date);
        }
        else {
            return "DATE";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onJournalClicked(Journal p_journal) {

    }
}