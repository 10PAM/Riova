package com.example.riova.listeners;

import com.example.riova.activities.JournalActivity;

/**
 * Interface handling Journal listening.
 */
public interface JournalListener {

    /**
     * Defines when a Journal is pressed.
     *
     * @param journal The journal item pressed.
     */
    void JournalClicked(JournalActivity journal);
}
