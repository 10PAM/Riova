package com.example.riova.utilities;

import android.os.CountDownTimer;

import java.util.Locale;

/**
 * A countdown timer for meditation sessions that supports pausing and resuming.
 */
public class MeditationTimer {

    //refreshes the countdown display and stops it when the session ends
    public interface Listener {

        void onTick(long millisRemaining);

        void onFinish();
    }

    private static final long TICK_INTERVAL_MILLIS = 1000L;

    private final Listener listener;

    private CountDownTimer countDownTimer;

    private long remainingMillis;

    private boolean running;

    //creates a timer
    public MeditationTimer(Listener listener) {
        this.listener = listener;
        this.remainingMillis = 0L;
        this.running = false;
    }

    /**
     * Starts a new session and replaces any other session
     *
     *
     * @param durationMillis the duration of the session
     *
     * @throws IllegalArgumentException if the duration is zero or negative
     */
    public void start(long durationMillis) {
        if (durationMillis <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero");
        }
        cancel();
        remainingMillis = durationMillis;
        runCountdown();
    }

    //pauses the session
    public void pause() {
        if (!running) {
            return;
        }
        countDownTimer.cancel();
        countDownTimer = null;
        running = false;
    }

    // resumes the session
    public void resume() {
        if (running || remainingMillis <= 0) {
            return;
        }
        runCountdown();
    }

    //stops the session and clears it
    public void cancel() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        remainingMillis = 0L;
        running = false;
    }

    //returns whether the countdown is currently running or not
    public boolean isRunning() {
        return running;
    }

    //returns whether the session is paused
    public boolean isPaused() {
        return !running && remainingMillis > 0;
    }

    //returns the time left in the current session
    public long getRemainingMillis() {
        return remainingMillis;
    }

    //formats the duration to minutes and seconds
    public static String formatTime(long millis) {
        long totalSeconds = Math.max(0L, millis) / 1000L;
        long minutes = totalSeconds / 60L;
        long seconds = totalSeconds % 60L;
        //guarantees format is correct for minutes and seconds
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    // Creates and starts a countdown
    private void runCountdown() {
        countDownTimer = new CountDownTimer(remainingMillis, TICK_INTERVAL_MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingMillis = millisUntilFinished;
                listener.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                remainingMillis = 0L;
                running = false;
                countDownTimer = null;
                listener.onFinish();
            }
        };
        running = true;
        countDownTimer.start();
    }
}
