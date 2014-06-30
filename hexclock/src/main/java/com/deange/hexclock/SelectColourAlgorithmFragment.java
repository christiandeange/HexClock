package com.deange.hexclock;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class SelectColourAlgorithmFragment extends DialogFragment {

    public static final String TAG = SelectColourAlgorithmFragment.class.getSimpleName();

    private OnColourAlgorithmSelectedListener mListener;

    public void setListener(final OnColourAlgorithmSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    public interface OnColourAlgorithmSelectedListener {
        public void onColourAlgorithmSelected(final int algorithmType);
    }

}
