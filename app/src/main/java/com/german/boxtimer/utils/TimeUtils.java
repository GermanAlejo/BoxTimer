package com.german.boxtimer.utils;

public class TimeUtils {



    public static String format(long millis) {
        long totalSeconds = (millis + 999) / 1000;   // round up, so it shows 00:01 until the end
        return String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60);
    }
}
