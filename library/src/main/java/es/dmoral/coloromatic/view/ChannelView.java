package es.dmoral.coloromatic.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import es.dmoral.coloromatic.R;
import es.dmoral.coloromatic.colormode.Channel;

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

@SuppressLint("ViewConstructor")
public class ChannelView extends RelativeLayout {

    private final Channel channel;

    private Context context;

    private OnProgressChangedListener listener;

    public ChannelView(Channel channel, @ColorInt int color, Context context) {
        super(context);
        this.channel = channel;
        this.context = context;

        channel.setProgress(channel.getExtractor().extract(color));
        if (channel.getProgress() < channel.getMin() || channel.getProgress() > channel.getMax()) {
            throw new IllegalArgumentException(
                    "Initial progress for channel: " + channel.getClass().getSimpleName()
                            + " must be between " + channel.getMin() + " and " + channel.getMax());
        }

        View rootView = inflate(context, R.layout.channel_row, this);
        bindViews(rootView);
    }

    private void bindViews(final View rootView) {
        TextView label = (TextView) rootView.findViewById(R.id.label);
        label.setText(context.getString(channel.getNameResourceId()));

        SeekBar seekbar = (SeekBar) rootView.findViewById(R.id.seekbar);
        seekbar.setMax(channel.getMax());
        seekbar.setProgress(channel.getProgress());

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                channel.setProgress(progress);
                if (listener != null) listener.onProgressChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void registerListener(OnProgressChangedListener listener) {
        this.listener = listener;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        listener = null;
    }

    public interface OnProgressChangedListener {
        void onProgressChanged();
    }
}
