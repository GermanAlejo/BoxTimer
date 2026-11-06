package com.german.boxtimer.entity.timer;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.german.boxtimer.utils.UtilConstants;
import com.german.boxtimer.utils.TimeUtils;

import java.time.Instant;

@Entity
public class TimerConfig {
    @PrimaryKey(autoGenerate = true)
    private Long uid;
    private String name;
    @Embedded(prefix = "round_")
    private TimeSpan roundTimer;
    @Embedded(prefix = "rest_")
    private TimeSpan restTimer;
    private Long totalTime;
    private Integer rounds;
    private Instant createdAt;

    public TimerConfig() {
        this.createdAt = Instant.now();
    }

    public TimerConfig(String name, TimeSpan roundTimer, TimeSpan restTimer, Long totalTime, Integer rounds) {
        this.name = name;
        this.roundTimer = roundTimer;
        this.restTimer = restTimer;
        this.totalTime = totalTime;
        this.rounds = rounds;
        this.createdAt = Instant.now();
    }

    //Helper methods
    public String increaseRoundTimer() {
        roundTimer.incrementSeconds();
        calculateTotalTime();
        return roundTimer.toString();
    }

    public String decreaseRoundTimer() {
        roundTimer.decrementSeconds();
        calculateTotalTime();
        return roundTimer.toString();
    }

    public String increaseRestTimer() {
        restTimer.incrementSeconds();
        calculateTotalTime();
        return restTimer.toString();
    }

    public String decreaseRestTimer() {
        restTimer.decrementSeconds();
        calculateTotalTime();
        return restTimer.toString();
    }

    public void incrementRoundNum() {
        rounds++;
        calculateTotalTime();
    }

    public void decrementRoundNum() {
        if (rounds - UtilConstants.TimeConstants.MAX_MINUTES_CHANGE > 0) {
            rounds--;
        }
        calculateTotalTime();
    }

    private void calculateTotalTime() {
        totalTime = TimeUtils.totalTimeMillis(roundTimer, restTimer, rounds);
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TimeSpan getRoundTimer() {
        return roundTimer;
    }

    public void setRoundTimer(TimeSpan roundTimer) {
        this.roundTimer = roundTimer;
    }

    public TimeSpan getRestTimer() {
        return restTimer;
    }

    public void setRestTimer(TimeSpan restTimer) {
        this.restTimer = restTimer;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public Integer getRounds() {
        return rounds;
    }

    public void setRounds(Integer rounds) {
        this.rounds = rounds;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}
