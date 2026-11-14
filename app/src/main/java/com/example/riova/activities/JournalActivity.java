package com.example.riova.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riova.R;
import com.example.riova.adapters.JournalAdapter;
import com.example.riova.listeners.JournalListener;
import com.example.riova.utilities.Constants;
import com.example.riova.utilities.PreferencesHandler;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the user journaling functionality.
 */
public class JournalActivity extends Fragment implements JournalListener {

    public String title;
    public String date;
    public String content;
    public String id;

    private PreferencesHandler preferencesHandler;

    public boolean isSelected = false;
    View mainView;

    public JournalActivity() {
        super(R.layout.journal);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        preferencesHandler = new PreferencesHandler(getContext());
    }

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
    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS).get()
                .addOnCompleteListener(task ->{
                    loading(false);
                    String currentUserId = preferencesHandler.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<JournalActivity> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            JournalActivity journal = new JournalActivity();
                            journal.title = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            journal.date = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            journal.content = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            journal.id = queryDocumentSnapshot.getId();
                            users.add(journal);
                        }
                        if (!users.isEmpty()) {
                            JournalAdapter usersAdapter = new JournalAdapter(users, this);
                            RecyclerView journalRecycler = mainView.findViewById(R.id.journal_recycler_view);
                            journalRecycler.setAdapter(usersAdapter);
                            journalRecycler.setVisibility(View.VISIBLE);
                        } else {
                            showToast("Error retrieving other users.");
                        }
                    } else {
                        showToast("Error retrieving other users...");
                    }
                });
    }

    private void loading(boolean p_loading) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {

    }

    @Override
    public void JournalClicked(JournalActivity journal) {

    }
}