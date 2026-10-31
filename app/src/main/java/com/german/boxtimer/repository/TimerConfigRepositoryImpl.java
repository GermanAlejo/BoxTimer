package com.german.boxtimer.repository;

import com.german.boxtimer.dao.TimerDao;
import com.german.boxtimer.entity.timer.TimerConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TimerConfigRepositoryImpl implements TimerConfigRepository {

    private final TimerDao timerDao;
    private final ExecutorService executor;


    @Override
    public void loadInitial(Consumer<TimerConfig> callback) {
        executor.execute(() -> {
            TimerConfig config = timerDao.findLast(); // plain DAO method
            if (config == null) {
                //create new
                config = new TimerConfig("Timer MMA", 3, 1, 3);
            }
            callback.accept(config);   // runs on the background thread
        });
    }

    public TimerConfigRepositoryImpl(TimerDao timerDao) {
        this.timerDao = timerDao;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void getTimerConfigById(Long id, Consumer<TimerConfig> callback) {
        executor.execute(() -> callback.accept(timerDao.getById(id)));
    }

    @Override
    public TimerConfig findLast() {
        return timerDao.findLast();
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
