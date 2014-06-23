package com.deange.hexclock;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.widget.TextView;

import com.deange.numberview.NumberView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    private Timer mTimer;
    private NumberHelper mHelper;

    private ColourView mColourView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mColourView = (ColourView) findViewById(R.id.hex_view);

        final NumberView[] numbers = new NumberView[NumberHelper.NUMBERS_COUNT];
        numbers[NumberHelper.HOUR_TENS] = (NumberView) findViewById(R.id.hour_tens);
        numbers[NumberHelper.HOUR_ONES] = (NumberView) findViewById(R.id.hour_ones);
        numbers[NumberHelper.MINUTE_TENS] = (NumberView) findViewById(R.id.minute_tens);
        numbers[NumberHelper.MINUTE_ONES] = (NumberView) findViewById(R.id.minute_ones);
        numbers[NumberHelper.SECOND_TENS] = (NumberView) findViewById(R.id.second_tens);
        numbers[NumberHelper.SECOND_ONES] = (NumberView) findViewById(R.id.second_ones);

        mTimer = new Timer();
        mHelper = new NumberHelper(numbers);

        mHelper.apply(new NumberHelper.NumberViewMutator() {
            @Override
            public void apply(final int type, final NumberView view) {
                final Paint paint = view.getPaint();
                paint.setColor(Color.WHITE);
                view.setPaint(paint);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            }
        });

        startTimer();
    }

    @Override
    protected void onDestroy() {
        mTimer.cancel();
        mTimer.purge();

        super.onDestroy();
    }

    private void update() {

        final Instant now = Instant.get();

        mColourView.setColour(0xFF000000 | Integer.parseInt(now.toString(), 16));
        mHelper.delegateTime(now.getHours(), now.getMinutes(), now.getSeconds());
    }

    private void startTimer() {

        final long now = System.currentTimeMillis();
        final long nextSecond = (now / 1000 + 1) * 1000;
        final int wait = (int) (nextSecond - now);

        mTimer.scheduleAtFixedRate(new UpdateTask(), wait, 1000);
    }

    private final class UpdateTask extends TimerTask {

        private final Handler mHandler = new Handler();
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
