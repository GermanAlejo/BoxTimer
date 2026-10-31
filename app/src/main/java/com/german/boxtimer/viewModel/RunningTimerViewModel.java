package com.german.boxtimer.viewModel;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.german.boxtimer.entity.timer.TimerConfig;
import com.german.boxtimer.repository.TimerConfigRepository;

import static com.german.boxtimer.utils.TimeUtils.format;


public class RunningTimerViewModel extends ViewModel {

    private static final long DEFAULT_DURATION_MILLIS = 5 * 60 * 1000L;   // temporary: 5 minutes
    private final MutableLiveData<Boolean> running = new MutableLiveData<>(false);
    private final MutableLiveData<String> displayTimeText = new MutableLiveData<>();
    private long endTimeMillis;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final TimerConfigRepository timerRepo;
    private TimerConfig timerConfig;

    public RunningTimerViewModel(TimerConfigRepository repo, Long timerId) {
        timerRepo = repo;
        repo.getTimerConfigById(timerId, loaded -> {
            if (loaded == null) {
                //Log here
                return;
            }
            timerConfig = loaded;
            initializeDisplayData();
        });
    }

    private void initializeDisplayData() {
        displayTimeText.postValue(timerConfig.getRoundDuration().toString());
    }

    private final Runnable tick = new Runnable() {
        @Override
        public void run() {
            long remaining = endTimeMillis - System.currentTimeMillis();
            if (remaining <= 0) {
                displayTimeText.setValue("00:00");
                running.setValue(false);
                return;                       // stop ticking
            }
            displayTimeText.setValue(format(remaining));
            handler.postDelayed(this, 100);   // schedule the next tick
        }
    };

    public LiveData<Boolean> isRunning() { return running; }

    public void start() {
        if (timerConfig == null) return;   // not loaded yet
        if (Boolean.TRUE.equals(running.getValue())) return;
        endTimeMillis = System.currentTimeMillis() + timerConfig.getRoundDuration();
        running.setValue(true);
        handler.post(tick);
    }

    public void reset() {
        handler.removeCallbacks(tick);
        running.setValue(false);
        displayTimeText.setValue(format(DEFAULT_DURATION_MILLIS));
    }

    @Override
    protected void onCleared() {
        handler.removeCallbacks(tick);   // screen is gone for good: stop ticking
    }

}
