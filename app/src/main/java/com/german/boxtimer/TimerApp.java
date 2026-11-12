package com.german.boxtimer;

import android.app.Application;
import androidx.room.Room;
import com.german.boxtimer.db.AppDatabase;
import com.german.boxtimer.repository.TimerConfigRepository;
import com.german.boxtimer.repository.TimerConfigRepositoryImpl;
import timber.log.Timber;


public class TimerApp extends Application {
    private static final String TAG = "TimerApp";

    private AppDatabase db;
    private TimerConfigRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        Timber.tag(TAG).i("Application started, creating database");
        db = Room.databaseBuilder(this, AppDatabase.class, "timers.db").build();
        repository = new TimerConfigRepositoryImpl(db.timerDao());
    }

    public TimerConfigRepository getRepository() {
        return repository;
    }
}
