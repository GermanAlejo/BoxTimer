package com.german.boxtimer.entity.timer;


import androidx.annotation.NonNull;
import com.german.boxtimer.utils.UtilConstants;
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
        minutes+= UtilConstants.TimeConstants.MAX_MINUTES_CHANGE;
        seconds = UtilConstants.TimeConstants.MIN_MINUTE_SECONDS;
    }

    private void decrementMinutes() {
        if(minutes > UtilConstants.TimeConstants.MIN_MINUTE_SECONDS) {
            minutes-= UtilConstants.TimeConstants.MAX_MINUTES_CHANGE;
            seconds = UtilConstants.TimeConstants.MAX_SECONDS_VALUE;
        }
        //TODO: show warning here
    }

    public void incrementSeconds() {
        if(seconds + UtilConstants.TimeConstants.MAX_SECONDS_CHANGE >= UtilConstants.TimeConstants.MAX_MINUTE_SECONDS) {
            incrementMinutes();
        } else {
            seconds+= UtilConstants.TimeConstants.MAX_SECONDS_CHANGE;
        }
    }

    public void decrementSeconds() {
        if(seconds - UtilConstants.TimeConstants.MAX_SECONDS_CHANGE <= UtilConstants.TimeConstants.MIN_MINUTE_SECONDS) {
            decrementMinutes();
        } else {
            seconds-= UtilConstants.TimeConstants.MAX_SECONDS_CHANGE;
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

    @NonNull
    @Override
    public String toString() {
        return TimeUtils.format(calculateMillis());
    }
}
