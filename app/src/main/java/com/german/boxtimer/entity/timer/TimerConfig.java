package com.german.boxtimer.entity.timer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;

@Entity
public class TimerConfig {

    @PrimaryKey(autoGenerate = true)
    private int uid;
    private String name;
    private Long roundDuration;
    private Long restDuration;
    private Long totalTimerDuration;
    //private sound
    private boolean repeats;
    private Integer rounds;
    private Instant createdAt;

    public TimerConfig() {
        this.createdAt = Instant.now();
    }

    public TimerConfig(int uid, String name, Long roundDuration, Long restDuration, Long totalTimerDuration, boolean repeats, Integer rounds) {
        this.uid = uid;
        this.name = name;
        this.roundDuration = roundDuration;
        this.restDuration = restDuration;
        this.totalTimerDuration = totalTimerDuration;
        this.repeats = repeats;
        this.rounds = rounds;
        this.createdAt = Instant.now();
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRoundDuration() {
        return roundDuration;
    }

    public void setRoundDuration(Long roundDuration) {
        this.roundDuration = roundDuration;
    }

    public Long getRestDuration() {
        return restDuration;
    }

    public void setRestDuration(Long restDuration) {
        this.restDuration = restDuration;
    }


    public Long getTotalTimerDuration() {
        return totalTimerDuration;
    }

    public void setTotalTimerDuration(Long totalTimerDuration) {
        this.totalTimerDuration = totalTimerDuration;
    }

    public boolean isRepeats() {
        return repeats;
    }

    public void setRepeats(boolean repeats) {
        this.repeats = repeats;
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
