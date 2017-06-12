package es.dmoral.coloromatic;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import es.dmoral.coloromatic.colormode.ColorMode;
import es.dmoral.coloromatic.view.ColorOMaticView;

/**
 * Color-O-Matic
 * Copyright (C) 2016 - GrenderG
 *
 * This file is part of Color-O-Matic.
 *
 * Color-O-Matic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Color-O-Matic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Color-O-Matic.  If not, see <http://www.gnu.org/licenses/>.
 */

public class ColorOMaticDialog extends DialogFragment {

    private final static String ARG_INITIAL_COLOR = "arg_initial_color";
    private final static String ARG_COLOR_MODE_ID = "arg_color_mode_id";
    private final static String ARG_INDICATOR_MODE = "arg_indicator_mode";
    private final static String ARG_SHOW_COLOR_INDICATOR = "arg_show_color_indicator";
    private final static String ARG_COLOR_INDICATOR_EDITABLE = "arg_color_indicator_editable";

    private OnColorSelectedListener listener;
    private ColorOMaticView colorOMaticView;

    private static ColorOMaticDialog newInstance(@ColorInt int initialColor, ColorMode colorMode, IndicatorMode indicatorMode, boolean showColorIndicator, boolean isColorIndicatorEditable) {
        ColorOMaticDialog fragment = new ColorOMaticDialog();
        fragment.setArguments(makeArgs(initialColor, colorMode, indicatorMode, showColorIndicator, isColorIndicatorEditable));
        return fragment;
    }

    private static Bundle makeArgs(@ColorInt int initialColor, ColorMode colorMode, IndicatorMode indicatorMode, boolean showColorIndicator, boolean isColorIndicatorEditable) {
        Bundle args = new Bundle();
        args.putInt(ARG_INITIAL_COLOR, initialColor);
        args.putInt(ARG_COLOR_MODE_ID, colorMode.ordinal());
        args.putInt(ARG_INDICATOR_MODE, indicatorMode.ordinal());
        args.putBoolean(ARG_SHOW_COLOR_INDICATOR, showColorIndicator);
        args.putBoolean(ARG_COLOR_INDICATOR_EDITABLE, isColorIndicatorEditable);
        return args;
    }

    public void setListener(OnColorSelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            colorOMaticView = new ColorOMaticView(
                    getArguments().getInt(ARG_INITIAL_COLOR),
                    getArguments().getBoolean(ARG_SHOW_COLOR_INDICATOR),
                    getArguments().getBoolean(ARG_COLOR_INDICATOR_EDITABLE),
                    ColorMode.values()[
                            getArguments().getInt(ARG_COLOR_MODE_ID)],
                    IndicatorMode.values()[
                            getArguments().getInt(ARG_INDICATOR_MODE)],
                    getActivity());
        } else {
            colorOMaticView = new ColorOMaticView(
                    savedInstanceState.getInt(ARG_INITIAL_COLOR, ColorOMaticView.DEFAULT_COLOR),
                    savedInstanceState.getBoolean(ARG_SHOW_COLOR_INDICATOR),
                    getArguments().getBoolean(ARG_COLOR_INDICATOR_EDITABLE),
                    ColorMode.values()[
                            savedInstanceState.getInt(ARG_COLOR_MODE_ID)],
                    IndicatorMode.values()[
                            savedInstanceState.getInt(ARG_INDICATOR_MODE)],
                    getActivity());
        }

        colorOMaticView.enableButtonBar(new ColorOMaticView.ButtonBarListener() {
            @Override
            public void onPositiveButtonClick(int color) {
                if (listener != null) listener.onColorSelected(color);
                dismiss();
            }

            @Override
            public void onNegativeButtonClick() {
                dismiss();
            }
        });

        final AlertDialog ad = new AlertDialog.Builder(getActivity(), getTheme()).setView(colorOMaticView).create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            ad.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    measureLayout(ad);
                }
            });
        } else {
            measureLayout(ad);
        }

        return ad;
    }

    void measureLayout(AlertDialog ad) {
        double multiplier = getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE
                ? getResources().getBoolean(R.bool.tablet_mode) ? 2 : 1.5
                : 1;

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE
                ? (int) (metrics.heightPixels * 0.8)
                : WindowManager.LayoutParams.WRAP_CONTENT;

        int width = (int) (getResources().getDimensionPixelSize(R.dimen.chroma_dialog_width) * multiplier);

        ad.getWindow().setLayout(width, height);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putAll(makeArgs(colorOMaticView.getCurrentColor(), colorOMaticView.getColorMode(), colorOMaticView.getIndicatorMode(), colorOMaticView.isShowTextIndicator(), colorOMaticView.isTextIndicatorEditable()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listener = null;
    }

    public static class Builder {
        private
        @ColorInt
        int initialColor = ColorOMaticView.DEFAULT_COLOR;
        private ColorMode colorMode = ColorOMaticView.DEFAULT_MODE;
        private IndicatorMode indicatorMode = IndicatorMode.DECIMAL;
        private boolean showColorIndicator = ColorOMaticView.DEFAULT_TEXT_INDICATOR_STATE;
        private boolean isColorIndicatorEditable = ColorOMaticView.DEFAULT_TEXT_INDICATOR_EDITABLE;
        private OnColorSelectedListener listener = null;

        public Builder initialColor(@ColorInt int initialColor) {
            this.initialColor = initialColor;
            return this;
        }

        public Builder colorMode(ColorMode colorMode) {
            this.colorMode = colorMode;
            return this;
        }

        public Builder showColorIndicator(boolean showColorIndicator) {
            this.showColorIndicator = showColorIndicator;
            return this;
        }

        public Builder isColorIndicatorEditable(boolean isColorIndicatorEditable) {
            this.isColorIndicatorEditable = isColorIndicatorEditable;
            return this;
        }

        public Builder indicatorMode(IndicatorMode indicatorMode) {
            this.indicatorMode = indicatorMode;
            return this;
        }

        public Builder onColorSelected(OnColorSelectedListener listener) {
            this.listener = listener;
            return this;
        }

        public ColorOMaticDialog create() {
            ColorOMaticDialog fragment = newInstance(initialColor, colorMode, indicatorMode, showColorIndicator, isColorIndicatorEditable);
            fragment.setListener(listener);
            return fragment;
        }
    }
}
