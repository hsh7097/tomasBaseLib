package com.example.hasang.tomas.customwidget.materialdialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.View;

import com.rey.material.app.Dialog;

/**
 * Created by hasang on 16. 5. 4..
 */
public class MaterialDialogFragment extends android.support.v4.app.DialogFragment {

    /**
     * Interface definition for passing style data.
     */
    public interface Builder {
        /**
         * Get a Dialog instance used for this fragment.
         *
         * @param context A Context instance.
         * @return The Dialog will be used for this fragment.
         */
        public MaterialDialog build(Context context);

        /**
         * Handle click event on Positive Action.
         */
        public void onPositiveActionClicked(MaterialDialogFragment fragment);

        /**
         * Handle click event on Negative Action.
         */
        public void onNegativeActionClicked(MaterialDialogFragment fragment);

        /**
         * Handle click event on Neutral Action.
         */
        public void onNeutralActionClicked(MaterialDialogFragment fragment);
    }

    protected static final String ARG_BUILDER = "arg_builder";

    protected Builder mBuilder;

    private View.OnClickListener mActionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mBuilder == null)
                return;

            if (v.getId() == Dialog.ACTION_POSITIVE)
                mBuilder.onPositiveActionClicked(MaterialDialogFragment.this);
            else if (v.getId() == Dialog.ACTION_NEGATIVE)
                mBuilder.onNegativeActionClicked(MaterialDialogFragment.this);
            else if (v.getId() == Dialog.ACTION_NEUTRAL)
                mBuilder.onNeutralActionClicked(MaterialDialogFragment.this);
        }
    };

    public static MaterialDialogFragment newInstance(Builder builder) {
        MaterialDialogFragment fragment = new MaterialDialogFragment();
        fragment.mBuilder = builder;
        return fragment;
    }

    @NonNull
    @Override
    public MaterialDialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog dialog = mBuilder == null ? new MaterialDialog(getActivity()) : mBuilder.build(getActivity());
        dialog.positiveActionClickListener(mActionListener)
                .negativeActionClickListener(mActionListener)
                .neutralActionClickListener(mActionListener);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mBuilder == null)
            mBuilder = (Builder) savedInstanceState.getParcelable(ARG_BUILDER);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mBuilder != null && mBuilder instanceof Parcelable)
            outState.putParcelable(ARG_BUILDER, (Parcelable) mBuilder);
    }

    @Override
    public void onDestroyView() {
        android.app.Dialog dialog = getDialog();

        if (dialog != null && dialog instanceof Dialog)
            ((Dialog) dialog).dismissImmediately();

        super.onDestroyView();
    }

}
