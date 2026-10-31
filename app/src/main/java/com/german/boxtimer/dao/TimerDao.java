package com.german.boxtimer.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Delete;
import com.german.boxtimer.entity.timer.TimerConfig;

@Dao
public interface TimerDao {

    @Query("SELECT * FROM timerconfig tc WHERE tc.uid = :id")
    TimerConfig getById(Long id);
    @Insert
    long insert(TimerConfig timer);

    @Query("SELECT * FROM timerconfig tc ORDER BY tc.createdAt DESC LIMIT 1")
    TimerConfig findLast();

    //@Update
    //void update(TimerConfig timer);

    @Delete
    void delete(TimerConfig timer);
}
