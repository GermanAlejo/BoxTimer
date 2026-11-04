package com.german.boxtimer.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import com.german.boxtimer.R;
import timber.log.Timber;

import static android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION;
import static android.media.AudioAttributes.USAGE_ALARM;

/**
 * Class to control sounds
 */
public class SoundPlayer implements SoundEffects {
    private static final String TAG = "SoundPlayer";
    private final SoundPool soundPool;
    private final int bellId;
    private final int singleBellId;

    public SoundPlayer(Context context) {
        Timber.tag(TAG).i("Creating sound player");
        android.media.AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(USAGE_ALARM)
                .setContentType(CONTENT_TYPE_SONIFICATION)
                .build();
        SoundPool.Builder builder = new SoundPool.Builder().setAudioAttributes(attributes);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            builder.setContext(context);
        }
        //Build sound player
        soundPool = builder.build();
        //Load bell sounds
        bellId = soundPool.load(context, R.raw.boxing_bell, 1);
        singleBellId = soundPool.load(context, R.raw.boxing_bell_single, 1);
    }

    @Override
    public void playBell() {
        Timber.tag(TAG).i("Playing bell");
        int streamId = soundPool.play(bellId, 0.5f, 0.5f, 0, 0,1.5f);
        if(streamId == 0) {
            Timber.tag(TAG).e("Could not play sound");
        }
    }

    @Override
    public void playSingleBell() {
        Timber.tag(TAG).i("Playing single bell");
        int streamId = soundPool.play(singleBellId, 0.5f, 0.5f, 0, 0, 1f);
        if(streamId == 0) {
            Timber.tag(TAG).e("Could not play sound");
        }
    }

    @Override
    public void playFinalBell() {
        Timber.tag(TAG).i("Playing final bell");
        int streamId = soundPool.play(bellId, 0.5f, 0.5f, 0, 0,2f);
        soundPool.setLoop(streamId, 1);
        if(streamId == 0) {
            Timber.tag(TAG).e("Could not play sound");
        }
    }

    @Override
    public void release() {
        soundPool.release();
    }

}
