package com.german.boxtimer.dao;

import androidx.room.*;
import com.german.boxtimer.entity.timer.TimerConfig;

@Dao
public interface TimerDao {

    @Query("SELECT * FROM timerconfig tc WHERE tc.uid = :id")
    TimerConfig getById(Long id);
    @Insert
    long insert(TimerConfig timer);

    //@Update
    //void update(TimerConfig timer);

    @Delete
    void delete(TimerConfig timer);
}
