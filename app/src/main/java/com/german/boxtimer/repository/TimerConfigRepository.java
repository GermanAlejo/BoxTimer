package com.german.boxtimer.repository;

import com.german.boxtimer.entity.timer.TimerConfig;

public interface TimerConfigRepository {
    public TimerConfig getTimerConfigById(Long id);
    public void saveTimerConfig(TimerConfig timer);
    public void deleteTimerConfig(TimerConfig timer);
}
