package com.example.riova.listeners;

import com.example.riova.models.Journal;

/**
 * Interface handling Journal listening.
 */
public interface JournalListener {

    /**
     * Defines when a Journal is pressed.
     *
     * @param journal The journal item pressed.
     */
    void onJournalClicked(Journal journal);
}
