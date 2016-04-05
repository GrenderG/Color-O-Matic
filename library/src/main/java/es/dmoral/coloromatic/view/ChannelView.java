package es.dmoral.coloromatic.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import es.dmoral.coloromatic.ColorOMaticUtil;
import es.dmoral.coloromatic.IndicatorMode;
import es.dmoral.coloromatic.R;
import es.dmoral.coloromatic.colormode.Channel;
import es.dmoral.coloromatic.colormode.ColorMode;

import java.util.HashMap;

/**
 * Created by Daniel Morales on 04/04/2016.
 */
public class ChannelView extends RelativeLayout {

    private final Channel channel;

    private Context context;

    private OnProgressChangedListener listener;

    public interface OnProgressChangedListener {
        void onProgressChanged();
    }

    public ChannelView(Channel channel, @ColorInt int color, Context context) {
        super(context);
        this.channel = channel;
        this.context = context;

        channel.setProgress(channel.getExtractor().extract(color));
        if(channel.getProgress() < channel.getMin() || channel.getProgress() > channel.getMax()) {
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
                if(listener != null) listener.onProgressChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
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
}
