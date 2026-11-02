package com.german.boxtimer.viewModel;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.TextView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.german.boxtimer.entity.timer.TimerConfig;
import com.german.boxtimer.repository.TimerConfigRepository;
import com.german.boxtimer.utils.TimeUtils;
import kotlin.time.TimeMark;
import timber.log.Timber;

import java.util.Locale;

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
    //Timer status time
    private boolean isPaused = false;
    private long pauseTimeMillis;
    private long endTimeMillis;
    private long restEndTimeMillis;
    private int roundNumber;

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
        restEndTimeMillis = TimeUtils.toMillis(timerConfig.getRestDuration());
        endTimeMillis = TimeUtils.toMillis(timerConfig.getRoundDuration());
        roundNumber = timerConfig.getRounds();
        String time = TimeUtils.format(endTimeMillis);
        runningTimerText.postValue(time);
        numRoundsLeftText.postValue(TimeUtils.formatNumRounds(1, roundNumber));
        Long totalTime = TimeUtils.totalTimeMillis(timerConfig.getRoundDuration(), timerConfig.getRestDuration(), timerConfig.getRounds());
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

            if (remaining <= 0) {
                roundNumber--;
                //End training
                if(roundNumber <= 0) {
                    Timber.tag(TAG).i("Training ended");
                    runningTimerText.setValue("00:00");
                    numRoundsLeftText.setValue(formatNumRounds(roundNumber, timerConfig.getRounds()));
                    running.setValue(false);
                    resting.setValue(false);
                    return; // stop ticking
                }

                //End round
                Timber.tag(TAG).i("Round ended");
                if(Boolean.TRUE.equals(isResting().getValue())) {
                    endTimeMillis = SystemClock.elapsedRealtime() + TimeUtils.toMillis(timerConfig.getRoundDuration());
                    resting.setValue(false);
                } else {
                    //recalculate round time and rest time
                    restEndTimeMillis = SystemClock.elapsedRealtime() + TimeUtils.toMillis(timerConfig.getRestDuration());
                    //reset
                    numRoundsLeftText.setValue(formatNumRounds(roundNumber, timerConfig.getRounds()));
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
        if(!isPaused) {
            //save future date (finish date) we calculate agains that
            endTimeMillis = SystemClock.elapsedRealtime() + TimeUtils.toMillis(timerConfig.getRoundDuration());
        } else {
            endTimeMillis = SystemClock.elapsedRealtime() + pauseTimeMillis;
        }
        running.setValue(true);
        handler.post(tickTimerTask);
    }

    public void reset() {
        handler.removeCallbacks(tickTimerTask);
        running.setValue(false);
        runningTimerText.setValue(format(TimeUtils.toMillis(timerConfig.getRoundDuration())));
    }

    public void pause() {
        isPaused = true;
        pauseTimeMillis = endTimeMillis - SystemClock.elapsedRealtime();
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
