package com.deange.hexclock;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.deange.numberview.NumberView;

public class NumberGroup extends FrameLayout {

    private NumberHelper mHelper;

    public NumberGroup(final Context context) {
        this(context, null);
    }

    public NumberGroup(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberGroup(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.number_view_group, this);

        final NumberView[] numbers = new NumberView[NumberHelper.NUMBERS_COUNT];
        numbers[NumberHelper.HOUR_TENS] = (NumberView) findViewById(R.id.hour_tens);
        numbers[NumberHelper.HOUR_ONES] = (NumberView) findViewById(R.id.hour_ones);
        numbers[NumberHelper.MINUTE_TENS] = (NumberView) findViewById(R.id.minute_tens);
        numbers[NumberHelper.MINUTE_ONES] = (NumberView) findViewById(R.id.minute_ones);
        numbers[NumberHelper.SECOND_TENS] = (NumberView) findViewById(R.id.second_tens);
        numbers[NumberHelper.SECOND_ONES] = (NumberView) findViewById(R.id.second_ones);

        final float textSize = getResources().getDimensionPixelSize(R.dimen.numberview_text_size);
        mHelper = new NumberHelper(numbers);
        mHelper.apply(new NumberHelper.NumberViewMutator() {
            @Override
            public void apply(final int type, final NumberView view) {
                final Paint paint = view.getPaint();
                paint.setColor(Color.WHITE);
                view.setPaint(paint);
                view.setTextSize(textSize);
            }
        });
    }

    public void apply(final NumberHelper.NumberViewMutator mutator) {
        mHelper.apply(mutator);
    }

    public void update(final int hours, final int minutes, final int seconds) {
        mHelper.delegateTime(hours, minutes, seconds);
    }
}
