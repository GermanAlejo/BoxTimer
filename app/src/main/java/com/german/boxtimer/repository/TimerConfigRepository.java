package com.german.boxtimer.repository;

import com.german.boxtimer.entity.timer.TimerConfig;

import java.util.function.Consumer;

public interface TimerConfigRepository {
    void loadInitial(Consumer<TimerConfig> callback);
    void getTimerConfigById(Long id, Consumer<TimerConfig> callback);
    TimerConfig findLast();
    void saveTimerConfig(TimerConfig timer);
    void deleteTimerConfig(TimerConfig timer);

}
