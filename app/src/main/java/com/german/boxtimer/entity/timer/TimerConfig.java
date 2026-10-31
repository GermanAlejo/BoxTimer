package com.german.boxtimer.entity.timer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;

@Entity
public class TimerConfig {

    @PrimaryKey(autoGenerate = true)
    private Long uid;
    private String name;
    private Integer roundDuration;
    private Integer restDuration;
    private Integer totalTimerDuration;
    //private sound
    //private boolean repeats;
    private Integer rounds;
    private Instant createdAt;

    public TimerConfig() {
        this.createdAt = Instant.now();
    }

    public TimerConfig(String name, Integer roundDuration, Integer restDuration, Integer rounds) {
        this.name = name;
        this.roundDuration = roundDuration;
        this.restDuration = restDuration;
        this.rounds = rounds;
        this.createdAt = Instant.now();
        this.totalTimerDuration = rounds * roundDuration + rounds- restDuration;
    }

    //Helper methods
    //TODO: Maybe do check here
    public void incrementRound() {
        roundDuration++;
    }

    public void decrementRound() {
        roundDuration--;
    }

    public void incrementRest() {
        restDuration++;
    }

    public void decrementRest() {
        restDuration--;
    }

    public void incrementRoundNum() {
        rounds++;
    }

    public void decrementRoundNum() {
        rounds--;
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

    public Integer getRoundDuration() {
        return roundDuration;
    }

    public void setRoundDuration(Integer roundDuration) {
        this.roundDuration = roundDuration;
    }

    public Integer getRestDuration() {
        return restDuration;
    }

    public void setRestDuration(Integer restDuration) {
        this.restDuration = restDuration;
    }


    public Integer getTotalTimerDuration() {
        return totalTimerDuration;
    }

    public void setTotalTimerDuration(Integer totalTimerDuration) {
        this.totalTimerDuration = totalTimerDuration;
    }

    //public boolean isRepeats() {
    //    return repeats;
    //}
//
    //public void setRepeats(boolean repeats) {
    //    this.repeats = repeats;
    //}

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
