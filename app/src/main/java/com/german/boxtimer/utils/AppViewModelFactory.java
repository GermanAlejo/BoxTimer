package com.german.boxtimer.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.german.boxtimer.repository.TimerConfigRepository;
import com.german.boxtimer.sound.SoundEffects;
import com.german.boxtimer.viewModel.MainTimerViewModel;
import com.german.boxtimer.viewModel.RunningTimerViewModel;


public class AppViewModelFactory implements ViewModelProvider.Factory {
    private final TimerConfigRepository repository;
    private SoundEffects soundEffects;
    private Long timerId;

    public AppViewModelFactory(TimerConfigRepository repository) {
        this.repository = repository;
    }

    public AppViewModelFactory(TimerConfigRepository repository, Long id, SoundEffects soundEffects) {
        this.repository = repository;
        this.timerId = id;
        this.soundEffects = soundEffects;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainTimerViewModel.class)) {
            return (T) new MainTimerViewModel(repository);
        }
        if (modelClass.isAssignableFrom(RunningTimerViewModel.class)) {
            return (T) new RunningTimerViewModel(repository, timerId, soundEffects);
        }
        throw new IllegalArgumentException("Unknown ViewModel: " + modelClass);
    }
}