package com.german.boxtimer.domain;


import com.german.boxtimer.entity.timer.TimerConfig;
import com.german.boxtimer.sound.SoundEffects;
import timber.log.Timber;

import java.util.HashMap;
import java.util.Map;

import static com.german.boxtimer.utils.TimeUtils.getBeepToPlay;
import static com.german.boxtimer.utils.UtilConstants.TimeConstants.LAST_SECOND_COUNTDOWN;

/**
 * Class to abstract business logic into
 */
public class TimerEngine {
    private static final String TAG = "TimerEngine";
    private final SoundEffects soundPlayer;
    private final Map<Integer, Boolean> beepMap;


    public TimerEngine(SoundEffects soundPlayer) {
        this.soundPlayer = soundPlayer;
        beepMap = new HashMap<>(Map.of(1, false, 2, false, 3, false, 4, false));
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

    public void finalSecondsCountdown(Long remaining) {
        long secondsLeft = remaining;
        if (secondsLeft >= 0L && secondsLeft <= LAST_SECOND_COUNTDOWN) {
            if (getBeepToPlay(secondsLeft, beepMap) != 0) {
                Timber.tag(TAG).i("Last seconds... playing beep");
                playBeep();
            }
            return;
        }
        //reset map
        beepMap.replaceAll((integer, aBoolean) -> false);
    }

    public void clearEngine() {
        beepMap.replaceAll((integer, aBoolean) -> false);
        soundPlayer.release();
    }

}
