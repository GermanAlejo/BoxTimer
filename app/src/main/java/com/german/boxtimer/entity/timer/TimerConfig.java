package com.german.boxtimer.entity.timer;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.german.boxtimer.utils.TimeConstants;

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
    @Embedded(prefix = "total_")
    private TimeSpan totalTime;
    //private sound
    //private boolean repeats;
    private Integer rounds;
    private Instant createdAt;

    public TimerConfig() {
        this.createdAt = Instant.now();
    }

    public TimerConfig(String name, TimeSpan roundTimer, TimeSpan restTimer, TimeSpan totalTime,
                        Integer rounds) {
        this.name = name;
        this.roundTimer = roundTimer;
        this.restTimer = restTimer;
        this.rounds = rounds;
        this.createdAt = Instant.now();
    }

    //Helper methods
    public void incrementRoundNum() {
        rounds++;
    }

    public void decrementRoundNum() {
        if(rounds - TimeConstants.MAX_MINUTES_CHANGE > 0) {
            rounds--;
        }
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

    //public boolean isRepeats() {
    //    return repeats;
    //}
//
    //public void setRepeats(boolean repeats) {
    //    this.repeats = repeats;
    //}


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

    public TimeSpan getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(TimeSpan totalTime) {
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
