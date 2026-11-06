package com.german.boxtimer.domain;


import com.german.boxtimer.entity.timer.TimerConfig;
import com.german.boxtimer.sound.SoundEffects;
import com.german.boxtimer.utils.TimeUtils;
import timber.log.Timber;

import static com.german.boxtimer.utils.UtilConstants.TimeConstants.LAST_SECOND_COUNTDOWN;

/**
 * Class to abstract business logic into
 */
public class TimerEngine {
    private static final String TAG = "TimerEngine";
    private final SoundEffects soundPlayer;
    private TimerConfig timerConfig;
    private long lastSecondMillis;
    private boolean isPaused = false;
    private long pauseTimeMillis;
    private long endTimeMillis;
    private long restEndTimeMillis;
    private int roundNumber;
    private int currentRound;

    public TimerEngine(TimerConfig timerConfig, SoundEffects soundPlayer) {
        this.timerConfig = timerConfig;
        this.soundPlayer = soundPlayer;
        lastSecondMillis = 0L;
    }

    public void playBell() {
        soundPlayer.playBell();
    }

    public void playSingleBell() {
        soundPlayer.playSingleBell();
    }

    public void playFinalBell() {
        soundPlayer.playFinalBell();
    }

    private void playBeep() {
        soundPlayer.playBeep();
    }

    public void finalSeconds(Long remaining) {
        if(remaining < LAST_SECOND_COUNTDOWN) {
            Timber.tag(TAG).i("Last seconds... playing beep");
            if(TimeUtils.hasSecondPassed(lastSecondMillis, remaining)){
                playBeep();
            }
            lastSecondMillis = remaining;
        }
    }

    public void clearEngine() {
        soundPlayer.release();
    }

}
