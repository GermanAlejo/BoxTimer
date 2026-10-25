package com.german.boxtimer.converter;

import androidx.room.TypeConverter;
import java.time.Instant;

public class Converters {
    @TypeConverter
    public static Long fromInstant(Instant value) {
        return value == null ? null : value.toEpochMilli();
    }

    @TypeConverter
    public static Instant toInstant(Long value) {
        return value == null ? null : Instant.ofEpochMilli(value);
    }
}