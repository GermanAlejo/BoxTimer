package com.german.boxtimer.repository;

import com.german.boxtimer.dao.TimerDao;
import com.german.boxtimer.entity.timer.TimerConfig;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimerConfigRepositoryImpl implements TimerConfigRepository {

    private final TimerDao timerDao;
    private final ExecutorService executor;


    public TimerConfigRepositoryImpl(TimerDao timerDao) {
        this.timerDao = timerDao;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public TimerConfig getTimerConfigById(Long id) {
        return timerDao.getById(id);
    }

    @Override
    public void saveTimerConfig(TimerConfig timer) {
        executor.execute(() -> timerDao.insert(timer));
    }

    @Override
    public void deleteTimerConfig(TimerConfig timer) {
        executor.execute(() -> timerDao.delete(timer));
    }
}
