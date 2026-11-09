package com.german.boxtimer.utils;

public final class UtilConstants {
    private UtilConstants(){}
    public static class TimeConstants {
        //String constants
        private TimeConstants(){}
        public static final String DEFAULT_TIMER_ID = "timerId";
        public static final String MIN_MINUTES_TEXT = "00:00";

        //Number constants
        public static final int MAX_MINUTES_CHANGE = 1;
        public static final int MAX_SECONDS_CHANGE = 5;
        public static final int MAX_MINUTE_SECONDS = 60;
        public static final int MIN_MINUTE_SECONDS = 0;
        public static final int MIN_SECONDS_ALLOWED = 10;
        public static final int MAX_SECONDS_VALUE = 55;
        public static final Long LAST_SECOND_COUNTDOWN = 5000L;
    }

    public static class SoundConstants {
        private SoundConstants(){}
        public static final float LEFT_VOLUME = 2f;
        public static final float RIGHT_VOLUME = 2f;
        public static final float SOUND_RATE = 1.5f;

    }

}
