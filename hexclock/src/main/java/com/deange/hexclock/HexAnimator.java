package com.deange.hexclock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;

import java.io.Closeable;
import java.util.Timer;
import java.util.TimerTask;

public class HexAnimator implements Closeable {

    private static final int DELAY = 100;
    private static final int ANIMATE_DELAY = 500;
    private static final int ANIMATE_DURATION = 1500;

    private ColourView mColourView;
    private NumberGroup mNumberGroup;

    private final boolean mAnimate;
    private int mDuration = ANIMATE_DURATION;

    private Timer mTimer;
    private final Handler mHandler = new Handler();

    public HexAnimator(final ColourView colourView, final NumberGroup numberGroup) {
        this(colourView, numberGroup, false);
    }

    public HexAnimator(final ColourView colourView, final NumberGroup numberGroup, final boolean animate) {
        mColourView = colourView;
        mNumberGroup = numberGroup;
        mAnimate = animate;
        mTimer = new Timer();

        if (mAnimate) {
            mNumberGroup.setAlpha(0);
        }
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(final int duration) {
        mDuration = duration;
    }

    private void update() {
        final Instant now = Instant.get();
        final int colour = convertInstantToColour(now);

        mNumberGroup.update(now.getHours(), now.getMinutes(), now.getSeconds());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Delay is needed since the number group performs an animation
                // 100ms is roughly in the middle of the animation
                mColourView.setColour(colour);
            }
        }, DELAY);
    }

    public void start() {
        final long now = System.currentTimeMillis();
        final long nextSecond = (now / 1000 + 1) * 1000;
        final int wait = (int) (nextSecond - now);

        if (mAnimate) {
            final ObjectAnimator animator = ObjectAnimator.ofFloat(mNumberGroup, View.ALPHA, 0, 1);
            animator.setStartDelay(ANIMATE_DELAY);
            animator.setDuration(mDuration);
            animator.start();
        }

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new UpdateTask(), wait, 1000);
    }

    public boolean isRunning() {
        return mTimer != null;
    }

    public void stop() {

        if (mAnimate) {
            final ObjectAnimator animator = ObjectAnimator.ofFloat(mNumberGroup, View.ALPHA, 1, 0);
            animator.setDuration(mDuration);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final Animator animation) {
                    close();
                }

                @Override
                public void onAnimationCancel(final Animator animation) {
                    close();
                }
            });

            animator.start();

        } else {
            close();
        }
    }

    @Override
    public void close() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }

    public static int convertInstantToColour(final Instant now) {
        // Basic RGB conversion for now
        final int hex = Integer.parseInt(now.toString(), 16);
        return 0xFF000000 | hex;
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
