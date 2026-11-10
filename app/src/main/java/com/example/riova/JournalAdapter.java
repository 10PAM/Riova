package com.example.riova;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Creates an Adapter for the RecyclerView's handling
 * of journal entries.
 */
public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalsViewHolder> {

    private List<Journal> journals;

    public JournalAdapter() {

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
        return 0;
    }

    /**
     * Sub-class; Handles Poster viewing.
     */
    class JournalsViewHolder extends RecyclerView.ViewHolder{

        public JournalsViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        /**
         * Manages journal binding.
         *
         * @param journal The journals.
         */
        void bindJournals(final Journal journal){


            journal.setOnClickListener(new View.OnClickListener() {
                /**
                 * Manages clicking for the Journals.
                 *
                 * @param view The view that was clicked.
                 */
                @Override
                public void onClick(View view) {
                    if(journal.isSelected){
                    }else{

                    }
                }
            });

        }

    }
}
