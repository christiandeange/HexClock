package com.deange.hexclock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class ColourView extends View {

    private int mColour;

    public ColourView(final Context context) {
        this(context, null);
    }

    public ColourView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColourView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setColour(0xFF000000);
    }

    public int getColour() {
        return mColour;
    }

    public void setColour(final int colour) {
        mColour = colour;
        setBackgroundColor(mColour);
    }
}
