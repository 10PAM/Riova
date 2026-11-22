package com.example.riova.listeners;

import com.example.riova.models.Journal;

/**
 * Interface handling Journal listening.
 */
public interface JournalListener {

    /**
     * Defines when a Journal is pressed.
     *
     * @param p_journal The journal item pressed.
     */
    void onJournalClicked(Journal p_journal);

    /**
     * Defines when a Journal is long-pressed.
     *
     * @param p_journal The journal item pressed.
     */
    void onJournalLongClicked(Journal p_journal);
}
