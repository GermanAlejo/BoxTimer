package com.german.boxtimer.utils;

import com.german.boxtimer.entity.timer.TimeSpan;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    /**
     * Returns total training time in millis
     *
     * @param roundTime time of each round in millis (long)
     * @param restTime time of each rest in millis (long)
     * @param numRounds total of rounds of training in int
     * @return total training considering all rounds with rest time included in Milliseconds
     */
    public static Long totalTimeMillis(TimeSpan roundTime, TimeSpan restTime, Integer numRounds) {
        Long totalRound = roundTime.calculateMillis() * numRounds;
        Long totalRest = restTime.calculateMillis() * numRounds;
        return totalRest + totalRound;
    }

    /**
     * Return minutes in Millis
     *
     * @param minutes Integer value of minutes
     * @return Long millis value of minutes
     */
    public static Long toMillis(Integer minutes) {
        return TimeUnit.MINUTES.toMillis(minutes);
    }

    /**
     * return minutes and seconds in millis
     *
     * @param minutes Integer value of minutes
     * @param seconds Integer value of seconds
     * @return Long millis of total time
     */
    public static Long toMillis(Integer minutes, Integer seconds) {
        return TimeUnit.SECONDS.toMillis(seconds) + toMillis(minutes);
    }

    /**
     * Formats the milliseconds pass a parameter to a clock in string format Ex: 00:00
     *
     * @param millis total time in milliseconds
     * @return Clock in string format
     */
    public static String format(long millis) {
        long totalSeconds = (millis + 999) / 1000;   // round up, so it shows 00:01 until the end
        return String.format(Locale.getDefault(), "%02d:%02d", totalSeconds / 60, totalSeconds % 60);
    }

    /**
     * Formats the Rounds to a string from int
     *
     * @param numRoundsLeft num rounds left
     * @param totalRounds total num of rounds
     * @return String format with rounds Ex: 1/3
     */
    public static String formatNumRounds(int numRoundsLeft, int totalRounds) {
        return String.format(Locale.getDefault(), "%d/%d", numRoundsLeft, totalRounds);
    }
}
