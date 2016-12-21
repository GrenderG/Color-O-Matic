package es.dmoral.coloromatic.colormode.mode;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

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

public class HSV implements AbstractColorMode {

    float[] colorToHSV(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return hsv;
    }

    @Override
    public List<Channel> getChannels() {
        List<Channel> list = new ArrayList<>();

        list.add(new Channel(R.string.channel_hue, 0, 360, new Channel.ColorExtractor() {
            @Override
            public int extract(int color) {
                return (int) colorToHSV(color)[0];
            }
        }));

        list.add(new Channel(R.string.channel_saturation, 0, 100, new Channel.ColorExtractor() {
            @Override
            public int extract(int color) {
                return (int) (colorToHSV(color)[1] * 100);
            }
        }));

        list.add(new Channel(R.string.channel_value, 0, 100, new Channel.ColorExtractor() {
            @Override
            public int extract(int color) {
                return (int) (colorToHSV(color)[2] * 100);
            }
        }));

        return list;
    }

    @Override
    public int evaluateColor(List<Channel> channels) {
        return Color.HSVToColor(new float[]{
                ((float) channels.get(0).getProgress()),
                ((float) channels.get(1).getProgress()) / 100,
                ((float) channels.get(2).getProgress()) / 100
        });
    }
}
