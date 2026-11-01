package com.german.boxtimer.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static Long totalTimeMillis(Integer roundTime, Integer restTime, Integer numRounds) {
        Long totalRound = roundTime.longValue() * numRounds;
        Long totalRest = restTime.longValue() * numRounds;
        return totalRest + totalRound;
    }

    public static Long toMillis(Integer minutes) {
        return TimeUnit.MINUTES.toMillis(minutes);
    }

    public static String format(long millis) {
        long totalSeconds = (millis + 999) / 1000;   // round up, so it shows 00:01 until the end
        return String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60);
    }
}
