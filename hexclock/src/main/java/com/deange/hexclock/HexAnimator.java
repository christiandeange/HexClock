package com.deange.hexclock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;

import java.io.Closeable;

public class HexAnimator implements Closeable, SecondlyTimer.OnSecondListener {

    private static final int DELAY = 100;
    private static final int ANIMATE_DELAY = 500;
    private static final int ANIMATE_DURATION = 1500;

    private ColourView mColourView;
    private NumberGroup mNumberGroup;

    private int mDuration = ANIMATE_DURATION;
    private final boolean mAnimate;
    private final AnimatorListenerAdapter mAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(final Animator animation) {
            close();
        }

        @Override
        public void onAnimationCancel(final Animator animation) {
            close();
        }
    };

    private SecondlyTimer mTimer;
    private final Handler mHandler = new Handler();

    public HexAnimator(final ColourView colourView, final NumberGroup numberGroup) {
        this(colourView, numberGroup, false);
    }

    public HexAnimator(final ColourView colourView, final NumberGroup numberGroup, final boolean animate) {
        mColourView = colourView;
        mNumberGroup = numberGroup;
        mAnimate = animate;
        mTimer = new SecondlyTimer(this);

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

    @Override
    public void onUpdate(final Instant instant) {
        final int colour = convertInstantToColour(instant);

        mNumberGroup.update(instant.getHours(), instant.getMinutes(), instant.getSeconds());
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
        mTimer.start();
        if (mAnimate) {
            final ObjectAnimator animator = ObjectAnimator.ofFloat(mNumberGroup, View.ALPHA, 0, 1);
            animator.setStartDelay(ANIMATE_DELAY);
            animator.setDuration(mDuration);
            animator.start();
        }
    }

    public void stop() {
        if (mAnimate) {
            final ObjectAnimator animator = ObjectAnimator.ofFloat(mNumberGroup, View.ALPHA, 1, 0);
            animator.setDuration(mDuration);
            animator.addListener(mAnimatorListener);
            animator.start();

        } else {
            close();
        }
    }

    @Override
    public void close() {
        mTimer.stop();
    }

    public static int convertInstantToColour(final Instant now) {
        // Basic RGB conversion for now
        final int hex = Integer.parseInt(now.toString(), 16);
        return 0xFF000000 | hex;
    }
}
