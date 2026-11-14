package com.example.riova.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import com.example.riova.R;

public class MeditationActivity extends Fragment {


    public MeditationActivity(){
        super(R.layout.meditation);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}