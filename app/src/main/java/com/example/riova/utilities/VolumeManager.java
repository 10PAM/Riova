/*
Programmer (Team Novelty) {
    - Fulya Bilgin, CWU Student ID: 50593580
}
File: VolumeManager.java
About: Single place to read the user's saved "master volume" setting (0-100 in
       User Settings) so any sound the app plays can honor it. The Settings page
       already persists and syncs the value; this class is the integration point
       that playback code reads from.

       Usage (for the meditation SoundPlayer / any MediaPlayer):
           VolumeManager.applyTo(mediaPlayer, context);
       or read the scalar directly:
           float v = VolumeManager.getVolumeScalar(context); // 0.0f - 1.0f
           mediaPlayer.setVolume(v, v);

       Note: there is no audio on this branch yet, so the audible effect lands
       once the meditation audio work is merged and calls this class.
*/

package com.example.riova.utilities;

import android.content.Context;
import android.media.MediaPlayer;

public class VolumeManager {

    // Matches the default used by the Settings volume SeekBar.
    private static final int DEFAULT_VOLUME = 50;

    /**
     * @param context any context.
     * @return the saved master volume as a percentage (0-100).
     */
    public static int getVolumePercent(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getInt(Constants.KEY_VOLUME, DEFAULT_VOLUME);
    }

    /**
     * @param context any context.
     * @return the saved master volume as a 0.0-1.0 scalar, ready for
     *         {@link MediaPlayer#setVolume(float, float)}.
     */
    public static float getVolumeScalar(Context context) {
        return getVolumePercent(context) / 100f;
    }

    /**
     * Applies the saved master volume to a MediaPlayer. No-op if the player is
     * null.
     *
     * @param player  the player to adjust.
     * @param context any context.
     */
    public static void applyTo(MediaPlayer player, Context context) {
        if (player != null) {
            float volume = getVolumeScalar(context);
            player.setVolume(volume, volume);
        }
    }
}
