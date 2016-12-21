package es.dmoral.coloromatic.colormode;

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

public final class Channel {

    private final int nameResourceId;
    private final int min;
    private final int max;
    private final ColorExtractor extractor;

    private int progress = 0;

    public Channel(int nameResourceId, int min, int max, ColorExtractor extractor) {
        this.nameResourceId = nameResourceId;
        this.min = min;
        this.max = max;
        this.extractor = extractor;
    }

    public Channel(int nameResourceId, int min, int max, int progress, ColorExtractor extractor) {
        this.nameResourceId = nameResourceId;
        this.min = min;
        this.max = max;
        this.extractor = extractor;
        this.progress = progress;
    }

    public int getNameResourceId() {
        return nameResourceId;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public ColorExtractor getExtractor() {
        return extractor;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public interface ColorExtractor {
        int extract(int color);
    }
}
