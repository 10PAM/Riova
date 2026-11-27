package com.example.riova.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.riova.R;
import com.example.riova.models.Sound;
import com.example.riova.utilities.MeditationTimer;
import com.example.riova.utilities.SoundPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * The Meditation page. Lets the user choose an ambient sound from a list and
 * type a session length in minutes, then plays the sound on a loop while a
 * countdown runs. The session can be paused, resumed, or stopped, and the
 * sound stops automatically when the countdown reaches zero.
 */
public class MeditationFragment extends Fragment implements MeditationTimer.Listener {

    private static final long MILLIS_PER_MINUTE = 60_000L;
    private static final int DEFAULT_MINUTES = 5;
    private static final int MAX_MINUTES = 180;
    private static final float LOCKED_LIST_ALPHA = 0.5f;
    private static final String PREFS = "riova_prefs";
    private static final String KEY_VOLUME = "master_volume";
    private static final int DEFAULT_VOLUME_PERCENT = 50;
    private static final float MAX_VOLUME_PERCENT = 100f;
    private ListView soundList;
    private EditText durationInput;
    private TextView timerText;
    private Button startPauseButton;
    private Button stopButton;
    private List<Sound> sounds;
    private int selectedSoundIndex;
    private MeditationTimer timer;
    private SoundPlayer soundPlayer;

    /**
     * Creates the fragment.
     */
    public MeditationFragment() {
        super(R.layout.meditation);
    }

    /**
     * Connects the layout's views to their behavior once the view exists.
     *
     * @param view the fragment's root view
     * @param savedInstanceState the previously saved state, if any
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        soundList = view.findViewById(R.id.meditation_sound_list);
        durationInput = view.findViewById(R.id.meditation_duration_input);
        timerText = view.findViewById(R.id.meditation_timer_text);
        startPauseButton = view.findViewById(R.id.meditation_start_pause_button);
        stopButton = view.findViewById(R.id.meditation_stop_button);

        sounds = createSounds();
        timer = new MeditationTimer(this);
        soundPlayer = new SoundPlayer();
        applyMasterVolume();
        setUpSoundList();
        setUpDurationInput();
        startPauseButton.setOnClickListener(button -> onStartPauseClicked());
        stopButton.setOnClickListener(button -> stopSession());

        updateControls();
    }

    /**
     * Pauses the session when the user switches to another page. Applies the master volume.
     *
     * @param hidden true if the page was just hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (timer == null) {
            return;
        }
        if (hidden) {
            if (timer.isRunning()) {
                pauseSession();
            }
        } else {
            applyMasterVolume();
        }
    }

    /**
     * Pauses the session when the user leaves the app.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (timer != null && timer.isRunning()) {
            pauseSession();
        }
    }

    /**
     * Stops the timer and releases the audio resources.
     */
    @Override
    public void onDestroyView() {
        timer.cancel();
        soundPlayer.stop();
        super.onDestroyView();
    }

    /**
     * Updates the countdown display every second.
     *
     * @param millisRemaining the time left in the session
     */
    @Override
    public void onTick(long millisRemaining) {
        showTime(millisRemaining);
    }

    /**
     * Stops the sound and tells the user the session is complete.
     */
    @Override
    public void onFinish() {
        soundPlayer.stop();
        showTime(0L);
        updateControls();
        Toast.makeText(requireContext(), R.string.meditation_complete, Toast.LENGTH_SHORT).show();
    }

    /**
     * Builds the list of ambient sounds available on this page.
     *
     * @return the list of sounds
     */
    private List<Sound> createSounds() {
        List<Sound> list = new ArrayList<>();
        list.add(new Sound("Rain", "Soft steady rainfall", R.raw.rain));
        list.add(new Sound("Ocean", "Gentle waves on the shore", R.raw.ocean));
        list.add(new Sound("Forest", "Birds and rustling leaves", R.raw.forest));
        return list;
    }

    /**
     * Fills the sound list with one sound per row, showing its name and description.
     */
    private void setUpSoundList() {
        ArrayAdapter<Sound> adapter = new ArrayAdapter<Sound>(
                requireContext(), R.layout.meditation_sound_item, sounds) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View row = convertView;
                if (row == null) {
                    row = LayoutInflater.from(getContext())
                            .inflate(R.layout.meditation_sound_item, parent, false);
                }
                Sound sound = sounds.get(position);
                TextView name = row.findViewById(R.id.meditation_sound_name);
                TextView description = row.findViewById(R.id.meditation_sound_description);
                name.setText(sound.getName());
                description.setText(sound.getDescription());
                return row;
            }
        };
        soundList.setAdapter(adapter);

        selectedSoundIndex = 0;
        soundList.setItemChecked(selectedSoundIndex, true);

        soundList.setOnItemClickListener((parent, row, position, id) -> {
            if (isSessionActive()) {
                soundList.setItemChecked(selectedSoundIndex, true);
                return;
            }
            selectedSoundIndex = position;
        });
    }

    /**
     * Sets the default duration and updates the countdown display.
     */
    private void setUpDurationInput() {
        durationInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                // Not needed: the display only updates after the text changes.
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                // Not needed: the display only updates after the text changes.
            }

            @Override
            public void afterTextChanged(Editable text) {
                showTime(readMinutes() * MILLIS_PER_MINUTE);
            }
        });
        durationInput.setText(String.valueOf(DEFAULT_MINUTES));
    }

    /**
     * Starts, pauses, or resumes the session.
     */
    private void onStartPauseClicked() {
        if (timer.isRunning()) {
            pauseSession();
        } else if (timer.isPaused()) {
            resumeSession();
        } else {
            startSession();
        }
    }

    /**
     * Starts a new session with the selected sound and the selected duration.
     */
    private void startSession() {
        int minutes = readMinutes();
        if (minutes == 0) {
            durationInput.setError(getString(R.string.meditation_duration_error));
            return;
        }

        Sound sound = sounds.get(selectedSoundIndex);
        if (!soundPlayer.play(requireContext(), sound)) {
            Toast.makeText(requireContext(), R.string.meditation_sound_error, Toast.LENGTH_SHORT).show();
            return;
        }

        hideKeyboard();
        timer.start(minutes * MILLIS_PER_MINUTE);
        updateControls();
    }

    /**
     * Pauses both the countdown and the sound.
     */
    private void pauseSession() {
        timer.pause();
        soundPlayer.pause();
        updateControls();
    }

    /**
     * Resumes both the countdown and the sound.
     */
    private void resumeSession() {
        timer.resume();
        soundPlayer.resume();
        updateControls();
    }

    /**
     * Stops the session and resets the duration display.
     */
    private void stopSession() {
        timer.cancel();
        soundPlayer.stop();
        showTime(readMinutes() * MILLIS_PER_MINUTE);
        updateControls();
    }

    /**
     * Reads the master volume saved by the Settings page and applies it to the sound player
     */
    private void applyMasterVolume() {
        SharedPreferences prefs =
                requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        int percent = prefs.getInt(KEY_VOLUME, DEFAULT_VOLUME_PERCENT);
        soundPlayer.setVolume(percent / MAX_VOLUME_PERCENT);
    }

    /**
     * Returns whether a session is in progress, either running or paused.
     *
     * @return true if a session is running or paused
     */
    private boolean isSessionActive() {
        return timer.isRunning() || timer.isPaused();
    }

    /**
     * Reads the number of minutes typed in the duration field.
     *
     * @return the minutes entered
     */
    private int readMinutes() {
        String text = durationInput.getText().toString().trim();
        if (text.isEmpty()) {
            return 0;
        }
        try {
            int minutes = Integer.parseInt(text);
            return (minutes >= 1 && minutes <= MAX_MINUTES) ? minutes : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Hides the keyboard after the user starts a session.
     */
    private void hideKeyboard() {
        InputMethodManager keyboard = (InputMethodManager)
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (keyboard != null) {
            keyboard.hideSoftInputFromWindow(durationInput.getWindowToken(), 0);
        }
        durationInput.clearFocus();
    }

    /**
     * Shows the given time in the countdown display.
     *
     * @param millis the time to show, in milliseconds
     */
    private void showTime(long millis) {
        timerText.setText(MeditationTimer.formatTime(millis));
    }

    /**
     * Updates the button label to pause, resume, or start.
     */
    private void updateControls() {
        boolean active = isSessionActive();

        if (timer.isRunning()) {
            startPauseButton.setText(R.string.meditation_pause);
        } else if (timer.isPaused()) {
            startPauseButton.setText(R.string.meditation_resume);
        } else {
            startPauseButton.setText(R.string.meditation_start);
        }

        stopButton.setEnabled(active);
        durationInput.setEnabled(!active);
        soundList.setAlpha(active ? LOCKED_LIST_ALPHA : 1.0f);
    }
}