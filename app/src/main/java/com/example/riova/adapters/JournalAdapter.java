package com.example.riova.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riova.R;
import com.example.riova.listeners.JournalListener;
import com.example.riova.models.Journal;

import org.jetbrains.annotations.UnknownNullability;

import java.util.List;

/**
 * Creates an Adapter for the RecyclerView's handling
 * of journal entries.
 */
public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalsViewHolder> {

    private final List<Journal> journals;
    private final JournalListener journalListener;

    /**
     * Constructs the Journal Adapter.
     *
     * @param p_journals The Journals being applied.
     * @param p_journalListener The JournalListener object being applied.
     */
    public JournalAdapter(List<Journal> p_journals, JournalListener p_journalListener) {
        this.journals = p_journals;
        this.journalListener = p_journalListener;
    }

    /**
     * Constructor for creating the Journal's ViewHolder.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return Returns a new ViewHolder.
     */
    @NonNull
    @Override
    public JournalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JournalsViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.journal,
                parent,
                false
        ));
    }

    /**
     * Binds Journal.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull JournalAdapter.JournalsViewHolder holder, int position) {
        holder.bindJournals(journals.get(position));
    }

    @Override
    public int getItemCount() {
        return journals.size();
    }

    /**
     * Sub-class; Handles Poster viewing.
     */
    class JournalsViewHolder extends RecyclerView.ViewHolder{

        View mainView;

        /**
         * Constructs the ViewHolder for journals
         *
         * @param itemView The main View context.
         */
        public JournalsViewHolder(@NonNull View itemView) {
            super(itemView);
            mainView = itemView;
        }

        /**
         * Applies Journal data to View.
         *
         * @param p_journal The Journal view.
         */
        private void setJournalData(Journal p_journal) {
            LinearLayout journalView = mainView.findViewById(R.id.banner_journal);

            TextView journalTitle = journalView.findViewById(R.id.journal_title);
            TextView journalDate = journalView.findViewById(R.id.journal_date);
            TextView journalContentBrief = journalView.findViewById(R.id.journal_content_brief);

            journalTitle.setText(p_journal.title);
            journalDate.setText(p_journal.date);
            journalContentBrief.setText(p_journal.content);

            mainView.setOnClickListener(v -> {
                journalListener.onJournalClicked(p_journal);
            });

        }

        /**
         * Manages journal binding.
         *
         * @param journal The journals.
         */
        void bindJournals(final @UnknownNullability Journal journal){

        }

    }
}
