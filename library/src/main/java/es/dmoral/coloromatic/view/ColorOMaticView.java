package es.dmoral.coloromatic.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.coloromatic.ColorOMaticUtil;
import es.dmoral.coloromatic.IndicatorMode;
import es.dmoral.coloromatic.R;
import es.dmoral.coloromatic.colormode.Channel;
import es.dmoral.coloromatic.colormode.ColorMode;

/**
 * Color-O-Matic
 * Copyright (C) 2016 - GrenderG
 * <p>
 * This file is part of Color-O-Matic.
 * <p>
 * Color-O-Matic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Color-O-Matic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Color-O-Matic.  If not, see <http://www.gnu.org/licenses/>.
 */

public class ColorOMaticView extends RelativeLayout {

    public final static int DEFAULT_COLOR = Color.GRAY;
    public final static ColorMode DEFAULT_MODE = ColorMode.RGB;
    public final static IndicatorMode DEFAULT_INDICATOR = IndicatorMode.DECIMAL;
    public final static boolean DEFAULT_TEXT_INDICATOR_STATE = false;
    public final static boolean DEFAULT_TEXT_INDICATOR_EDITABLE = false;

    private final ColorMode colorMode;
    private
    @ColorInt
    int currentColor;
    private IndicatorMode indicatorMode;
    private boolean showTextIndicator;
    private boolean isTextIndicatorEditable;
    private String lastColor = "";
    private Context context;

    public ColorOMaticView(Context context) {
        this(DEFAULT_COLOR, DEFAULT_TEXT_INDICATOR_STATE, DEFAULT_TEXT_INDICATOR_EDITABLE, DEFAULT_MODE, DEFAULT_INDICATOR, context);
        this.context = context;
    }

    public ColorOMaticView(@ColorInt int initialColor, ColorMode colorMode, Context context) {
        this(initialColor, false, false, colorMode, DEFAULT_INDICATOR, context);
        this.context = context;
    }

    public ColorOMaticView(@ColorInt int initialColor, boolean showTextIndicator, boolean isTextIndicatorEditable, ColorMode colorMode, IndicatorMode indicatorMode, Context context) {
        super(context);
        this.indicatorMode = indicatorMode;
        this.colorMode = colorMode;
        this.currentColor = initialColor;
        this.showTextIndicator = showTextIndicator;
        this.isTextIndicatorEditable = isTextIndicatorEditable;
        this.context = context;
        init();
    }

    void init() {
        inflate(getContext(), R.layout.chroma_view, this);
        setClipToPadding(false);

        final List<Channel> channels = colorMode.getColorMode().getChannels();
        final List<ChannelView> channelViews = new ArrayList<>();
        for (Channel c : channels) {
            channelViews.add(new ChannelView(c, currentColor, getContext()));
        }

        final View colorView = findViewById(R.id.color_view);
        colorView.setBackgroundColor(currentColor);
        final TextView colorTextIndicator = (TextView) findViewById(R.id.tv_color_indicator);
        if (showTextIndicator)
            colorTextIndicator.setVisibility(View.VISIBLE);
        final EditText editTextIndicator = (EditText) findViewById(R.id.ed_color_edit);
        if (showTextIndicator && isTextIndicatorEditable) {
            if (indicatorMode == IndicatorMode.DECIMAL)
                editTextIndicator.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            colorTextIndicator.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    colorTextIndicator.setVisibility(GONE);
                    editTextIndicator.setVisibility(VISIBLE);
                    editTextIndicator.requestFocus();
                }
            });
            editTextIndicator.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if (isHexCodeValid(editTextIndicator.getText().toString()) == -1)
                            editTextIndicator.setText(colorTextIndicator.getText());
                        editTextIndicator.setVisibility(GONE);
                        colorTextIndicator.setVisibility(VISIBLE);
                        editTextIndicator.setSelection(editTextIndicator.getText().length());
                    } else {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editTextIndicator, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            });
            editTextIndicator.setFilters(new InputFilter[]{new InputFilter.LengthFilter(getTextLength()), new InputFilter.AllCaps()});
            editTextIndicator.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int current = isHexCodeValid(s.toString());
                    if (!lastColor.equals(s.toString()) && current != -1 && (colorMode == ColorMode.HSV ? (s.length() >= 5) : s.length() >= getTextLength())) {
                        lastColor = s.toString();
                        switch (colorMode) {
                            case ARGB:
                                for (ChannelView channelView : channelViews) {
                                    Channel channel = channelView.getChannel();
                                    switch (getResources().getString(channel.getNameResourceId())) {
                                        case "A":
                                            channelView.setProgress(Color.alpha(current));
                                            break;
                                        case "R":
                                            channelView.setProgress(Color.red(current));
                                            break;
                                        case "G":
                                            channelView.setProgress(Color.green(current));
                                            break;
                                        case "B":
                                            channelView.setProgress(Color.blue(current));
                                            break;
                                    }
                                }
                                break;
                            case RGB:
                                for (ChannelView channelView : channelViews) {
                                    Channel channel = channelView.getChannel();
                                    switch (getResources().getString(channel.getNameResourceId())) {
                                        case "R":
                                            channelView.setProgress(Color.red(current));
                                            break;
                                        case "G":
                                            channelView.setProgress(Color.green(current));
                                            break;
                                        case "B":
                                            channelView.setProgress(Color.blue(current));
                                            break;
                                    }
                                }
                                break;
                            case HSV:
                                for (ChannelView channelView : channelViews) {
                                    Channel channel = channelView.getChannel();
                                    String[] progress = s.toString().split(" ");
                                    switch (getResources().getString(channel.getNameResourceId())) {
                                        case "H":
                                            channelView.setProgress(Integer.parseInt(progress[0]));
                                            break;
                                        case "S":
                                            channelView.setProgress(Integer.parseInt(progress[1]));
                                            break;
                                        case "V":
                                            channelView.setProgress(Integer.parseInt(progress[2]));
                                            break;
                                    }
                                }
                                break;
                        }
                        colorTextIndicator.setText(s);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        updateText(colorView, colorTextIndicator, editTextIndicator, channelViews, channels);

        ChannelView.OnProgressChangedListener seekBarChangeListener = new ChannelView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(boolean fromSeek) {
                List<Channel> channels = new ArrayList<>();
                for (ChannelView chan : channelViews)
                    channels.add(chan.getChannel());
                if (!fromSeek) {
                    updateText(colorView, colorTextIndicator, channelViews, channels);
                } else {
                    updateText(colorView, colorTextIndicator, editTextIndicator, channelViews, channels);
                }
            }
        };

        ViewGroup channelContainer = (ViewGroup) findViewById(R.id.channel_container);
        for (ChannelView c : channelViews) {
            channelContainer.addView(c);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) c.getLayoutParams();
            params.topMargin =
                    getResources().getDimensionPixelSize(R.dimen.channel_view_margin_top);
            params.bottomMargin =
                    getResources().getDimensionPixelSize(R.dimen.channel_view_margin_bottom);

            c.registerListener(seekBarChangeListener);
        }
    }

    private int getTextLength() {
        switch (colorMode) {
            case ARGB:
                return 9;
            case RGB:
                return 7;
            default:
            case HSV:
                return 11;

        }
    }

    private int isHexCodeValid(String hex) {
        if (indicatorMode == IndicatorMode.HEX) {
            try {
                return Color.parseColor(hex);
            } catch (IllegalArgumentException e) {
                return -1;
            }
        } else {
            try {
                if (hex.contains(" ")) {
                    String[] array = hex.trim().split(" ");
                    if (array.length == 3) {
                        for (String num : array) {
                            if (!TextUtils.isDigitsOnly(num))
                                return -1;
                        }
                        if (Integer.parseInt(array[0]) <= 360 &&
                                Integer.parseInt(array[1]) <= 100 &&
                                Integer.parseInt(array[2]) <= 100) {
                            return -2;
                        } else {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                } else {
                    return -1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
    }

    private void updateText(View colorView, TextView colorTextIndicator, EditText colorTextEdit,
                            List<ChannelView> channelViews, List<Channel> channels) {
        currentColor = colorMode.getColorMode().evaluateColor(channels);
        colorView.setBackgroundColor(currentColor);

        if (indicatorMode == IndicatorMode.HEX) {
            colorTextIndicator.setText(ColorOMaticUtil.getFormattedColorString(currentColor, colorMode == ColorMode.ARGB));
            colorTextEdit.setText(ColorOMaticUtil.getFormattedColorString(currentColor, colorMode == ColorMode.ARGB));
        } else {
            String decText = "";
            for (ChannelView chan : channelViews)
                decText += chan.getChannel().getProgress() + " ";
            colorTextIndicator.setText(String.valueOf(decText.trim()));
            colorTextEdit.setText(String.valueOf(decText.trim()));
        }

        if (!getResources().getBoolean(R.bool.tablet_mode)) {
            colorTextIndicator.setTextColor(getInverseColor(currentColor));
            colorTextEdit.setTextColor(getInverseColor(currentColor));
        }
        colorTextEdit.setSelection(colorTextEdit.getText().length());
    }

    private void updateText(View colorView, TextView colorTextIndicator,
                            List<ChannelView> channelViews, List<Channel> channels) {
        currentColor = colorMode.getColorMode().evaluateColor(channels);
        colorView.setBackgroundColor(currentColor);

        if (indicatorMode == IndicatorMode.HEX) {
            colorTextIndicator.setText(ColorOMaticUtil.getFormattedColorString(currentColor, colorMode == ColorMode.ARGB));
        } else {
            String decText = "";
            for (ChannelView chan : channelViews)
                decText += chan.getChannel().getProgress() + " ";
            colorTextIndicator.setText(String.valueOf(decText.trim()));
        }

        if (!getResources().getBoolean(R.bool.tablet_mode)) {
            colorTextIndicator.setTextColor(getInverseColor(currentColor));
        }
    }

    // Based on http://stackoverflow.com/a/5761067/4208583
    private int getInverseColor(int color) {
        return Color.argb(Color.alpha(color), 255 - Color.red(color), 255 -
                Color.green(color), 255 - Color.blue(color));
    }

    public ColorMode getColorMode() {
        return colorMode;
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public IndicatorMode getIndicatorMode() {
        return indicatorMode;
    }

    public boolean isShowTextIndicator() {
        return showTextIndicator;
    }

    public boolean isTextIndicatorEditable() {
        return isTextIndicatorEditable;
    }

    public void enableButtonBar(final ButtonBarListener listener) {
        LinearLayout buttonBar = (LinearLayout) findViewById(R.id.button_bar);
        TextView positiveButton = (TextView) buttonBar.findViewById(R.id.positive_button);
        TextView negativeButton = (TextView) buttonBar.findViewById(R.id.negative_button);

        if (listener != null) {
            buttonBar.setVisibility(VISIBLE);
            positiveButton.setTextColor(ColorOMaticUtil.getThemeAccentColor(getContext()));
            negativeButton.setTextColor(ColorOMaticUtil.getThemeAccentColor(getContext()));
            positiveButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPositiveButtonClick(currentColor);
                }
            });

            negativeButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onNegativeButtonClick();
                }
            });
        } else {
            buttonBar.setVisibility(GONE);
            positiveButton.setOnClickListener(null);
            negativeButton.setOnClickListener(null);
        }
    }

    public interface ButtonBarListener {
        void onPositiveButtonClick(int color);

        void onNegativeButtonClick();
    }
}
