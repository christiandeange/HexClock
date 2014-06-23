package com.deange.hexclock;

import android.os.Handler;

import java.io.Closeable;
import java.util.Timer;
import java.util.TimerTask;

public class HexAnimator implements Closeable {

    private static final int DELAY = 100;

    private ColourView mColourView;
    private NumberGroup mNumberGroup;

    private Timer mTimer;
    private final Handler mHandler = new Handler();

    public HexAnimator(final ColourView colourView, final NumberGroup numberGroup) {
        mColourView = colourView;
        mNumberGroup = numberGroup;
        mTimer = new Timer();
    }

    private void update() {
        final Instant now = Instant.get();

        mNumberGroup.update(now.getHours(), now.getMinutes(), now.getSeconds());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Delay is needed since the number group performs an animation
                // 100ms is roughly in the middle of the animation
                mColourView.setColour(0xFF000000 | Integer.parseInt(now.toString(), 16));
            }
        }, DELAY);
    }

    public void start() {
        final long now = System.currentTimeMillis();
        final long nextSecond = (now / 1000 + 1) * 1000;
        final int wait = (int) (nextSecond - now);

        mTimer.scheduleAtFixedRate(new UpdateTask(), wait, 1000);
    }

    @Override
    public void close() {
        mTimer.cancel();
        mTimer.purge();
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
