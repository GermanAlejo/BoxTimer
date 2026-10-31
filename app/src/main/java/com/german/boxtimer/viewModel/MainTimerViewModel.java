package com.german.boxtimer.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.german.boxtimer.entity.timer.TimerConfig;
import com.german.boxtimer.repository.TimerConfigRepository;


public class MainTimerViewModel extends ViewModel {

    private TimerConfigRepository timerRepo;
    private final MutableLiveData<Integer> displayRoundNumber = new MutableLiveData<>();
    private final MutableLiveData<Integer> displayRestNumber = new MutableLiveData<>();
    private final MutableLiveData<Integer> displayNumRoundsNumber = new MutableLiveData<>();
    private TimerConfig timerConfig;


    //first get default/last timer from database
    //call repository here in the constructor
    public MainTimerViewModel(TimerConfigRepository repo) {
        timerRepo = repo;
        timerRepo.loadInitial(loaded -> {
            timerConfig = loaded;
            initializeDisplayData();                 // fills all display values once
        });
    }

    // function to initialize display elements
    private void initializeDisplayData() {
        displayRoundNumber.postValue(timerConfig.getRoundDuration());
        displayRestNumber.postValue(timerConfig.getRestDuration());
        displayNumRoundsNumber.postValue(timerConfig.getRounds());
    }

    // Exposed read-only: the Activity can observe but not change the values

    //get config timer for the activity class
    public final TimerConfig getTimerCurrentConfig() {
        return timerConfig;
    }

    //get display values
    public LiveData<Integer> getRoundTime() {
        return displayRoundNumber;
    }
    public LiveData<Integer> getRestTime() {
        return displayRestNumber;
    }

    public LiveData<Integer> getNumRounds() {
        return displayNumRoundsNumber;
    }

    public void increaseRound() {
        timerConfig.incrementRound();
        changeButtonStatus(displayRoundNumber, 1, 1);
    }

    public void decreaseRound() {
        timerConfig.decrementRound();
        changeButtonStatus(displayRoundNumber, -1, 0);
    }

    public void increaseRest() {
        timerConfig.incrementRest();
        changeButtonStatus(displayRestNumber, 1, 0);
    }

    public void decreaseRest() {
        timerConfig.decrementRest();
        changeButtonStatus(displayRestNumber, -1, 1);
    }

    public void increaseNumRounds() {
        timerConfig.incrementRoundNum();
        changeButtonStatus(displayNumRoundsNumber, 1, 1);
    }

    public void decreaseNumRounds() {
        timerConfig.decrementRoundNum();
        changeButtonStatus(displayNumRoundsNumber, -1, 1);
    }

    private void changeButtonStatus(MutableLiveData<Integer> display, Integer change, Integer minVal) {
        Integer currentValue = display.getValue();
        Integer value = (currentValue == null ? minVal : currentValue) + change;
        display.setValue(value);
    }

    @Override
    protected void onCleared() {
        //Log here
        timerRepo.saveTimerConfig(timerConfig);
    }

}
