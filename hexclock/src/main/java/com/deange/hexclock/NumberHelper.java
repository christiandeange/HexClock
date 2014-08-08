package com.deange.hexclock;

import com.deange.numberview.NumberView;

public final class NumberHelper {

    public static final int HOUR_TENS = 0;
    public static final int HOUR_ONES = 1;
    public static final int MINUTE_TENS = 2;
    public static final int MINUTE_ONES = 3;
    public static final int SECOND_TENS = 4;
    public static final int SECOND_ONES = 5;
    public static final int NUMBERS_COUNT = 6;

    private NumberView[] mViews;

    public NumberHelper(final NumberView[] views) {
        mViews = views;
    }

    public void delegateTime(final int hours, final int minutes, final int seconds,
                             final boolean immediate) {
        advanceIfNecessary(get(HOUR_TENS),   hours / 10,   immediate);
        advanceIfNecessary(get(HOUR_ONES),   hours % 10,   immediate);
        advanceIfNecessary(get(MINUTE_TENS), minutes / 10, immediate);
        advanceIfNecessary(get(MINUTE_ONES), minutes % 10, immediate);
        advanceIfNecessary(get(SECOND_TENS), seconds / 10, immediate);
        advanceIfNecessary(get(SECOND_ONES), seconds % 10, immediate);
    }

    private NumberView get(final int index) {
        return mViews[index];
    }

    private void advanceIfNecessary(final NumberView view, final int next,
                                    final boolean immediate) {
        if (immediate) {
            view.advanceImmediate(next);

        } else if (view.getCurrentNumber() != next) {
            view.advance(next);
        }
    }

    public void apply(final NumberViewMutator mutator) {
        for (int i = 0; i < NUMBERS_COUNT; i++) {
            mutator.apply(i, get(i));
        }
    }

    public interface NumberViewMutator {
        public void apply(final int type, final NumberView view);
    }

}
