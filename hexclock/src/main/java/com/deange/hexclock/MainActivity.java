package com.deange.hexclock;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    private final Timer mTimer = new Timer();

    private ColourView mColourView;
    private TextView mHexView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mColourView = (ColourView) findViewById(R.id.hex_view);
        mHexView = (TextView) findViewById(R.id.hex_text);

        startTimer();
    }

    @Override
    protected void onDestroy() {
        mTimer.cancel();
        mTimer.purge();

        super.onDestroy();
    }

    private void update() {
        final String now = Instant.get().toString();
        mHexView.setText("#" + now);
        mColourView.setColour(0xFF000000 | Integer.parseInt(now, 16));
    }

    private void startTimer() {
        mTimer.scheduleAtFixedRate(new UpdateTask(), 0, 1000);
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
