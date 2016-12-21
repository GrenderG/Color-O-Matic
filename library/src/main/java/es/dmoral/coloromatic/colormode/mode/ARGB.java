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

public class ARGB implements AbstractColorMode {

    @Override
    public List<Channel> getChannels() {
        List<Channel> list = new ArrayList<>();

        list.add(new Channel(R.string.channel_alpha, 0, 255, new Channel.ColorExtractor() {
            @Override
            public int extract(int color) {
                return Color.alpha(color);
            }
        }));

        list.add(new Channel(R.string.channel_red, 0, 255, new Channel.ColorExtractor() {
            @Override
            public int extract(int color) {
                return Color.red(color);
            }
        }));

        list.add(new Channel(R.string.channel_green, 0, 255, new Channel.ColorExtractor() {
            @Override
            public int extract(int color) {
                return Color.green(color);
            }
        }));

        list.add(new Channel(R.string.channel_blue, 0, 255, new Channel.ColorExtractor() {
            @Override
            public int extract(int color) {
                return Color.blue(color);
            }
        }));

        return list;
    }

    @Override
    public int evaluateColor(List<Channel> channels) {
        return Color.argb(
                channels.get(0).getProgress(),
                channels.get(1).getProgress(),
                channels.get(2).getProgress(),
                channels.get(3).getProgress());
    }
}
