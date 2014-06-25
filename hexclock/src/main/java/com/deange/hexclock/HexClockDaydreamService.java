package com.deange.hexclock;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.service.dreams.DreamService;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Random;


/**
 * This class is a sample implementation of a DreamService. When activated, a
 * TextView will repeatedly, move from the left to the right of screen, at a
 * random y-value.
 * <p />
 * Daydreams are only available on devices running API v17+.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class HexClockDaydreamService extends DreamService {

    private static final int ANIMATION_DURATION = 3500;
    private static final int SHOW_DURATION = 10000 + ANIMATION_DURATION;
    private static final int INTERMISSION_DURATION = ANIMATION_DURATION + 100;

    private final Rect mRect = new Rect();
    private final Handler mHandler = new Handler();
    private final Runnable mMoveRunnable = new Runnable() {
        @Override
        public void run() {
            move();
        }
    };
    private final Runnable mStopRunnable = new Runnable() {
        @Override
        public void run() {
            prepNextAnimation();
        }
    };

    private NumberGroup mNumberGroup;
    private HexAnimator mAnimator;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        setInteractive(false);
        setFullscreen(true);
        setScreenBright(true);

        // Set the content view, just like you would with an Activity.
        setContentView(R.layout.hex_clock_layout);
        final ColourView colourView = (ColourView) findViewById(R.id.hex_view);
        mNumberGroup = (NumberGroup) findViewById(R.id.numbers_view);

        ((RelativeLayout.LayoutParams) mNumberGroup.getLayoutParams()).removeRule(RelativeLayout.CENTER_IN_PARENT);

        mAnimator = new HexAnimator(colourView, mNumberGroup, true);
        mAnimator.setDuration(ANIMATION_DURATION);

        getWindowManager().getDefaultDisplay().getRectSize(mRect);
    }

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();

        // Wait for the first layout pass
        mNumberGroup.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(final View v,
                   final int left, final int top, final int right, final int bottom,
                   final int oldLeft, final int oldTop, final int oldRight, final int oldBottom) {
                mNumberGroup.removeOnLayoutChangeListener(this);
                move();
            }
        });
    }

    @Override
    public void onDreamingStopped() {
        super.onDreamingStopped();

        mAnimator.stop();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mAnimator.close();
    }

    private void move() {
        final Point randomPoint = getRandomPointInside();
        mNumberGroup.setX(randomPoint.x);
        mNumberGroup.setY(randomPoint.y);

        mAnimator.start();
        mHandler.postDelayed(mStopRunnable, SHOW_DURATION);
    }

    private void prepNextAnimation() {
        mAnimator.stop();
        mHandler.postDelayed(mMoveRunnable, INTERMISSION_DURATION);
    }

    private Point getRandomPointInside() {

        final int x = mRect.left +
                (int)(Math.random() * (mRect.right - mNumberGroup.getWidth() - mRect.left));
        final int y = mRect.top +
                (int)(Math.random() * (mRect.bottom - mNumberGroup.getHeight() - mRect.top));

        return new Point(x, y);
    }

}
