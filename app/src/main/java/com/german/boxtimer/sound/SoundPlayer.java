package com.german.boxtimer.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import com.german.boxtimer.R;
import com.german.boxtimer.utils.UtilConstants;
import timber.log.Timber;

import static android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION;
import static android.media.AudioAttributes.USAGE_MEDIA;

/**
 * Class to control sounds
 */
public class SoundPlayer implements SoundEffects {
    private static final String TAG = "SoundPlayer";
    private final SoundPool soundPool;
    private final int bellId;
    private final int singleBellId;
    private final int beepId;

    public SoundPlayer(Context context) {
        Timber.tag(TAG).i("Creating sound player");
        android.media.AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(USAGE_MEDIA)
                .setContentType(CONTENT_TYPE_SONIFICATION)
                .build();
        SoundPool.Builder builder = new SoundPool.Builder().setAudioAttributes(attributes);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            builder.setContext(context);
        }
        //Build sound player
        soundPool = builder.build();
        //Load bell sounds
        bellId = soundPool.load(context, R.raw.bell_effect, 1);
        singleBellId = soundPool.load(context, R.raw.bell_single_effect, 1);
        beepId = soundPool.load(context, R.raw.beep, 1);
    }

    @Override
    public void playBell() {
        Timber.tag(TAG).i("Playing bell");
        int streamId = soundPool.play(bellId,
                UtilConstants.SoundConstants.LEFT_VOLUME,
                UtilConstants.SoundConstants.RIGHT_VOLUME,
                1, 0,
                UtilConstants.SoundConstants.SOUND_RATE);
        if (streamId == 0) {
            Timber.tag(TAG).e("Could not play sound");
        }
    }

    @Override
    public void playSingleBell() {
        Timber.tag(TAG).i("Playing single bell");
        int streamId = soundPool.play(singleBellId,
                UtilConstants.SoundConstants.LEFT_VOLUME,
                UtilConstants.SoundConstants.RIGHT_VOLUME,
                1, 0,
                UtilConstants.SoundConstants.SOUND_RATE);
        if (streamId == 0) {
            Timber.tag(TAG).e("Could not play sound");
        }
    }

    @Override
    public void playFinalBell() {
        Timber.tag(TAG).i("Playing final bell");
        int streamId = soundPool.play(bellId,
                UtilConstants.SoundConstants.LEFT_VOLUME,
                UtilConstants.SoundConstants.RIGHT_VOLUME,
                2, 2,
                UtilConstants.SoundConstants.SOUND_RATE);
        soundPool.setLoop(streamId, 1);
        if (streamId == 0) {
            Timber.tag(TAG).e("Could not play sound");
        }
    }

    @Override
    public void playBeep() {
        Timber.tag(TAG).i("Playing beep");
        int streamId = soundPool.play(beepId,
                UtilConstants.SoundConstants.LEFT_VOLUME,
                UtilConstants.SoundConstants.RIGHT_VOLUME,
                0, 0, 2);
        if (streamId == 0) {
            Timber.tag(TAG).e("Could not play sound");
        }
    }

    @Override
    public void release() {
        soundPool.release();
    }

}
