package com.deange.hexclock;

import java.util.Calendar;

public class Instant {

    private final int mHours;
    private final int mMinutes;
    private final int mSeconds;

    public static Instant get() {
        return new Instant();
    }

    protected Instant() {
        final Calendar cal = Calendar.getInstance();
        mHours   = cal.get(Calendar.HOUR_OF_DAY);
        mMinutes = cal.get(Calendar.MINUTE);
        mSeconds = cal.get(Calendar.SECOND);
    }

    public int getHours() {
        return mHours;
    }

    public int getMinutes() {
        return mMinutes;
    }

    public int getSeconds() {
        return mSeconds;
    }

    public String getHoursString() {
        return String.format("%02d", mHours);
    }

    public String getMinutesString() {
        return String.format("%02d", mMinutes);
    }

    public String getSecondsString() {
        return String.format("%02d", mSeconds);
    }

    @Override
    public String toString() {
        return getHoursString() + getMinutesString() + getSecondsString();
    }
}
