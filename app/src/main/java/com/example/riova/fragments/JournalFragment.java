package com.example.riova.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    /**
     * Constructs Journal Fragment.
     */
    public JournalFragment() {
        super(R.layout.journal);
    }

    /**
     * Detects when Fragment is created.
     *
     * @param view The View returned by main.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        initialize();
    }

    /**
     * Initializes Journal Fragment.
     */
    private void initialize() {
        journals = new ArrayList<>();
        preferencesHandler = new PreferencesHandler(getContext());
        database = FirebaseFirestore.getInstance();
        journalAdapter = new JournalAdapter(journals, this);
        journalRecycler = mainView.findViewById(R.id.journal_recycler_view);
        journalRecycler.setAdapter(journalAdapter);
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
                    journal.journalId = documentChange.getDocument().getId();
                    journals.add(journal);
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    for (int i = 0; i < journals.size(); ++i) {
                        Journal journal = journals.get(i);
                        if (journal.journalId.equals(preferencesHandler.getString(Constants.KEY_JOURNAL_ID_APPEND))) {
                            journal.title = documentChange.getDocument()
                                    .getString(Constants.KEY_JOURNAL_TITLE);
                            journal.content = documentChange.getDocument()
                                    .getString(Constants.KEY_JOURNAL_CONTENT);
                            journal.dateTime = getReadableDataTime(documentChange.getDocument()
                                    .getDate(Constants.KEY_JOURNAL_TIMESTAMP));
                            journal.date = documentChange.getDocument()
                                    .getDate(Constants.KEY_JOURNAL_TIMESTAMP);
                            journal.journalId = documentChange.getDocument().getId();
                            preferencesHandler.putString(Constants.KEY_JOURNAL_ID_APPEND, journal.id);
                            journalAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }
            journals.sort(Collections
                    .reverseOrder((obj1, obj2) -> obj1.date.compareTo(obj2.date)));
            if (count == 0) {
                journalAdapter.notifyDataSetChanged();
            } else {
                journalAdapter.notifyItemRangeChanged(journals.size(), journals.size());
                journalRecycler.smoothScrollToPosition(0);
            }
            journalRecycler.setVisibility(View.VISIBLE);
        }
    });


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
        return "DATE";
    }

    /**
     * Listens for View destroyal.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onJournalClicked(Journal p_journal) {
        preferencesHandler.putString(Constants.KEY_JOURNAL_ID_APPEND, p_journal.journalId);
        showPage(Constants.TAG_JOURNAL_APPEND);
    }
}