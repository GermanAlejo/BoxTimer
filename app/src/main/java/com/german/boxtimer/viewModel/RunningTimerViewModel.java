package com.german.boxtimer.viewModel;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.german.boxtimer.entity.timer.TimerConfig;
import com.german.boxtimer.repository.TimerConfigRepository;
import com.german.boxtimer.utils.TimeUtils;

import static com.german.boxtimer.utils.TimeUtils.format;


public class RunningTimerViewModel extends ViewModel {

    private final MutableLiveData<Boolean> running = new MutableLiveData<>(false);
    private final MutableLiveData<String> runningTimerText = new MutableLiveData<>();
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
        Long timeMillis = TimeUtils.toMillis(timerConfig.getRoundDuration());
        endTimeMillis = timeMillis;
        String time = TimeUtils.format(timeMillis);
        runningTimerText.postValue(time);
    }

    private final Runnable tickTimerTask = new Runnable() {
        @Override
        public void run() {
            //Total time minus current moment
            long remaining = endTimeMillis - SystemClock.elapsedRealtime();
            if (remaining <= 0) {
                runningTimerText.setValue("00:00");
                running.setValue(false);
                return;                       // stop ticking
            }
            runningTimerText.setValue(format(remaining));
            handler.postDelayed(this, 100);   // schedule the next tick
        }
    };

    public LiveData<Boolean> isRunning() { return running; }

    public void start() {
        if (timerConfig == null) return;   // not loaded yet
        if (Boolean.TRUE.equals(running.getValue())) return;
        //save future date (finish date) we calculate agains that
        endTimeMillis = SystemClock.elapsedRealtime() + TimeUtils.toMillis(timerConfig.getRoundDuration());
        running.setValue(true);
        handler.post(tickTimerTask);
    }

    public void reset() {
        handler.removeCallbacks(tickTimerTask);
        running.setValue(false);
        runningTimerText.setValue(format(TimeUtils.toMillis(timerConfig.getRoundDuration())));
    }

    public LiveData<String> getDisplayTimeText() {
        return runningTimerText;
    }

    @Override
    protected void onCleared() {
        handler.removeCallbacks(tickTimerTask);   // screen is gone for good: stop ticking
    }

}
