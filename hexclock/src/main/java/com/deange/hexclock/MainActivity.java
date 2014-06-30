package com.deange.hexclock;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity
        extends FragmentActivity
        implements View.OnClickListener, SelectColourAlgorithmFragment.OnColourAlgorithmSelectedListener {

    private static final String KEY_ROTATED = "rotated";
    private static final int ACTION_BAR_DELAY = 3000;
    private static final boolean ENABLE_COLOUR_SWITCHING = false;

    private static final int CLOSE_ACTION_BAR = 0;

    private boolean mRotated;
    private HexAnimator mAnimator;
    private SelectColourAlgorithmFragment mFragment;

    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case CLOSE_ACTION_BAR:
                    if (getActionBar() != null) {
                        getActionBar().hide();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.hide();

        setContentView(R.layout.hex_clock_layout);
        final ColourView colourView = (ColourView) findViewById(R.id.hex_view);
        final NumberGroup numberGroup = (NumberGroup) findViewById(R.id.numbers_view);

        colourView.setOnClickListener(this);

        mRotated = (savedInstanceState != null && savedInstanceState.getBoolean(KEY_ROTATED));
        mAnimator = new HexAnimator(colourView, numberGroup, !mRotated);
        mAnimator.start();

        final FragmentManager manager = getSupportFragmentManager();
        mFragment = (SelectColourAlgorithmFragment)
                manager.findFragmentByTag(SelectColourAlgorithmFragment.TAG);
        if (mFragment != null) {
            mFragment.setListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final boolean isColourChooser = item.getItemId() == R.id.menu_choose_interpreter;
        if (isColourChooser) {
            showSelectColourAlgorithmFragment();
        }
        return isColourChooser || super.onOptionsItemSelected(item);
    }

    private void showSelectColourAlgorithmFragment() {

        final FragmentManager manager = getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();

        mFragment = (SelectColourAlgorithmFragment)
                manager.findFragmentByTag(SelectColourAlgorithmFragment.TAG);
        if (mFragment != null) {
            transaction.remove(mFragment);
        }

        mFragment = new SelectColourAlgorithmFragment();
        mFragment.setListener(this);
        mFragment.show(transaction, SelectColourAlgorithmFragment.TAG);
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

    @Override
    public void onClick(final View v) {

        if (!ENABLE_COLOUR_SWITCHING) {
            return;
        }

        final ActionBar actionBar = getActionBar();
        actionBar.show();

        mHandler.removeCallbacksAndMessages(CLOSE_ACTION_BAR);
        mHandler.sendMessageDelayed(
                Message.obtain(mHandler, CLOSE_ACTION_BAR, CLOSE_ACTION_BAR), ACTION_BAR_DELAY);
    }

    @Override
    public void onColourAlgorithmSelected(final int algorithmType) {
        // TODO
    }
}
