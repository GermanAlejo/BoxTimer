package com.german.boxtimer.entity.timer;


import com.german.boxtimer.utils.TimeConstants;
import com.german.boxtimer.utils.TimeUtils;

/**
 * Class to represent a timer type (rest, round) in minutes and seconds
 */
public class TimeSpan {
    private int minutes;
    private int seconds;

    public TimeSpan(int minutes, int seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Long calculateMillis() {
        return TimeUtils.toMillis(minutes, seconds);
    }

    private void incrementMinutes() {
        minutes+= TimeConstants.MAX_MINUTES_CHANGE;
        seconds = TimeConstants.MIN_MINUTE_SECONDS;
    }

    private void decrementMinutes() {
        if(minutes > TimeConstants.MIN_MINUTE_SECONDS) {
            minutes-=TimeConstants.MAX_MINUTES_CHANGE;
            seconds = TimeConstants.MAX_SECONDS_VALUE;
        }
        //TODO: show warning here
    }

    public void incrementSeconds() {
        if(seconds + TimeConstants.MAX_SECONDS_CHANGE >= TimeConstants.MAX_MINUTE_SECONDS) {
            incrementMinutes();
        } else {
            seconds+=TimeConstants.MAX_SECONDS_CHANGE;
        }
    }

    public void decrementSeconds() {
        if(seconds - TimeConstants.MAX_SECONDS_CHANGE <= TimeConstants.MIN_MINUTE_SECONDS) {
            decrementMinutes();
        } else {
            seconds-=TimeConstants.MAX_SECONDS_CHANGE;
        }
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return TimeUtils.format(calculateMillis());
    }
}
