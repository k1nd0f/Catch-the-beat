package com.kindof.catchthebeat.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Time {
    private static final long nanosInMillis = 1000000;
    private static final long millisInSeconds = 1000;

    private static final String DATE_FORMAT = "d MMMM y//HH:mm";
    private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

    public static float nanosToMillis(float nanos) {
        return nanos / nanosInMillis;
    }

    public static float millisToNanos(float millis) {
        return millis * nanosInMillis;
    }

    public static float nanosToSeconds(float nanos) {
        return millisToSeconds(nanosToMillis(nanos));
    }

    public static float millisToSeconds(float millis) {
        return millis / millisInSeconds;
    }

    public static float secondsToMillis(float seconds) {
        return seconds * millisInSeconds;
    }

    public static float secondsToNanos(float seconds) {
        return millisToNanos(secondsToMillis(seconds));
    }

    public static String getStringOfTime() {
        Date date = new Date();
        date.setMinutes(date.getMinutes() + date.getTimezoneOffset());
        return dateFormat.format(date);
    }
}