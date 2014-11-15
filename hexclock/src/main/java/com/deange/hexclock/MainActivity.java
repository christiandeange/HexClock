package com.deange.hexclock;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class MainActivity
        extends FragmentActivity {

    private static final String KEY_ROTATED = "rotated";

    private boolean mRotated;
    private boolean mWentThroughOnCreate;
    private HexAnimator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWentThroughOnCreate = true;

        setContentView(R.layout.hex_clock_layout);
        final ColourView colourView = (ColourView) findViewById(R.id.hex_view);
        final NumberGroup numberGroup = (NumberGroup) findViewById(R.id.numbers_view);

        mRotated = (savedInstanceState != null && savedInstanceState.getBoolean(KEY_ROTATED));
        mAnimator = new HexAnimator(colourView, numberGroup, !mRotated);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAnimator.startWithAnimation(mWentThroughOnCreate && !mRotated);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWentThroughOnCreate = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAnimator.stop();
        mAnimator.close();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putBoolean(KEY_ROTATED, true);
        super.onSaveInstanceState(outState);
    }

}
