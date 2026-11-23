package com.example.riova.utilities;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.riova.models.Sound;

/**
 * Plays ambient sounds for meditation sessions.
 */
public class SoundPlayer {

    private static final float DEFAULT_VOLUME = 1.0f;


    private MediaPlayer mediaPlayer;

    private float volume;

    //Creates a SoundPlayer
    public SoundPlayer() {
        this.volume = DEFAULT_VOLUME;
    }

    /**
     * starts playing the sound on a loop
     *
     * @param context the context to load the audio resource
     * @param sound the sound to play
     * @return {@code true} if playback started, {@code false} if the audio
     *         could not be loaded
     */
    public boolean play(Context context, Sound sound) {
        stop();
        mediaPlayer = MediaPlayer.create(context, sound.getResourceId());
        if (mediaPlayer == null) {
            return false;
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(volume, volume);
        mediaPlayer.start();
        return true;
    }

    //pauses the current sound
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    // resumes the sound
    public void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    // stops the current sound
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * Sets the playback volume
     *
     * @param volume the new volume
     */
    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(this.volume, this.volume);
        }
    }

    // returns whether a sound is playing or not.
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }
}

