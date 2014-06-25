package com.deange.hexclock;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    private static final String KEY_ROTATED = "rotated";

    private boolean mRotated;
    private HexAnimator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.hex_clock_layout);
        final ColourView colourView = (ColourView) findViewById(R.id.hex_view);
        final NumberGroup numberGroup = (NumberGroup) findViewById(R.id.numbers_view);

        mRotated = (savedInstanceState != null && savedInstanceState.getBoolean(KEY_ROTATED));
        mAnimator = new HexAnimator(colourView, numberGroup, !mRotated);
        mAnimator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAnimator.close();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putBoolean(KEY_ROTATED, true);
        super.onSaveInstanceState(outState);
    }
}
