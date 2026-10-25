package com.german.boxtimer.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.german.boxtimer.converter.Converters;
import com.german.boxtimer.dao.TimerDao;
import com.german.boxtimer.entity.timer.TimerConfig;

@Database(entities = {TimerConfig.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TimerDao timerDao();
}