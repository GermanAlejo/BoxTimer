package com.german.boxtimer;

import android.app.Application;
import androidx.room.Room;
import com.german.boxtimer.db.AppDatabase;
import com.german.boxtimer.repository.TimerConfigRepository;
import com.german.boxtimer.repository.TimerConfigRepositoryImpl;


public class TimerApp extends Application {
    private AppDatabase db;
    private TimerConfigRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        db = Room.databaseBuilder(this, AppDatabase.class, "timers.db").build();
        repository = new TimerConfigRepositoryImpl(db.timerDao());
    }

    public TimerConfigRepository getRepository() {
        return repository;
    }
}
