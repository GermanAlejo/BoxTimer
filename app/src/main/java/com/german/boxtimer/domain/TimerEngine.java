package com.german.boxtimer.domain;


import com.german.boxtimer.entity.timer.TimerConfig;
import com.german.boxtimer.sound.SoundEffects;

/**
 * Class to abstract business logic into
 */
public class TimerEngine {
    private static final String TAG = "TimerEngine";
    private final SoundEffects soundPlayer;
    private TimerConfig timerConfig;
    private boolean isPaused = false;
    private long pauseTimeMillis;
    private long endTimeMillis;
    private long restEndTimeMillis;
    private int roundNumber;
    private int currentRound;

    public TimerEngine(TimerConfig timerConfig, SoundEffects soundPlayer) {
        this.timerConfig = timerConfig;
        this.soundPlayer = soundPlayer;
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


    public void clearEngine() {
        soundPlayer.release();
    }

}
