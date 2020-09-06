package com.kindof.catchthebeat.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Time {
    private static final long MILLI_IN_NANOS = 1000000;
    private static final long SECOND_IN_MILLIS = 1000;
    private static final long HOUR_IN_MINUTES = 60;
    private static final long MINUTE_IN_SECONDS = 60;

    private static final String PATTERN = "d MMMM y//HH:mm";
    private static final DateFormat dateFormat = new SimpleDateFormat(PATTERN, Locale.ENGLISH);

    public static float nanosToHours(float nanos) {
        return minutesToHours(nanosToMinutes(nanos));
    }

    public static float nanosToMinutes(float nanos) {
        return millisToMinutes(nanosToMillis(nanos));
    }

    public static float nanosToSeconds(float nanos) {
        return millisToSeconds(nanosToMillis(nanos));
    }

    public static float nanosToMillis(float nanos) {
        return nanos / MILLI_IN_NANOS;
    }

    public static float millisToHours(float millis) {
        return minutesToHours(millisToMinutes(millis));
    }

    public static float millisToMinutes(float millis) {
        return secondsToMinutes(millisToSeconds(millis));
    }

    public static float millisToSeconds(float millis) {
        return millis / SECOND_IN_MILLIS;
    }

    public static float millisToNanos(float millis) {
        return millis * MILLI_IN_NANOS;
    }

    public static float secondsToHours(float seconds) {
        return minutesToHours(secondsToMinutes(seconds));
    }

    public static float secondsToMinutes(float seconds) {
        return seconds / MINUTE_IN_SECONDS;
    }

    public static float secondsToMillis(float seconds) {
        return seconds * SECOND_IN_MILLIS;
    }

    public static float secondsToNanos(float seconds) {
        return millisToNanos(secondsToMillis(seconds));
    }
    
    public static float minutesToHours(float minutes) {
        return minutes / HOUR_IN_MINUTES;
    }

    public static float minutesToSeconds(float minutes) {
        return minutes * MINUTE_IN_SECONDS;
    }

    public static float minutesToMillis(float minutes) {
        return secondsToMillis(minutesToSeconds(minutes));
    }

    public static float minutesToNanos(float minutes) {
        return secondsToNanos(minutesToSeconds(minutes));
    }

    public static float hoursToMinutes(float hours) {
        return hours * HOUR_IN_MINUTES;
    }

    public static float hoursToSeconds(float hours) {
        return minutesToSeconds(hoursToMinutes(hours));
    }

    public static float hoursToMillis(float hours) {
        return secondsToMillis(hoursToSeconds(hours));
    }

    public static float hoursToNanos(float hours) {
        return millisToNanos(hoursToMillis(hours));
    }

    public static String getStringOfTime() {
        Date date = new Date();
        date.setMinutes(date.getMinutes() + date.getTimezoneOffset());
        return dateFormat.format(date) + " GMT";
    }
}