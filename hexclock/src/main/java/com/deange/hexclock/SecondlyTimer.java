package com.deange.hexclock;

import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;

public class SecondlyTimer {

    private Timer mTimer;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private OnSecondListener mListener;

    public SecondlyTimer() {

    }

    public SecondlyTimer(final OnSecondListener listener) {
        this();
        mListener = listener;
    }

    public void setOnSecondListener(final OnSecondListener listener) {
        mListener = listener;
    }

    public void start() {
        if (mTimer == null) {
            final long now = System.currentTimeMillis();
            final long nextSecond = (now / 1000 + 1) * 1000;
            final int wait = (int) (nextSecond - now);

            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new UpdateTask(), wait, 1000);
        }
    }

    public void stop() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }

    public void post(final Runnable runnable) {
        mHandler.post(runnable);
    }

    private void update() {
        if (mListener != null) {
            mListener.onUpdate(Instant.get());
        }
    }

    public interface OnSecondListener {
        public void onUpdate(final Instant instant);

    }

    private final class UpdateTask extends TimerTask {
        private final Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                update();
            }
        };

        @Override
        public void run() {
            mHandler.post(mRunnable);
        }

    }
}
