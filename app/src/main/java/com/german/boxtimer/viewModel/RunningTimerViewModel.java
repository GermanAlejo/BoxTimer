package com.german.boxtimer.viewModel;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.german.boxtimer.entity.timer.TimerConfig;
import com.german.boxtimer.repository.TimerConfigRepository;
import com.german.boxtimer.utils.TimeConstants;
import com.german.boxtimer.utils.TimeUtils;
import timber.log.Timber;


import static com.german.boxtimer.utils.TimeUtils.format;
import static com.german.boxtimer.utils.TimeUtils.formatNumRounds;


public class RunningTimerViewModel extends ViewModel {
    private static final String TAG = "RunningTimerVM";
    private final MutableLiveData<Boolean> running = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> resting = new MutableLiveData<>(false);
    private final MutableLiveData<String> totalTimeText = new MutableLiveData<>();
    private final MutableLiveData<String> runningTimerText = new MutableLiveData<>();
    private final MutableLiveData<String> numRoundsLeftText = new MutableLiveData<>();
    private final MutableLiveData<String> restTimeLeftText = new MutableLiveData<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private TimerConfig timerConfig;
    private boolean isPaused = false;
    private long pauseTimeMillis;
    private long endTimeMillis;
    private long restEndTimeMillis;
    private int roundNumber;
    private int currentRound;

    public RunningTimerViewModel(TimerConfigRepository repo, Long timerId) {
        Timber.tag(TAG).i("Creating Running timer VM");
        repo.getTimerConfigById(timerId, loaded -> {
            if (loaded == null) {
                Timber.tag(TAG).w("Timer not found, cancelling");
                return;
            }
            timerConfig = loaded;
            initializeDisplayData();
        });
    }

    private void initializeDisplayData() {
        //calulate timer times in millis
        restEndTimeMillis = timerConfig.getRestTimer().calculateMillis();
        Long timeMillis = timerConfig.getRoundTimer().calculateMillis();
        Long restMillis = timerConfig.getRestTimer().calculateMillis();
        Long totalTime = TimeUtils.totalTimeMillis(timerConfig.getRoundTimer(), timerConfig.getRestTimer(), timerConfig.getRounds());

        //save future date (finish date) we calculate agains that
        endTimeMillis = SystemClock.elapsedRealtime() + timeMillis;

        //set up round numbers
        roundNumber = timerConfig.getRounds();
        currentRound = 1;
        //Set up text values
        String time = TimeUtils.format(timeMillis);
        String restTime = TimeUtils.format(restMillis);
        runningTimerText.postValue(time);
        restTimeLeftText.postValue(restTime);
        numRoundsLeftText.postValue(TimeUtils.formatNumRounds(currentRound, roundNumber));
        totalTimeText.postValue(TimeUtils.format(totalTime));
    }

    private final Runnable tickTimerTask = new Runnable() {
        @Override
        public void run() {
            Timber.tag(TAG).i("Starting round clock timer");
            //check whether we are counting round or rest
            long remaining;
            MutableLiveData<String> timeText;
            if(Boolean.TRUE.equals(isResting().getValue())) {
                remaining = restEndTimeMillis - SystemClock.elapsedRealtime();
                timeText = restTimeLeftText;
            } else {
                //Total time minus current moment
                remaining = endTimeMillis - SystemClock.elapsedRealtime();
                timeText = runningTimerText;
            }

            // check if round finished
            if (remaining <= 0) {
                if(Boolean.TRUE.equals(isResting().getValue()) || currentRound == roundNumber) {
                    currentRound++;
                }
                //End training
                if(currentRound > roundNumber) {
                    Timber.tag(TAG).i("Training ended");
                    runningTimerText.setValue(TimeConstants.MIN_MINUTES_TEXT);
                    numRoundsLeftText.setValue(formatNumRounds(timerConfig.getRounds(), timerConfig.getRounds()));
                    running.setValue(false);
                    resting.setValue(false);
                    return; // stop ticking
                }

                //End round
                Timber.tag(TAG).i("Round ended");
                if(Boolean.TRUE.equals(isResting().getValue())) {
                    endTimeMillis = SystemClock.elapsedRealtime() + timerConfig.getRoundTimer().calculateMillis();
                    resting.setValue(false);
                    numRoundsLeftText.setValue(formatNumRounds(currentRound, timerConfig.getRounds()));
                } else {
                    //recalculate round time and rest time
                    restEndTimeMillis = SystemClock.elapsedRealtime() + timerConfig.getRestTimer().calculateMillis();
                    //reset
                    resting.setValue(true);
                }
            }
            timeText.setValue(format(remaining));
            handler.postDelayed(this, 100);   // schedule the next tick
        }
    };

    public LiveData<Boolean> isRunning() { return running; }
    public LiveData<Boolean> isResting() { return resting; }

    public void start() {
        if (timerConfig == null) return;   // not loaded yet
        if (Boolean.TRUE.equals(running.getValue())) return;
        //If pause we don't want to recalculate the time
        if(isPaused) {
            if(Boolean.TRUE.equals(isResting().getValue())) {
                restEndTimeMillis = SystemClock.elapsedRealtime() + pauseTimeMillis;
            } else {
                endTimeMillis = SystemClock.elapsedRealtime() + pauseTimeMillis;
            }
        }
        running.setValue(true);
        handler.post(tickTimerTask);
    }

    public void reset() {
        handler.removeCallbacks(tickTimerTask);
        running.setValue(false);
        resting.setValue(false);
        isPaused = false;
        initializeDisplayData();
    }

    public void pause() {
        isPaused = true;
        if(Boolean.TRUE.equals(isResting().getValue())) {
            pauseTimeMillis = restEndTimeMillis - SystemClock.elapsedRealtime();
        } else {
            pauseTimeMillis = endTimeMillis - SystemClock.elapsedRealtime();
        }
        handler.removeCallbacks(tickTimerTask);
        running.setValue(false);
    }

    public LiveData<String> getTotalTimeText() {
        return totalTimeText;
    }

    public LiveData<String> getDisplayTimeText() {
        return runningTimerText;
    }

    public LiveData<String> getNumRoundsLeftText() {
        return numRoundsLeftText;
    }

    public LiveData<String> getRestTimeLeftText() {
        return restTimeLeftText;
    }

    @Override
    protected void onCleared() {
        handler.removeCallbacks(tickTimerTask);   // screen is gone for good: stop ticking
    }

}
