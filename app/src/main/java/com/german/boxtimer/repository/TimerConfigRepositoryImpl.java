package com.german.boxtimer.repository;

import com.german.boxtimer.dao.TimerDao;
import com.german.boxtimer.entity.timer.TimerConfig;
import com.german.boxtimer.entity.timer.TimeSpan;
import com.german.boxtimer.utils.TimeUtils;
import timber.log.Timber;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TimerConfigRepositoryImpl implements TimerConfigRepository {

    private static final String TAG = "TimerConfigRepository";

    private final TimerDao timerDao;
    private final ExecutorService executor;


    @Override
    public void loadInitial(Consumer<TimerConfig> callback) {
        executor.execute(() -> {
            Timber.tag(TAG).i("Searching for timer");
            TimerConfig config = timerDao.findLast(); // plain DAO method
            if (config == null) {
                Timber.tag(TAG).i("Timer not found, creating new one...");
                //create new
                TimeSpan round = new TimeSpan(3, 0);
                TimeSpan rest = new TimeSpan(1, 0);
                //asume no seconds
                Long totalMin = TimeUtils.toMillis(round.getMinutes() + rest.getMinutes());
                config = new TimerConfig("Timer MMA", round, rest, totalMin, 3);
                timerDao.insert(config);
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

    @Override
    public void updateTimerConfig(TimerConfig timer) {
        executor.execute(() -> timerDao.update(timer));
    }
}
