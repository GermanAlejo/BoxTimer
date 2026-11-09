package com.german.boxtimer.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.german.boxtimer.entity.timer.TimerConfig;
import com.german.boxtimer.repository.TimerConfigRepository;
import com.german.boxtimer.utils.TimeUtils;
import timber.log.Timber;


public class MainTimerViewModel extends ViewModel {
    private static final String TAG = "MainTimerVM";
    private final TimerConfigRepository timerRepo;
    private final MutableLiveData<String> totalTimeText = new MutableLiveData<>();
    private final MutableLiveData<String> displayRoundNumber = new MutableLiveData<>();
    private final MutableLiveData<String> displayRestNumber = new MutableLiveData<>();
    private final MutableLiveData<String> displayNumRoundsNumber = new MutableLiveData<>();
    private TimerConfig timerConfig;
    //Flag to control if the config was changed
    private boolean isDirty = false;


    //first get default/last timer from database
    //call repository here in the constructor
    public MainTimerViewModel(TimerConfigRepository repo) {
        timerRepo = repo;
        timerRepo.loadInitial(loaded -> {
            Timber.tag(TAG).i("Loading timer config from repo");
            timerConfig = loaded;
            initializeDisplayData();                 // fills all display values once
        });
    }

    // function to initialize display elements
    private void initializeDisplayData() {
        //Calculate total time
        Long totalTime = TimeUtils.totalTimeMillis(timerConfig.getRoundTimer(), timerConfig.getRestTimer(), timerConfig.getRounds());

        //Set display values
        totalTimeText.postValue(TimeUtils.format(totalTime));
        displayRoundNumber.postValue(timerConfig.getRoundTimer().toString());
        displayRestNumber.postValue(timerConfig.getRoundTimer().toString());
        displayNumRoundsNumber.postValue(timerConfig.getRounds().toString());
    }

    // Exposed read-only: the Activity can observe but not change the values

    //get config timer for the activity class
    public final TimerConfig getTimerCurrentConfig() {
        return timerConfig;
    }

    //get display values
    public LiveData<String> getTotalTimeText() {
        return totalTimeText;
    }

    public LiveData<String> getRoundTime() {
        return displayRoundNumber;
    }

    public LiveData<String> getRestTime() {
        return displayRestNumber;
    }

    public LiveData<String> getNumRounds() {
        return displayNumRoundsNumber;
    }

    public void increaseRound() {
        final String time = timerConfig.increaseRoundTimer();
        isDirty = true;
        changeButtonStatus(displayRoundNumber, time);
    }

    public void decreaseRound() {
        final String time = timerConfig.decreaseRoundTimer();
        isDirty = true;
        changeButtonStatus(displayRoundNumber, time);
    }

    public void increaseRest() {
        final String time = timerConfig.increaseRestTimer();
        isDirty = true;
        changeButtonStatus(displayRestNumber, time);
    }

    public void decreaseRest() {
        final String time = timerConfig.decreaseRestTimer();
        isDirty = true;
        changeButtonStatus(displayRestNumber, time);
    }

    public void increaseNumRounds() {
        timerConfig.incrementRoundNum();
        isDirty = true;
        changeButtonStatus(displayNumRoundsNumber, timerConfig.getRounds().toString());
    }

    public void decreaseNumRounds() {
        timerConfig.decrementRoundNum();
        isDirty = true;
        changeButtonStatus(displayNumRoundsNumber, timerConfig.getRounds().toString());
    }

    private void changeButtonStatus(MutableLiveData<String> display, String value) {
        display.setValue(value);
        //Set the total with all changes
        totalTimeText.setValue(TimeUtils.format(timerConfig.getTotalTime()));
    }

    public void updateTimerData() {
        if (isDirty) {
            timerRepo.updateTimerConfig(timerConfig);
        }
        isDirty = false;
    }

    @Override
    protected void onCleared() {
        Timber.tag(TAG).i("Main View closed");
        if (isDirty) {
            timerRepo.updateTimerConfig(timerConfig);
        }
    }

}
