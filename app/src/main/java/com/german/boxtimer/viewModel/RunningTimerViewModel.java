package com.german.boxtimer.viewModel;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RunningTimerViewModel extends ViewModel {
    private static final long DEFAULT_DURATION_MILLIS = 5 * 60 * 1000L;   // temporary: 5 minutes

    private final MutableLiveData<String> displayText = new MutableLiveData<>("05:00");
    private final MutableLiveData<Boolean> running = new MutableLiveData<>(false);

    private final MutableLiveData<Integer> displayRoundNumber = new MutableLiveData<>(0);

    private final Handler handler = new Handler(Looper.getMainLooper());
    private long endTimeMillis;

    private final Runnable tick = new Runnable() {
        @Override
        public void run() {
            long remaining = endTimeMillis - System.currentTimeMillis();
            if (remaining <= 0) {
                displayText.setValue("00:00");
                running.setValue(false);
                return;                       // stop ticking
            }
            displayText.setValue(format(remaining));
            handler.postDelayed(this, 100);   // schedule the next tick
        }
    };

    // Exposed read-only: the Activity can observe but not change the values
    public LiveData<String> getDisplayText() { return displayText; }
    public LiveData<Boolean> isRunning() { return running; }

    public void start() {
        //TODO: Make start button dissapear/change views
        if (Boolean.TRUE.equals(running.getValue())) return;
        endTimeMillis = System.currentTimeMillis() + DEFAULT_DURATION_MILLIS;
        running.setValue(true);
        handler.post(tick);
    }

    public void reset() {
        handler.removeCallbacks(tick);
        running.setValue(false);
        displayText.setValue(format(DEFAULT_DURATION_MILLIS));
    }

    public LiveData<Integer> getValButton() {
        return displayRoundNumber;
    }

    public void increaseButton() {
        Integer current = displayRoundNumber.getValue();
        displayRoundNumber.setValue(current+1);
    }

    public void decreaseButton() {
        Integer current = displayRoundNumber.getValue();
        displayRoundNumber.setValue(current-1);
    }

    @Override
    protected void onCleared() {
        handler.removeCallbacks(tick);   // screen is gone for good: stop ticking
    }

    private static String format(long millis) {
        long totalSeconds = (millis + 999) / 1000;   // round up, so it shows 00:01 until the end
        return String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60);
    }
}
