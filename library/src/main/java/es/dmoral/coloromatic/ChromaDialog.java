package es.dmoral.coloromatic;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import es.dmoral.coloromatic.colormode.ColorMode;
import es.dmoral.coloromatic.view.ChromaView;

/**
 * Created by Daniel Morales on 04/04/2016.
 */
public class ChromaDialog extends DialogFragment {

    private final static String ARG_INITIAL_COLOR = "arg_initial_color";
    private final static String ARG_COLOR_MODE_ID = "arg_color_mode_id";
    private final static String ARG_INDICATOR_MODE = "arg_indicator_mode";
    private final static String ARG_SHOW_COLOR_INDICATOR = "arg_show_color_indicator";

    private OnColorSelectedListener listener;
    private ChromaView chromaView;

    private static ChromaDialog newInstance(@ColorInt int initialColor, ColorMode colorMode, IndicatorMode indicatorMode, boolean showColorIndicator) {
        ChromaDialog fragment = new ChromaDialog();
        fragment.setArguments(makeArgs(initialColor, colorMode, indicatorMode, showColorIndicator));
        return fragment;
    }

    private static Bundle makeArgs(@ColorInt int initialColor, ColorMode colorMode, IndicatorMode indicatorMode, boolean showColorIndicator) {
        Bundle args = new Bundle();
        args.putInt(ARG_INITIAL_COLOR, initialColor);
        args.putInt(ARG_COLOR_MODE_ID, colorMode.ordinal());
        args.putInt(ARG_INDICATOR_MODE, indicatorMode.ordinal());
        args.putBoolean(ARG_SHOW_COLOR_INDICATOR, showColorIndicator);
        return args;
    }

    public static class Builder {
        private @ColorInt int initialColor = ChromaView.DEFAULT_COLOR;
        private ColorMode colorMode = ChromaView.DEFAULT_MODE;
        private IndicatorMode indicatorMode = IndicatorMode.DECIMAL;
        private boolean showColorIndicator = ChromaView.DEFAULT_TEXT_INDICATOR_STATE;
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

        public Builder indicatorMode(IndicatorMode indicatorMode) {
            this.indicatorMode = indicatorMode;
            return this;
        }

        public Builder onColorSelected(OnColorSelectedListener listener) {
            this.listener = listener;
            return this;
        }

        public ChromaDialog create() {
            ChromaDialog fragment = newInstance(initialColor, colorMode, indicatorMode, showColorIndicator);
            fragment.setListener(listener);
            return fragment;
        }
    }

    public void setListener(OnColorSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(savedInstanceState == null) {
            chromaView = new ChromaView(
                    getArguments().getInt(ARG_INITIAL_COLOR),

                    getArguments().getBoolean(ARG_SHOW_COLOR_INDICATOR),

                    ColorMode.values()[
                            getArguments().getInt(ARG_COLOR_MODE_ID)],

                    IndicatorMode.values()[
                            getArguments().getInt(ARG_INDICATOR_MODE)],


                    getActivity());
        }
        else {
            chromaView = new ChromaView(

                    savedInstanceState.getInt(ARG_INITIAL_COLOR, ChromaView.DEFAULT_COLOR),

                    savedInstanceState.getBoolean(ARG_SHOW_COLOR_INDICATOR),

                    ColorMode.values()[
                            savedInstanceState.getInt(ARG_COLOR_MODE_ID)],

                    IndicatorMode.values()[
                            savedInstanceState.getInt(ARG_INDICATOR_MODE)],

                    getActivity());
        }

        chromaView.enableButtonBar(new ChromaView.ButtonBarListener() {
            @Override
            public void onPositiveButtonClick(int color) {
                if(listener != null) listener.onColorSelected(color);
                dismiss();
            }

            @Override
            public void onNegativeButtonClick() {
                dismiss();
            }
        });

        final AlertDialog ad = new AlertDialog.Builder(getActivity(), getTheme()).setView(chromaView).create();
        ad.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                int multiplier = getResources().getConfiguration()
                        .orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? 2
                        : 1;


                DisplayMetrics metrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

                int height = getResources().getConfiguration()
                        .orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? (int) (metrics.heightPixels * 0.8)
                        : WindowManager.LayoutParams.WRAP_CONTENT;

                int width = getResources().getDimensionPixelSize(R.dimen.chroma_dialog_width) * multiplier;

                ad.getWindow().setLayout(width, height);
            }
        });

        return ad;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putAll(makeArgs(chromaView.getCurrentColor(), chromaView.getColorMode(), chromaView.getIndicatorMode(), chromaView.isShowTextIndicator()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listener = null;
    }
}
