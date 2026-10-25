package com.german.boxtimer.repository;

import com.german.boxtimer.entity.timer.TimerConfig;

public interface TimerConfigRepository {
    public TimerConfig getTimerConfigById(Long id);
    public void saveTimerConfig();
    public void deleteTimerConfig();
}
