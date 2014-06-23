package com.deange.hexclock;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    private HexAnimator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final ColourView colourView = (ColourView) findViewById(R.id.hex_view);
        final NumberGroup numberGroup = (NumberGroup) findViewById(R.id.numbers_view);

        mAnimator = new HexAnimator(colourView, numberGroup);
        mAnimator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAnimator.close();
    }
}
